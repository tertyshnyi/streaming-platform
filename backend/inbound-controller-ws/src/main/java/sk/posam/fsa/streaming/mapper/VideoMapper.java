package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.rest.dto.VideoDto;

@Component
public class VideoMapper {

    public VideoDto toDto(Video entity) {
        if (entity == null) return null;

        return new VideoDto()
                .id(entity.getId())
                .resolution(entity.getResolution())
                .url(entity.getUrl());
    }

    public Video toEntity(VideoDto dto, Episode episode, Movie movie) {
        if (dto == null) return null;

        Video entity = new Video();
        entity.setId(dto.getId());
        entity.setResolution(dto.getResolution());
        entity.setUrl(dto.getUrl());

        if (episode != null) {
            entity.setEpisode(episode);
            entity.setMovie(null);
        } else if (movie != null) {
            entity.setMovie(movie);
            entity.setEpisode(null);
        }

        return entity;
    }
}
