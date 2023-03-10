package ru.practicum.explorewithme.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventFullDto toEventFullDto(Event event, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .paid(event.getPaid())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public EventShortDto toEventShortDto(Event event, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public Event toEvent(NewEventDto newEventDto, CategoryDto category, User initiator) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(categoryMapper.toCategory(category))
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .paid(newEventDto.getPaid())
                .state(EventStatus.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public Event toEvent(UpdateEventRequestDto updateEventRequestDto, CategoryDto category) {
        return Event.builder()
                .annotation(updateEventRequestDto.getAnnotation())
                .category(categoryMapper.toCategory(category))
                .description(updateEventRequestDto.getDescription())
                .eventDate(LocalDateTime.parse(updateEventRequestDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .participantLimit(updateEventRequestDto.getParticipantLimit())
                .requestModeration(updateEventRequestDto.getRequestModeration())
                .paid(updateEventRequestDto.getPaid())
                .state(EventStatus.PENDING)
                .title(updateEventRequestDto.getTitle())
                .build();
    }

    public Event toEvent(UpdateEventAdminDto updateEventAdminDto, CategoryDto category) {
        return Event.builder()
                .annotation(updateEventAdminDto.getAnnotation())
                .category(categoryMapper.toCategory(category))
                .description(updateEventAdminDto.getDescription())
                .eventDate(LocalDateTime.parse(updateEventAdminDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .participantLimit(updateEventAdminDto.getParticipantLimit())
                .requestModeration(updateEventAdminDto.getRequestModeration())
                .paid(updateEventAdminDto.getPaid())
                .title(updateEventAdminDto.getTitle())
                .build();
    }
}
