package net.jayanth

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

/**
 * Created by jmp on 6/20/2017.
 */
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration : AbstractWebSocketMessageBrokerConfigurer(){
    override fun configureMessageBroker(registry: MessageBrokerRegistry?) {
        registry?.enableSimpleBroker("/topic")
        registry?.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        registry?.addEndpoint("/imageMessages")?.withSockJS()
    }
}