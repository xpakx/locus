package io.github.xpakx.locus.downloader.settings;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    private final String contentTopic;

    public AMQPConfig(@Value("${amqp.exchange.content}") final String contentTopic) {
        this.contentTopic = contentTopic;
    }

    @Bean
    public TopicExchange contentTopicExchange() {
        return ExchangeBuilder
                .topicExchange(contentTopic)
                .durable(true)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange pagesTopicExchange(@Value("${amqp.exchange.pages}") final String exchangeName) {
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue pagesQueue(@Value("${amqp.queue.pages}") final String queueName) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    @Bean
    public Binding accountsBinding(final Queue pagesQueue, final TopicExchange pagesTopicExchange) {
        return BindingBuilder.bind(pagesQueue)
                .to(pagesTopicExchange)
                .with("page");
    }
}