package ca.uqac.magnet.something;

import java.util.ArrayList;

/**
 * Created by carlo_000 on 22/10/2015.
 */
public abstract class MathUtils {
    //using first order filtering
    public static float[] lowPassFilter(float[] x,float[] y,float alpha){
        if ( y == null ) return x;
        float[] output = new float[x.length];

        int t = 1;
        for (int i = 0; i < x.length ; i++) {
            output[i] = y[i]+ alpha*(x[i]-y[i]);
        }
        return output;
    }
}
