package io.github.xpakx.locus.downloader.clients;

import io.github.xpakx.locus.downloader.clients.event.ContentEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContentPublisher {
    private final AmqpTemplate template;
    private final String contentTopic;

    public ContentPublisher(AmqpTemplate template, @Value("${amqp.exchange.content}") String contentTopic) {
        this.template = template;
        this.contentTopic = contentTopic;
    }

    public void sendContent(Long bookmarkId, String content) {
        ContentEvent event = new ContentEvent(bookmarkId, content);
        template.convertAndSend(contentTopic, "content", event);
    }
}
