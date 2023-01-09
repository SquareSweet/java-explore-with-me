package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(
            List<Long> users, List<EventStatus> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest
    );

    @Query("select e from Event e " +
            "where e.category.id in (:categories) " +
            "and e.paid = :paid " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and (lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text))")
    List<Event> findAllByParams(String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e " +
            "where e.category.id in (:categories) " +
            "and e.paid = :paid " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and (lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text)) " +
            "and (e.participantLimit - e.confirmedRequests > 0)")
    List<Event> findAllByParamsAvailable(String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    List<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    Event findByIdAndState(Long eventId, EventStatus eventStatus);

    List<Event> findAllByCategoryId(Long categoryId);
}
