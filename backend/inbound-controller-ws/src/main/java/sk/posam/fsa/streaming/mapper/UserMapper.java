package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.models.enums.Authority;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.rest.dto.AuthorityDto;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toUserDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfileImg(entity.getProfileImg());
        dto.setAuthorities(entity.getAuthorities().stream()
                .map(authority -> AuthorityDto.valueOf(authority.name()))
                .collect(Collectors.toList()));
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        return dto;
    }

    public User toUserEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User entity = new User();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfileImg(dto.getProfileImg());
        entity.setAuthorities(dto.getAuthorities().stream()
                .map(authorityDto -> Authority.valueOf(authorityDto.name()))
                .collect(Collectors.toSet()));
        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt().toLocalDateTime());
        }

        return entity;
    }

}
