package sk.posam.fsa.streaming.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.rest.dto.CreateUserDto;

@Component
@Mapper(componentModel = "spring")
public interface UserMapperMapstruct {
    User toUser(CreateUserDto createUserDto);
}
