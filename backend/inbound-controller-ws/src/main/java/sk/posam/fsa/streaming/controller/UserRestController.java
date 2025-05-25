package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.mapper.UserMapperMapstruct;
import sk.posam.fsa.streaming.rest.api.UsersApi;
import sk.posam.fsa.streaming.rest.dto.CreateUserDto;
import sk.posam.fsa.streaming.rest.dto.UpdateUserDto;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

import java.util.Optional;

@RestController
public class UserRestController implements UsersApi {

    private final UserFacade userFacade;
    private final UserMapperMapstruct userMapperMapstruct;
    private final CurrentUserDetailService currentUserDetailService;

    public UserRestController(UserFacade userFacade,
                              UserMapperMapstruct userMapperMapstruct,
                              CurrentUserDetailService currentUserDetailService) {
        this.userFacade = userFacade;
        this.userMapperMapstruct = userMapperMapstruct;
        this.currentUserDetailService = currentUserDetailService;
    }

    @Override
    public ResponseEntity<UserDto> createUser(CreateUserDto dto) {
        User user = userMapperMapstruct.toUser(dto);
        User created = userFacade.createWithKeycloak(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapperMapstruct.toUserDto(created));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserDto dto) {
        User existingUser = userFacade.get(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapperMapstruct.updateUserFromDto(dto, existingUser);

        User updated = userFacade.updateWithKeycloak(existingUser);
        return ResponseEntity.ok(userMapperMapstruct.toUserDto(updated));
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        try {
            UserDto userFromToken = currentUserDetailService.getCurrentUser();
            if (userFromToken == null || userFromToken.getKeycloakId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Optional<User> userFromDbOpt = userFacade.findByKeycloakId(userFromToken.getKeycloakId());

            if (userFromDbOpt.isPresent()) {
                UserDto userFromDb = userMapperMapstruct.toUserDto(userFromDbOpt.get());
                return ResponseEntity.ok(userFromDb);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
