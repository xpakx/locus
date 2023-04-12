package io.github.xpakx.locus.bookmark;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "tag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "owner"})
)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String owner;

    @ManyToMany(mappedBy = "tags")
    private Set<Bookmark> bookmarks;
}