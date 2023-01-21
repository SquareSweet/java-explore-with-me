package ru.practicum.explorewithme.compilation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation as c " +
            "where c.pinned = :pinned")
    List<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
