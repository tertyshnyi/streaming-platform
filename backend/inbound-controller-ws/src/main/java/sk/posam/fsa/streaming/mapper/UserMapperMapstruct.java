package sk.posam.fsa.streaming.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.rest.dto.CreateUserDto;
import sk.posam.fsa.streaming.rest.dto.UpdateUserDto;
import sk.posam.fsa.streaming.rest.dto.UserDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@Mapper(componentModel = "spring")
public interface UserMapperMapstruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(CreateUserDto createUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);

    UserDto toUserDto(User user);

    default UserDto toUserDtoWithLog(User user) {
        UserDto dto = toUserDto(user);
        System.out.println("Mapping User to UserDto, id = " + user.getId() + ", dto id = " + dto.getId());
        return dto;
    }


    User toUserEntity(UserDto dto);

    default OffsetDateTime map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(ZoneOffset.UTC);
    }

    default LocalDateTime map(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toLocalDateTime();
    }

    default String mapUserToName(User user) {
        return user != null ? user.getName() : null;
    }
}

