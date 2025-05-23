package sk.posam.fsa.streaming.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.rest.dto.EpisodeDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EpisodeMapper {

    private final SeriesMapper seriesMapper;
    private final VideoMapper videoMapper;

    public EpisodeMapper(@Lazy SeriesMapper seriesMapper, VideoMapper videoMapper) {
        this.seriesMapper = seriesMapper;
        this.videoMapper = videoMapper;
    }

    public EpisodeDto toDto(Episode entity) {
        if (entity == null) return null;

        EpisodeDto dto = new EpisodeDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDuration(entity.getDuration());
        if (entity.getSeries() != null) {
            dto.setSeriesId(entity.getSeries().getId().intValue());
        }

        if (entity.getVideos() != null) {
            dto.setVideos(entity.getVideos().stream()
                    .map(videoMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Episode toEntity(EpisodeDto dto, Series series) {
        if (dto == null) return null;

        Episode entity = new Episode();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDuration(dto.getDuration());
        entity.setSeries(series);

        if (dto.getVideos() != null) {
            List<Video> videos = dto.getVideos().stream()
                    .map(videoDto -> videoMapper.toEntity(videoDto, entity, null))
                    .collect(Collectors.toList());
            entity.setVideos(videos);
        }

        return entity;
    }
}
