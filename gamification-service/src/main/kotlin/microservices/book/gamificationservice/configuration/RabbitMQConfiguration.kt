package microservices.book.gamificationservice.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory

/**
 * Creates a queue and binds it to the exchange to receive messages filtered by the routing key.
 */
@Configuration
class RabbitMQConfiguration : RabbitListenerConfigurer {

    @Bean
    fun multiplicationExchange(@Value("\${multiplication.exchange}") exchangeName: String): TopicExchange =
            TopicExchange(exchangeName)

    @Bean
    fun queue(@Value("\${multiplication.queue}") queueName: String): Queue =
            Queue(queueName, true)

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange,
                @Value("\${multiplication.anything.routing-key}") routingKey: String): Binding =
            BindingBuilder.bind(queue).to(exchange).with(routingKey)

    @Bean
    fun messageHandlerMethodFactor(): DefaultMessageHandlerMethodFactory =
            DefaultMessageHandlerMethodFactory().apply {
                setMessageConverter(MappingJackson2MessageConverter())
            }

    override fun configureRabbitListeners(registrar: RabbitListenerEndpointRegistrar) {
        registrar.messageHandlerMethodFactory = messageHandlerMethodFactor()
    }
}
