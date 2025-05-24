package sk.posam.fsa.streaming.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.posam.fsa.streaming.rest.dto.UserDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class JwtConverter extends AbstractAuthenticationToken {
    private final Jwt source;

    public JwtConverter(Jwt source) {
        super(Collections.emptyList());
        this.source = Objects.requireNonNull(source);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return Collections.emptyList();
    }

    @Override
    public Object getPrincipal() {
        UserDto userDto = new UserDto();

        userDto.setKeycloakId(source.getClaimAsString("sub"));
        userDto.setPhoneNumber(source.getClaimAsString("phoneNumber"));
        userDto.setProfileImg(source.getClaimAsString("profileImg"));
        userDto.setEmail(source.getClaimAsString("email"));

        Map<String, Object> userInfo = source.getClaim("user_info");
        if (userInfo != null) {
            Object nameObj = userInfo.get("name");
            if (nameObj instanceof String nameStr) {
                userDto.setName(nameStr);
            }
        }

        String createdAtStr = source.getClaimAsString("createdAt");
        if (createdAtStr != null) {
            userDto.setCreatedAt(OffsetDateTime.parse(createdAtStr));
        } else {
            Object iatObj = source.getClaim("iat");
            if (iatObj instanceof Number issuedAtSeconds) {
                Instant instant = Instant.ofEpochSecond(issuedAtSeconds.longValue());
                userDto.setCreatedAt(instant.atOffset(ZoneOffset.UTC));
            }
        }

        List<String> roles = extractAuthorities();
        userDto.setAuthorities(String.join(",", roles));

        System.out.println("Extracted User Data from JWT: " + userDto);

        if (userDto.getPhoneNumber() == null || userDto.getEmail() == null || userDto.getName() == null || userDto.getKeycloakId() == null) {
            throw new IllegalStateException("Missing expected claims in JWT.");
        }

        return userDto;
    }

    private List<String> extractAuthorities() {
        Map<String, Object> realmAccess = source.getClaim("realm_access");
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> rolesList) {
                return rolesList.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .toList();
            }
        }
        return Collections.emptyList();
    }
}
