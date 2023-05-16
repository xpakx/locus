package io.github.xpakx.locus.clients;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.elasticsearch.BookmarkData;
import io.github.xpakx.locus.elasticsearch.BookmarkESRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class WorkerAspect {
    private final PagePublisher pagePublisher;

    @AfterReturning(value="@annotation(GetContentFromWorker)", returning = "response")
    public void sendPage(BookmarkDto response) {
        pagePublisher.sendPageToWorker(response.id(), response.url());
    }
}
