package cn.lingban.watchmode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 * 具体主题角色
 */
//@Service
public class WatchedImpl implements Watched {
    private List<Watcher> list = new LinkedList<Watcher>();
    @Override
    public void addWatcher(Watcher watcher) {
        list.add(watcher);
    }

    @Override
    public void removeWatcher(Watcher watcher) {
        list.remove(watcher);
    }

    @Override
    public void notifyWatcher(String taskId) {
        if(list.size()==0 || list==null)return;
        for(Watcher watcher : list){
            watcher.update(taskId);
        }
    }
}
