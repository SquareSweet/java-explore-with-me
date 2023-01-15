package ru.practicum.explorewithme.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"category", "initiator", "location"})
@ToString(exclude = {"category", "initiator", "location"})
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;
    @Column(name = "confirmed_requests")
    Long confirmedRequests;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    User initiator;
    @Embedded
    Location location;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    Boolean paid;
    @Enumerated(EnumType.STRING)
    EventStatus state;
    String title;
    Long views;
}
