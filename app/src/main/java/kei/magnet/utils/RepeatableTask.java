package kei.magnet.utils;

import android.os.Handler;

import java.util.concurrent.Callable;

/**
 * Created by carlo_000 on 15/12/2015.
 */
public class RepeatableTask implements Runnable{
    final Handler handler = new Handler();
    private long delay;
    private Callable callback;
    public RepeatableTask(Callable callback,long delay) {
        this.delay=delay;
        this.callback = callback;
    }
    @Override
    public void run() {
        try{
            //do your code here
            //also call the same runnable
            callback.call();
            handler.postDelayed(this, delay);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        finally{
            //also call the same runnable
            handler.postDelayed(this, delay);
        }
    }
}
