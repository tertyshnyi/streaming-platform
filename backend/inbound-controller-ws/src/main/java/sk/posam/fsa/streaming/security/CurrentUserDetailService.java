package sk.posam.fsa.streaming.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.rest.dto.UserDto;

@Service
public class CurrentUserDetailService {

    private final UserFacade userFacade;

    public CurrentUserDetailService(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public UserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDto) {
            return (UserDto) principal;
        }

        throw new IllegalStateException("Current user is not of type UserDto.");
    }

    public Long getUserId() {
        return getCurrentUser().getId();
    }

    public String getUserEmail() {
        return getCurrentUser().getEmail();
    }

    public String getUserName() {
        return getCurrentUser().getName();
    }

    public String getUserPhoneNumber() {
        return getCurrentUser().getPhoneNumber();
    }
}
