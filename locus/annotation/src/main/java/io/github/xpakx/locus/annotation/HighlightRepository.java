package io.github.xpakx.locus.annotation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HighlightRepository extends JpaRepository<Highlight, Long>, HighlightRepositoryCustom {
    List<Highlight> findByUrlAndOwner(String url, String owner);
}