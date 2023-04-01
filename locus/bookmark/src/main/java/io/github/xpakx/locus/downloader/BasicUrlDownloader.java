package io.github.xpakx.locus.downloader;

import io.github.xpakx.locus.downloader.error.DownloadException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Order(999) // make sure that's last downloader
public class BasicUrlDownloader implements WebpageDownloader {
    private final UrlReaderService urlReader;
    @Override
    public boolean isApplicable(String url) {
        return true;
    }

    @Override
    public String getContentForUrl(String url) {
        try {
            return urlReader.read(new URL(url));
        } catch(MalformedURLException ex) {
            throw new DownloadException("Malformed url!");
        } catch(IOException ex) {
            throw new DownloadException("Error while loading data from url!");
        }
    }
}
