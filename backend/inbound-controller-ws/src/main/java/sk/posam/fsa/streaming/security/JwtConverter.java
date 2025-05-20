package sk.posam.fsa.streaming.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.posam.fsa.streaming.rest.dto.UserDto;

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

        userDto.setId(UUID.fromString(source.getClaimAsString("sub")));
        userDto.setPhoneNumber(source.getClaimAsString("phoneNumber"));
        userDto.setProfileImg(source.getClaimAsString("profileImg"));
        userDto.setEmail(source.getClaimAsString("email"));
        userDto.setName(source.getClaimAsString("name"));

        List<String> roles = extractAuthorities();
        userDto.setAuthorities(String.join(",", roles));

        System.out.println("Extracted User Data from JWT: " + userDto);

        if (userDto.getPhoneNumber() == null || userDto.getEmail() == null || userDto.getName() == null || userDto.getId() == null) {
            throw new IllegalStateException("Missing expected claims in JWT.");
        }

        return userDto;
    }

    private List<String> extractAuthorities() {
        Object claim = source.getClaim("authorities");

        if (claim instanceof List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }

        // В случае если роли одной строкой, например "ROLE_USER,ROLE_ADMIN"
        if (claim instanceof String str) {
            return Arrays.stream(str.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }

        return Collections.emptyList();
    }
}
