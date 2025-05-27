package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesFacade extends MediaContentFacade<Series> {
    Series addEpisode(Long seriesId, Episode episode);

    void removeEpisode(Long seriesId, Long episodeId);

    Series updateEpisode(Long seriesId, Long episodeId, Episode updatedEpisode);

    Optional<Episode> getEpisodeById(Long seriesId, Long episodeId);

    List<Episode> getAllEpisodes(Long seriesId);
    List<String> getDistinctCountries();
    List<Integer> getDistinctReleaseYears();
    void recalculateEpisodeStats(Series series);
}
