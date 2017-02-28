package cn.lingban.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by xukai on 2017/2/27.
 */
@Component
public class InfoSocketEndPoint extends TextWebSocketHandler {
    public InfoSocketEndPoint() {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        //获取提交过来的消息
        String text = message.getPayload();
        System.out.println("handMessage:" + text);
        super.handleTextMessage(session, message);
        TextMessage returnMessage = new TextMessage(message.getPayload()
                + " received at server");
        session.sendMessage(returnMessage);
    }
}
