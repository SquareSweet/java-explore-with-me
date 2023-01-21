package ru.practicum.explorewithme.comment.service;

import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(NewCommentDto newCommentDto, Long userId);

    CommentDto update(UpdateCommentDto updateCommentDto, Long userId, Long commentId);

    CommentDto updateAdmin(UpdateCommentDto updateCommentDto, Long commentId);

    CommentDto getById(Long commentId);

    List<CommentDto> getByEventId(Long eventId, int from, int size);

    List<CommentDto> getByUserId(Long userId, int from, int size);

    void deleteById(Long userId, Long commentId);

    void deleteByIdAdmin(Long commentId);

}
