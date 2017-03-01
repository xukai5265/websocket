package cn.lingban.datacenter;

import cn.lingban.watchmode.Watched;
import cn.lingban.watchmode.WatchedImpl;
import cn.lingban.watchmode.Watcher;
import cn.lingban.watchmode.WatcherImpl;
import org.springframework.stereotype.Service;

/**
 * Created by xukai on 2017/3/1.
 */
@Service
public class CreateData {
    private static Watcher watcher = new WatcherImpl();
    private static Watched watched = new WatchedImpl();
    static {
        watched.addWatcher(watcher);
    }

    public void pushData() throws InterruptedException {
        for(int i=0;i<100;i++){
            Thread.sleep(2000l);
            watched.notifyWatcher("aaa");
        }
    }
}
