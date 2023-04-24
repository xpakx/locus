package io.github.xpakx.locus.annotation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HighlightRepositoryImpl implements HighlightRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Map<String, List<Highlight>> getNewestAnnotationsForUrls(List<String> urls, String username) {

            Query query = this.entityManager.createNativeQuery(
                    "SELECT c.id AS id, c.text AS text, c.annotation AS annotation, " +
                            "c.timestamp AS timestamp, c.type AS type, " +
                            "c.owner AS owner, c.url AS url, c.created_at AS created_at " +
                            "FROM highlight c " +
                            "WHERE c.id IN (SELECT c2.id FROM highlight c2 WHERE c2.url = c.url ORDER BY c2.created_at DESC LIMIT 2) " +
                            "AND c.url IN ?1 " +
                            "AND c.owner = ?2 " +
                            "ORDER BY c.created_at DESC"
            );
            query.setParameter(1, urls);
            query.setParameter(2, username);

            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(this::mapToHighlight)
                    .collect(Collectors.groupingBy(Highlight::getUrl));

    }

    private Highlight mapToHighlight(Object[] object) {
        Highlight highlight = new Highlight();
        highlight.setId(((BigInteger) object[0]).longValue());
        highlight.setText((String) object[1]);
        highlight.setAnnotation((String) object[2]);
        highlight.setTimestamp((Integer) object[3]);
        highlight.setType(HighlightType.valueOf((String) object[4]));
        highlight.setOwner((String) object[5]);
        highlight.setUrl((String) object[6]);
        highlight.setCreatedAt(((java.sql.Timestamp) object[7]).toLocalDateTime());
        return highlight;
    }
}
