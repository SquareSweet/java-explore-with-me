package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(
            List<Long> users, List<EventStatus> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest
    );

    @Query("select e from Event e " +
            "where e.state = :state " +
            "and (:categories is null or e.category.id in (:categories)) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeEnd as date) is null and e.eventDate >= :rangeStart " +
            "or e.eventDate between :rangeStart and :rangeEnd) " +
            "and (:text is null or lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text))")
    List<Event> findAllByParams(String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, EventStatus state, PageRequest pageRequest);

    @Query("select e from Event e " +
            "where e.state = :state " +
            "and (:categories is null or e.category.id in (:categories)) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeEnd as date) is null and e.eventDate >= :rangeStart " +
            "or e.eventDate between :rangeStart and :rangeEnd) " +
            "and (:text is null or lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text)) " +
            "and (e.participantLimit - e.confirmedRequests > 0)")
    List<Event> findAllByParamsAvailable(String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, EventStatus state, PageRequest pageRequest);

    List<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    Optional<Event> findAllByIdAndState(Long eventId, EventStatus eventStatus);

    Long countAllByCategoryId(Long categoryId);
}
