package sk.posam.fsa.streaming.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.posam.fsa.streaming.rest.dto.UserDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class JwtConverter extends AbstractAuthenticationToken {
    private final Jwt source;

    public JwtConverter(Jwt source) {
        super(extractGrantedAuthorities(source));
        this.source = Objects.requireNonNull(source);
        setAuthenticated(true);
    }

    private static Collection<? extends GrantedAuthority> extractGrantedAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> rolesList) {
                return rolesList.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .map(role -> "ROLE_" + role.toUpperCase(Locale.ROOT))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }

    @Override
    public Object getCredentials() {
        return Collections.emptyList();
    }

    @Override
    public Object getPrincipal() {
        UserDto userDto = new UserDto();

        String keycloakIdStr = source.getClaimAsString("sub");
        if (keycloakIdStr != null) {
            userDto.setKeycloakId(UUID.fromString(keycloakIdStr));
        }
        userDto.setEmail(source.getClaimAsString("email"));
        userDto.setName(source.getClaimAsString("name"));
        userDto.setPhoneNumber(source.getClaimAsString("phoneNumber"));
        userDto.setProfileImg(source.getClaimAsString("profileImg"));

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
        userDto.setAuthorities(roles);

        if (userDto.getPhoneNumber() == null || userDto.getEmail() == null || userDto.getName() == null
                || userDto.getKeycloakId() == null) {
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
