package io.github.xpakx.locus.annotation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "highlight")
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String text;
    private String annotation;
    private Integer timestamp;
    private HighlightType type;
    private String owner;
    private String url;
    private LocalDateTime createdAt;
}