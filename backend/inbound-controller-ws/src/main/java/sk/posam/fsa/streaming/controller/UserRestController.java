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
import sk.posam.fsa.streaming.rest.dto.UserDto;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class UserRestController implements UsersApi {

    private final UserFacade userFacade;
    private final UserMapper userMapper;
    private final UserMapperMapstruct userMapperMapstruct;

    public UserRestController(UserFacade userFacade, UserMapper userMapper, UserMapperMapstruct userMapperMapstruct) {
        this.userFacade = userFacade;
        this.userMapper = userMapper;
        this.userMapperMapstruct = userMapperMapstruct;
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
        Optional<User> user = userFacade.get(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(userMapper.toUserDto(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        try {
            userFacade.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new NoSuchElementException("User with id " + id + " not found.");
        }
    }
}
