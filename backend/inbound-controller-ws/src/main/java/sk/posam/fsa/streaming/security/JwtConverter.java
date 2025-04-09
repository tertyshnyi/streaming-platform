package sk.posam.fsa.streaming.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.rest.dto.AuthorityDto;
import sk.posam.fsa.streaming.domain.models.enums.Authority;

import java.util.*;
import java.util.stream.Collectors;

class JwtConverter extends AbstractAuthenticationToken {
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
        userDto.setEmail(source.getClaimAsString("email"));
        userDto.setName(source.getClaimAsString("name"));
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
