package sk.posam.fsa.streaming.domain.models.enums;

public enum Authority {
    USER,
    RELEASER,
    MODERATOR,
    ADMIN;

    @Override
    public String toString() {
        return name();
    }
}
