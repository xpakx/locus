package io.github.xpakx.locus.bookmark;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bookmark")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String url;
    private LocalDate date;

    @Column(columnDefinition="TEXT")
    private String content;

    private String owner;
}