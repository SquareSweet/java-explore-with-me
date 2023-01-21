package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.mdoel.EndpointHit;
import ru.practicum.explorewithme.mdoel.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.explorewithme.mdoel.ViewStats(hit.app, hit.uri, count(hit.id)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.uri IN (:uris) " +
            "AND hit.timestamp BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<ViewStats> countAllHits(
            @Param(value = "start") LocalDateTime start,
            @Param(value = "end") LocalDateTime end,
            @Param(value = "uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.explorewithme.mdoel.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.uri IN (:uris) " +
            "AND hit.timestamp BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<ViewStats> countUniqueHits(
            @Param(value = "start") LocalDateTime start,
            @Param(value = "end") LocalDateTime end,
            @Param(value = "uris") List<String> uris
    );
}
