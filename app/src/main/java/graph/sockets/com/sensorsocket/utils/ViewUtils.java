package graph.sockets.com.sensorsocket.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * Created by sumandas on 26/03/2017.
 */

public class ViewUtils {

    public static void setTint(Context context, ImageView imageView ,int setColor) {
        imageView.setColorFilter(ContextCompat.getColor(context,  setColor));

    }
}
