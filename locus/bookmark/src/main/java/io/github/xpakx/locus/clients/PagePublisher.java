package io.github.xpakx.locus.clients;

import io.github.xpakx.locus.clients.dto.PageEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PagePublisher {
    private final AmqpTemplate template;
    private final String pageTopic;

    public PagePublisher(AmqpTemplate template, @Value("${amqp.exchange.pages}") String pageTopic) {
        this.template = template;
        this.pageTopic = pageTopic;
    }

    public void sendPageToWorker(Long bookmarkId, String url) {
        PageEvent event = new PageEvent(bookmarkId, url);
        template.convertAndSend(pageTopic, "page", event);
    }
}
