package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.mapper.UserMapper;
import sk.posam.fsa.streaming.mapper.UserMapperMapstruct;
import sk.posam.fsa.streaming.rest.api.UsersApi;
import sk.posam.fsa.streaming.rest.dto.CreateUserDto;
import sk.posam.fsa.streaming.rest.dto.UpdateUserDto;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

import java.util.NoSuchElementException;

@RestController
public class UserRestController implements UsersApi {

    private final UserFacade userFacade;
    private final UserMapper userMapper;
    private final UserMapperMapstruct userMapperMapstruct;
    private final CurrentUserDetailService currentUserDetailService;

    public UserRestController(UserFacade userFacade, UserMapper userMapper, UserMapperMapstruct userMapperMapstruct,
                              CurrentUserDetailService currentUserDetailService) {
        this.userFacade = userFacade;
        this.userMapper = userMapper;
        this.userMapperMapstruct = userMapperMapstruct;
        this.currentUserDetailService = currentUserDetailService;
    }

    @Override
    public ResponseEntity<UserDto> createUser(CreateUserDto dto) {
        User user = userMapperMapstruct.toUser(dto);
        User created = userFacade.createWithKeycloak(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toUserDto(created));
    }

    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserDto dto) {
        User existingUser = userFacade.get(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhoneNumber(dto.getPhoneNumber());
        existingUser.setProfileImg(dto.getProfileImg());

        User updated = userFacade.updateWithKeycloak(existingUser);
        return ResponseEntity.ok(userMapper.toUserDto(updated));
    }


    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        try {
            UserDto userFromToken = currentUserDetailService.getCurrentUser();

            if (userFromToken != null && userFromToken.getId() != null) {
                System.out.println("Returning UserDto from token: " + userFromToken);
                return ResponseEntity.ok(userFromToken);
            } else {
                System.out.println("User in token is null or missing ID");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserDto());
            }

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserDto());
        }
    }

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {
        UserDto currentUser = currentUserDetailService.getCurrentUser();
        try {
            userFacade.delete(currentUser.getId());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
