package kei.magnet.utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Random;

import kei.magnet.R;

/**
 * Created by Dimitri on 14/12/2015.
 */
public class ImagesUtils {
    private static ImagesUtils instance = null;

    private HashMap<Integer, BitmapDescriptor> dictionnaryFriendImages = new HashMap<>();

    private ImagesUtils() {
        dictionnaryFriendImages.put(0, BitmapDescriptorFactory.fromResource(R.drawable.friend0));
        dictionnaryFriendImages.put(1, BitmapDescriptorFactory.fromResource(R.drawable.friend1));
        dictionnaryFriendImages.put(2, BitmapDescriptorFactory.fromResource(R.drawable.friend2));
        dictionnaryFriendImages.put(3, BitmapDescriptorFactory.fromResource(R.drawable.friend3));
        dictionnaryFriendImages.put(4, BitmapDescriptorFactory.fromResource(R.drawable.friend4));
        dictionnaryFriendImages.put(5, BitmapDescriptorFactory.fromResource(R.drawable.friend5));
    }

    public static ImagesUtils getInstance() {
        if(instance == null) {
            instance = new ImagesUtils();
        }
        return instance;
    }

    public BitmapDescriptor getRandomFriendImage() {
        Random random = new Random();
        return dictionnaryFriendImages.get(random.nextInt(dictionnaryFriendImages.size()));
    }
}
