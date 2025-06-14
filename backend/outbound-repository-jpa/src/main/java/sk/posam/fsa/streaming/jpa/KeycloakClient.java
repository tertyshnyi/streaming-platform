package sk.posam.fsa.streaming.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);

    private final RestTemplate restTemplate;
    private final String keycloakUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public KeycloakClient(RestTemplate restTemplate, String keycloakUrl, String realm,
                          String clientId, String clientSecret) {
        this.restTemplate = restTemplate;
        this.keycloakUrl = keycloakUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Регистрирует пользователя в Keycloak.
     * Возвращает ID созданного пользователя.
     */
    public String registerUser(User user) {
        String token = getAdminToken();
        String url = String.format("%s/admin/realms/%s/users", keycloakUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = buildUserPayload(user);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        logger.info("Отправка запроса на регистрацию пользователя в Keycloak: {}", user.getEmail());

        ResponseEntity<Void> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (Exception e) {
            logger.error("Ошибка при регистрации пользователя", e);
            throw new KeycloakClientException("Ошибка регистрации пользователя: " + e.getMessage(), e);
        }

        if (response.getStatusCode() == HttpStatus.CREATED) {
            if (response.getHeaders().getLocation() != null) {
                String location = response.getHeaders().getLocation().toString();
                String userId = location.substring(location.lastIndexOf('/') + 1);
                logger.info("Пользователь успешно зарегистрирован, id: {}", userId);

                assignUserRole(userId);

                return userId;
            } else {
                logger.error("В ответе отсутствует заголовок Location");
                throw new KeycloakClientException("Отсутствует заголовок Location в ответе");
            }
        }

        logger.error("Не удалось зарегистрировать пользователя, статус: {}", response.getStatusCode());
        throw new KeycloakClientException("Ошибка регистрации пользователя в Keycloak. Статус: " + response.getStatusCode());
    }

    /**
     * Обновляет существующего пользователя.
     */
    public void updateUser(User user) {
        String token = getAdminToken();
        String url = String.format("%s/admin/realms/%s/users/%s", keycloakUrl, realm, user.getKeycloakId());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = buildUserUpdatePayload(user);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
            logger.info("Пользователь обновлен в Keycloak: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя", e);
            throw new KeycloakClientException("Ошибка обновления пользователя в Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Получение токена администратора для доступа к Keycloak Admin API.
     */
    private String getAdminToken() {
        String url = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object accessTokenObj = response.getBody().get("access_token");
                if (accessTokenObj != null) {
                    return accessTokenObj.toString();
                }
            }

            logger.error("Не удалось получить токен администратора, статус: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Исключение при получении токена администратора", e);
        }

        throw new KeycloakClientException("Не удалось получить токен администратора");
    }

    /**
     * Построение payload для создания пользователя.
     */
    private Map<String, Object> buildUserPayload(User user) {
        List<Map<String, Object>> credentials = List.of(
                Map.of(
                        "type", "password",
                        "value", user.getPassword(),
                        "temporary", false
                )
        );

        Map<String, List<String>> attributes = new LinkedHashMap<>();
        if (user.getPhoneNumber() != null) {
            attributes.put("phoneNumber", List.of(user.getPhoneNumber()));
        }
        if (user.getProfileImg() != null) {
            attributes.put("profileImg", List.of(user.getProfileImg()));
        }
        if (user.getName() != null) {
            attributes.put("name", List.of(user.getName()));
        }

        return Map.of(
                "username", user.getEmail(),
                "email", user.getEmail(),
                "enabled", true,
                "credentials", credentials,
                "attributes", attributes
        );
    }

    /**
     * Построение payload для обновления пользователя.
     */
    private Map<String, Object> buildUserUpdatePayload(User user) {
        Map<String, Object> payload = new LinkedHashMap<>();

        if (user.getEmail() != null) {
            payload.put("username", user.getEmail());
            payload.put("email", user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            List<Map<String, Object>> credentials = List.of(
                    Map.of(
                            "type", "password",
                            "value", user.getPassword(),
                            "temporary", false
                    )
            );
            payload.put("credentials", credentials);
        }

        Map<String, List<String>> attributes = new LinkedHashMap<>();

        if (user.getPhoneNumber() != null) {
            attributes.put("phoneNumber", List.of(user.getPhoneNumber()));
        }
        if (user.getProfileImg() != null) {
            attributes.put("profileImg", List.of(user.getProfileImg()));
        }
        if (user.getName() != null) {
            attributes.put("name", List.of(user.getName()));
        }

        if (!attributes.isEmpty()) {
            payload.put("attributes", attributes);
        }

        return payload;
    }

    /**
     * Назначение роли USER пользователю.
     */
    public void assignUserRole(String userId) {
        String token = getAdminToken();

        String rolesUrl = String.format("%s/admin/realms/%s/roles", keycloakUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> rolesRequest = new HttpEntity<>(headers);
        ResponseEntity<List> rolesResponse = restTemplate.exchange(rolesUrl, HttpMethod.GET, rolesRequest, List.class);

        if (!rolesResponse.getStatusCode().is2xxSuccessful() || rolesResponse.getBody() == null) {
            throw new KeycloakClientException("Не удалось получить роли из Keycloak");
        }

        List<Map<String, Object>> roles = rolesResponse.getBody();

        Map<String, Object> userRole = roles.stream()
                .filter(role -> "USER".equals(role.get("name")))
                .findFirst()
                .orElseThrow(() -> new KeycloakClientException("Роль USER не найдена в Keycloak realm"));

        String assignRoleUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakUrl, realm, userId);

        HttpEntity<List<Map<String, Object>>> assignRequest = new HttpEntity<>(List.of(userRole), headers);

        try {
            restTemplate.postForEntity(assignRoleUrl, assignRequest, Void.class);
            logger.info("Назначена роль USER пользователю с id {}", userId);
        } catch (Exception e) {
            logger.error("Ошибка при назначении роли USER", e);
            throw new KeycloakClientException("Ошибка при назначении роли USER: " + e.getMessage(), e);
        }
    }

    /**
     * Получение ролей пользователя.
     */
    public List<String> getUserRoles(String userId) {
        String token = getAdminToken();

        String url = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakUrl, realm, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new KeycloakClientException("Не удалось получить роли пользователя " + userId);
        }

        List<Map<String, Object>> roles = response.getBody();

        return roles.stream()
                .map(role -> (String) role.get("name"))
                .toList();
    }

    // Класс для собственных исключений клиента Keycloak
    public static class KeycloakClientException extends RuntimeException {
        public KeycloakClientException(String message) {
            super(message);
        }

        public KeycloakClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
