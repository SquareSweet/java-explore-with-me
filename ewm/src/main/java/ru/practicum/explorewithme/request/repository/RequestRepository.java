package ru.practicum.explorewithme.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    @Query("update Request r " +
            "set r.status = 'REJECTED' " +
            "where r.event.id = :eventId " +
            "and r.status = 'PENDING'")
    void rejectAllPendingRequests(Long eventId);
}
