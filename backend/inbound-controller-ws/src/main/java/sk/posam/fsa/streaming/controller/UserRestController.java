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
import sk.posam.fsa.streaming.rest.dto.LoginRequestDto;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

import java.util.NoSuchElementException;
import java.util.Optional;

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
    public ResponseEntity<UserDto> createUser(CreateUserDto createUserDto) {
        User user = userMapperMapstruct.toUser(createUserDto);
        User savedUser = userFacade.create(user);
        UserDto userDto = userMapper.toUserDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        UserDto currentUser = currentUserDetailService.getCurrentUser();

        try {
            Optional<User> user = userFacade.get(id, currentUser.getId());
            return user.map(value -> ResponseEntity.ok(userMapper.toUserDto(value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        UserDto currentUser = currentUserDetailService.getCurrentUser();

        try {
            userFacade.delete(id, currentUser.getId());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<String> loginUser(LoginRequestDto loginRequestDto) {
        try {
            String jwtToken = userFacade.login(loginRequestDto.getEmailOrPhone(), loginRequestDto.getPassword());
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
