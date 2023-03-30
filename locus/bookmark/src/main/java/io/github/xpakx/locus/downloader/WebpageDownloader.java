package io.github.xpakx.locus.downloader;

public interface WebpageDownloader {
    boolean isApplicable(String url);
    String getContentForUrl(String url);
}
