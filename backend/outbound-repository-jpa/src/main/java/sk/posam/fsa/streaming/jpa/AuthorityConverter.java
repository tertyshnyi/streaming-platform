package sk.posam.fsa.streaming.jpa;

import jakarta.persistence.AttributeConverter;
import sk.posam.fsa.streaming.domain.models.enums.Authority;

public class AuthorityConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority code) {
        if (code == null) {
            return null;
        }
        return code.name();
    }

    @Override
    public Authority convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        try {
            return Authority.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid value for enum UserRole: " + code);
        }
    }
}
