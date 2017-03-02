package cn.lingban.controller;

import cn.lingban.datacenter.CreateData;
import cn.lingban.websocket.SystemWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by xukai on 2017/2/28.
 * http://www.open-open.com/lib/view/open1436275663161.html
 * https://my.oschina.net/110NotFound/blog/536975
 * http://www.concretepage.com/spring-4/spring-4-websocket-sockjs-stomp-tomcat-example#configuration
 */
@Controller
public class AdminController {
    @Bean
    public SystemWebSocketHandler systemWebSocketHandler(){
        return new SystemWebSocketHandler();
    }


    @Autowired
    private CreateData createData;

    @MessageMapping("/change-notice")
    @SendTo("/topic/notice")
    public String greeting(String value) {
        System.out.println(value);
        return value;
    }


    @RequestMapping("/create/data")
    @ResponseBody
    public String createData(){
        try {
            createData.pushData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 手动更新数据，测试使用
     * 线上实际业务，可以根据观察者模式或者采用定时采集需要发送的数据。
     * @param request
     * @return
     */
    @RequestMapping("/send/data")
    @ResponseBody
    public String auditing(HttpServletRequest request){
        try {
            TextMessage message = new TextMessage("test data");
            systemWebSocketHandler().sendAll(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
