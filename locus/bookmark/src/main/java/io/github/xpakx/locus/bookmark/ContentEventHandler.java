package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.event.ContentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentEventHandler {
    private final BookmarkService bookmarkService;
    @RabbitListener(queues = "${amqp.queue.content}")
    void handleNewAccount(final ContentEvent event) {
        try {
            bookmarkService.addContent(event.bookmarkId(), event.content());
        } catch (final Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
