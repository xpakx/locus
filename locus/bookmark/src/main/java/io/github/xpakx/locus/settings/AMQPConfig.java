package io.github.xpakx.locus.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class AMQPConfig {
    private final String contentTopic;
    private final String pagesTopic;
    private final String contentQueue;

    public AMQPConfig(
            @Value("${amqp.exchange.content}") final String contentTopic,
            @Value("${amqp.exchange.pages}") final String pagesTopic,
            @Value("${amqp.queue.content}") final String contentQueue
    ) {
        this.contentTopic = contentTopic;
        this.pagesTopic = pagesTopic;
        this.contentQueue = contentQueue;
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
    public TopicExchange pagesTopicExchange() {
        return ExchangeBuilder
                .topicExchange(pagesTopic)
                .durable(true)
                .build();
    }

    @Bean
    public Queue contentQueue() {
        return QueueBuilder
                .durable(contentQueue)
                .build();
    }

    @Bean
    public Binding queueBinding(final Queue contentQueue, final TopicExchange contentTopicExchange) {
        return BindingBuilder.bind(contentQueue)
                .to(contentTopicExchange)
                .with("content");
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        final MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();
        jsonConverter.getObjectMapper().registerModule(
                new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(
            final MessageHandlerMethodFactory messageHandlerMethodFactory) {
        return (c) -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);


    }
}
