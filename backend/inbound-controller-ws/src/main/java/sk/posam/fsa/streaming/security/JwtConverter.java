package sk.posam.fsa.streaming.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.rest.dto.AuthorityDto;

import java.util.*;
import java.util.stream.Collectors;

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

        // Извлекаем данные из JWT
        userDto.setId(UUID.fromString(source.getClaimAsString("sub")));  // Используем "sub" вместо "id"
        userDto.setPhoneNumber(source.getClaimAsString("phoneNumber"));
        userDto.setProfileImg(source.getClaimAsString("profileImg"));
        userDto.setEmail(source.getClaimAsString("email"));
        userDto.setName(source.getClaimAsString("name"));

        // Логирование для отладки
        System.out.println("Extracted User Data from JWT: " + userDto);

        // Проверим, что обязательные данные присутствуют
        if (userDto.getPhoneNumber() == null || userDto.getEmail() == null || userDto.getName() == null || userDto.getId() == null) {
            throw new IllegalStateException("Missing expected claims in JWT.");
        }

        // Преобразуем список ролей
        userDto.setAuthorities(extractAuthorities());

        return userDto;
    }


    private List<AuthorityDto> extractAuthorities() {
        List<String> roles = (List<String>) source.getClaim("authorities");
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(AuthorityDto::valueOf)
                .collect(Collectors.toList());
    }
}
