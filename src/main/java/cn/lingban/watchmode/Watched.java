package cn.lingban.watchmode;

/**
 * Created by Administrator on 2016/8/20.
 * 被观察者
 */
public interface Watched {
    void addWatcher(Watcher watcher);
    void removeWatcher(Watcher watcher);
    void notifyWatcher(String taskId);
}
