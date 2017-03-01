package cn.lingban.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
// http://www.open-open.com/lib/view/open1408453906131.html
//http://www.xdemo.org/spring-websocket-comet/

/**
 * Created by xukai on 2017/2/27.
 */
@Component
public class SystemWebSocketHandler implements WebSocketHandler {
    private static final ArrayList<WebSocketSession> users;

    static {
        users = new ArrayList<>();
    }

    /**
     * 建立连接后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");

        Map map = session.getAttributes();
        users.add(session);
        System.out.println("session數："+users.size());
        session.sendMessage(new TextMessage("Server:connected OK!"));
    }

    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        String text = (String)wsm.getPayload();
        System.out.println("received at server message:"+text);
        TextMessage returnMessage = new TextMessage(wsm.getPayload()
                + " received at server");
        wss.sendMessage(returnMessage);
    }

    /**
     * 消息传输错误处理
     */
    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            wss.close();
        }
        System.out.println("websocket connection closed......");
    }

    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("websocket connection closed......");
        users.remove(wss);
        System.out.println("关闭连接后session数："+users.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有的连接发送消息
     * @param message
     * @throws IOException
     */
    public void sendAll(TextMessage message) throws IOException {
        for(WebSocketSession session :users){
            if(session.isOpen()){
                session.sendMessage(message);
            }
        }
    }
}
