package kei.magnet.utils;

import android.os.Handler;

import java.util.concurrent.Callable;

/**
 * Created by carlo_000 on 15/12/2015.
 */
public class CallBack implements Callable{
    public final Handler handler = new Handler();
    public long time;
    public CallBack(long time){
        this.time = time;
    }
    public Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try{
                //do your code here
                //also call the same runnable
                call();
                handler.postDelayed(this, time);
            }
            catch (Exception e) {
                // TODO: handle exception
            }
            finally{
                //also call the same runnable
                handler.postDelayed(this, time);
            }
        }
    };

    @Override
    public Object call() throws Exception {

        return null;
    }
}

