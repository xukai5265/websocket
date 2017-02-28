package cn.lingban.controller;

import cn.lingban.websocket.SystemWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by xukai on 2017/2/28.
 */
@Controller
public class AdminController {
    @Bean
    public SystemWebSocketHandler systemWebSocketHandler(){
        return new SystemWebSocketHandler();
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
            systemWebSocketHandler().sendAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
