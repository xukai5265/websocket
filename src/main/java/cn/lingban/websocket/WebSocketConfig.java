package cn.lingban.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by xukai on 2017/2/27.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    public WebSocketConfig() {
    }

    /**
     * 注册消息连接节点
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 用来注册websocket server 实现类，第二个参数是访问websocket 的地址
        registry.addHandler(systemWebSocketHandler(), "/websocket").addInterceptors(new HandshakeInterceptor());
        System.out.println("registed!");
        //这个是使用Sockjs的注册方法
        registry.addHandler(systemWebSocketHandler(), "/sockjs/websocket/info").addInterceptors(new HandshakeInterceptor())
                .withSockJS();

        registry.addHandler(systemWebSocketHandler(), "/call/details/{abc}").addInterceptors(new HandshakeInterceptor())
                .withSockJS();
    }


    @Bean
    public WebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }
}
