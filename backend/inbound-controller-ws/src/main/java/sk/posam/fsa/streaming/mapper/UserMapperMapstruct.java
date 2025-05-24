package sk.posam.fsa.streaming.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.rest.dto.CreateUserDto;
import sk.posam.fsa.streaming.rest.dto.UpdateUserDto;

@Component
@Mapper(componentModel = "spring")
public interface UserMapperMapstruct {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(CreateUserDto createUserDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUser(UpdateUserDto updateUserDto);
}
