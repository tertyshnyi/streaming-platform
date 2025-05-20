package sk.posam.fsa.streaming.domain.models.enums;

public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science Fiction"),
    SPORT("Sport"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western"),
    MUSICAL("Musical"),
    SUPERHERO("Superhero"),
    SHORT("Short"),
    REALITY("Reality"),
    TALK_SHOW("Talk Show"),
    GAME_SHOW("Game Show"),
    NEWS("News"),
    NOIR("Noir"),
    EXPERIMENTAL("Experimental");

    private final String label;

    Genre(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

