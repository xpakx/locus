package io.github.xpakx.locus.downloader.error;

public class DownloadException extends RuntimeException {
    public DownloadException(String message) {
        super(message);
    }

    public DownloadException() {
        super("Downloading failed!");
    }
}