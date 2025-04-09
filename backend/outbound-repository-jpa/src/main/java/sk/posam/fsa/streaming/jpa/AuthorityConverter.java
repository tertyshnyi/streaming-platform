package sk.posam.fsa.streaming.jpa;

import jakarta.persistence.AttributeConverter;
import sk.posam.fsa.streaming.domain.models.enums.Authority;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorityConverter implements AttributeConverter<Set<Authority>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Authority> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public Set<Authority> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return Collections.emptySet();
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(Authority::valueOf)
                .collect(Collectors.toSet());
    }
}
