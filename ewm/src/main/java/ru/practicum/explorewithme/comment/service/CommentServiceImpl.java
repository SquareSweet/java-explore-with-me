package ru.practicum.explorewithme.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.repository.CommentRepository;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.CommentNotFoundException;
import ru.practicum.explorewithme.common.exception.IllegalOperationException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper mapper;

    @Override
    public CommentDto create(NewCommentDto newCommentDto, Long userId) {
        User user = userService.getById(userId); //throws exception if user does not exist
        Event event = eventService.getByIdNotMapped(newCommentDto.getEvent()); //throws exception if event does not exist
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new IllegalOperationException("Event id: " + event.getId() + " is not published");
        }
        Comment comment = repository.save(mapper.toComment(newCommentDto, user, event));
        log.info("Created comment id: {}", comment.getId());
        return mapper.toCommentDto(comment);
    }

    @Override
    public CommentDto update(UpdateCommentDto updateCommentDto, Long userId, Long commentId) {
        userService.getById(userId); //throws exception if user does not exist
        Comment comment = repository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not the author of comment id: " + commentId);
        }
        comment.setText(updateCommentDto.getText());
        comment.setLastEditedOn(LocalDateTime.now());
        comment.setEditedByAdmin(false);
        repository.save(comment);
        log.info("Updated comment id: {}", comment.getId());
        return mapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateAdmin(UpdateCommentDto updateCommentDto, Long commentId) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        comment.setText(updateCommentDto.getText());
        comment.setLastEditedOn(LocalDateTime.now());
        comment.setEditedByAdmin(true);
        repository.save(comment);
        log.info("Updated comment id: {} by admin", comment.getId());
        return mapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        return mapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getByEventId(Long eventId, int from, int size) {
        eventService.getByIdNotMapped(eventId); //throws exception if event does not exist
        return repository.findAllByEventId(eventId, OffsetPageRequest.of(from, size, Sort.by("createdOn"))).stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getByUserId(Long userId, int from, int size) {
        userService.getById(userId); //throws exception if user does not exist
        return repository.findAllByAuthorId(userId, OffsetPageRequest.of(from, size, Sort.by("createdOn"))).stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long userId, Long commentId) {
        userService.getById(userId); //throws exception if user does not exist
        Comment comment = repository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not the author of comment id: " + commentId);
        }
        repository.deleteById(commentId);
        log.info("Deleted comment id: {}", commentId);
    }

    @Override
    public void deleteByIdAdmin(Long commentId) {
        repository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        repository.deleteById(commentId);
        log.info("Deleted comment id: {} by admin", commentId);
    }

}
