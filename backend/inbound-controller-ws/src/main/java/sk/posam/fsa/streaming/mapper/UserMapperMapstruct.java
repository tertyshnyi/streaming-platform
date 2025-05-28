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
import java.util.List;
import java.util.stream.Collectors;

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

    @Mapping(source = "authorities", target = "authorities")
    UserDto toUserDto(User user);

    @Mapping(source = "authorities", target = "authorities")
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

    default List<String> mapAuthorities(String rolesString) {
        if (rolesString == null || rolesString.isBlank()) return List.of();
        return List.of(rolesString.split(","));
    }

    default String mapAuthorities(List<String> rolesList) {
        if (rolesList == null || rolesList.isEmpty()) return "";
        return String.join(",", rolesList);
    }
}

