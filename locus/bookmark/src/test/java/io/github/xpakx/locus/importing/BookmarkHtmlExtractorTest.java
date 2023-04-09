package io.github.xpakx.locus.importing;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BookmarkHtmlExtractorTest {
    BookmarkHtmlExtractor extractor = new BookmarkHtmlExtractor();

    @Test
    void shouldExtractLinks() {
        String html = """
                <DT><A HREF="https://academic.oup.com/sleep/article/44/5/zsaa261/6104495" ADD_DATE="1661771385" >Sleep and mental health in athletes during COVID-19 lockdown | SLEEP | Oxford Academic</A>
                <DT><A HREF="https://www.frontiersin.org/articles/10.3389/fpsyg.2021.638252/full" ADD_DATE="1661771446">Frontiers | Prevalence of Shift Work Disorder: A Systematic Review and Meta-Analysis</A>
                <DT><A HREF="https://www.tandfonline.com/doi/abs/10.1080/07420528.2019.1661424" ADD_DATE="1661771467">Narrow-band ultraviolet B (NB UV-B) exposures improve mood in healthy individuals differently depending on chronotype: Chronobiology International: Vol 36, No 11</A>
                """;

        List<BookmarkRequest> result = extractor.extractBookmarks(html);

        org.hamcrest.MatcherAssert.assertThat(result, hasSize(3));
        assertThat(result)
                .extracting(BookmarkRequest::url)
                .contains(
                        "https://academic.oup.com/sleep/article/44/5/zsaa261/6104495",
                        "https://www.frontiersin.org/articles/10.3389/fpsyg.2021.638252/full",
                        "https://www.tandfonline.com/doi/abs/10.1080/07420528.2019.1661424"
                );
    }

}