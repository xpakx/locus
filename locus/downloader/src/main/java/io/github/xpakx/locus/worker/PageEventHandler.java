package io.github.xpakx.locus.worker;

import io.github.xpakx.locus.clients.ContentPublisher;
import io.github.xpakx.locus.worker.event.PageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageEventHandler {
private WorkerService service;
private ContentPublisher publisher;

    @RabbitListener(queues = "${amqp.queue.pages}")
    void handleNewAccount(final PageEvent event) {
        try {
           String content = service.extractContent(event.url());
           publisher.sendContent(event.bookmarkId(), content);
        } catch (final Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
