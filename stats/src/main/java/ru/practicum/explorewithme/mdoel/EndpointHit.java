package ru.practicum.explorewithme.mdoel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    @Column(length = 200)
    String app;
    @NotBlank
    @Column(length = 200)
    String uri;
    @NotBlank
    @Column(length = 15)
    String ip;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "hit_time")
    LocalDateTime timestamp;
}
