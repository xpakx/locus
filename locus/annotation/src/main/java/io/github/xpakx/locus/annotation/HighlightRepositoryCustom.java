package io.github.xpakx.locus.annotation;

import java.util.List;
import java.util.Map;

public interface HighlightRepositoryCustom {
    Map<String, List<Highlight>> getNewestAnnotationsForUrls(List<String> urls, String username);
}
