package io.github.xpakx.locus.downloader.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DownloadException extends RuntimeException {
    public DownloadException(String message) {
        super(message);
    }

    public DownloadException() {
        super("Downloading failed!");
    }
}