package io.github.xpakx.locus.worker;

import io.github.xpakx.locus.downloader.WebpageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final List<WebpageDownloader> downloaders;

    // TODO: should be called on new msg from broker and resend result on completion
    public String extractContent(String url) {
        return downloaders.stream()
                .filter((a) -> a.isApplicable(url))
                .map((a) -> a.getContentForUrl(url))
                .findFirst()
                .orElse("");
    }
}
