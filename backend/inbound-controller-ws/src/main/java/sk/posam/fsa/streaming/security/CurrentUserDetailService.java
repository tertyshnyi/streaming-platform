package sk.posam.fsa.streaming.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.posam.fsa.streaming.rest.dto.UserDto;

@Service
public class CurrentUserDetailService {

    public UserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDto) {
            return (UserDto) principal;
        }

        throw new IllegalStateException("Current user is not of type UserDto.");
    }
}
