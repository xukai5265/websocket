package cn.lingban.watchmode;


import cn.lingban.websocket.SystemWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * Created by Administrator on 2016/8/20.
 * 具体观察者角色
 */
@Service
public class WatcherImpl implements Watcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public SystemWebSocketHandler systemWebSocketHandler(){
        return new SystemWebSocketHandler();
    }
    @Override
    public void update(String taskId) {
        System.out.println("更新");

        //这里需要调用到web socket 来发送信息
        TextMessage message = new TextMessage(taskId);
        try {
            systemWebSocketHandler().sendAll(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}