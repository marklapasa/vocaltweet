package net.lapasa.vocaltweet.fragments.playback;

import android.content.Context;
import android.util.AttributeSet;

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

/**
 * Created by mlapasa on 3/9/2015.
 */
public class CustomHoloCircularProgressBar extends HoloCircularProgressBar
{

    public CustomHoloCircularProgressBar(Context context)
    {
        super(context);
        init();
    }


    public CustomHoloCircularProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CustomHoloCircularProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        setThumbEnabled(false);
        setMarkerEnabled(false);
        setWheelSize(20);
    }

}
