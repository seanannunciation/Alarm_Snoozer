package com.example.annse.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by annse on 13-11-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static Ringtone ringtone;
    //public static final int REQUEST_CODE=222;
    @Override
   public void onReceive(Context context, Intent intent) {

        //MediaPlayer mp = new MediaPlayer();

        Toast.makeText(context,"WAKE UP!!!" ,Toast.LENGTH_LONG).show();
        Uri alarmuri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmuri==null){
            alarmuri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        //mp.stop();
        //mp=new MediaPlayer();
        //mp.start();

        ringtone=RingtoneManager.getRingtone(context,alarmuri);
        ringtone.play();

    }
}
