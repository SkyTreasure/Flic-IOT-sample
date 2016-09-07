package com.io.myapplication.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.io.myapplication.R;
import com.io.myapplication.constants.ApplicationConstants;
import com.io.myapplication.constants.Config;
import com.io.myapplication.ui.activity.BaseActivity;
import com.io.myapplication.ui.activity.MainActivity;
import com.io.myapplication.ui.activity.WebViewActivity;

import java.util.Date;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

/**
 * Created by akash on 6/9/16.
 */
public class TapBroadcastReceiver  extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        Config.setFlicCredentials();
    }

    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        if (isUp) {
            // Code for button up event here
        } else {
            // Code for button down event here
        }

        if (isDown) {
           /* Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Button was pressed")
                    .setContentText("Pressed last time at " + new Date())
                    .build();
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);*/
        }
    }



    @Override
    public void onButtonSingleOrDoubleClickOrHold(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold) {
      //  super.onButtonSingleOrDoubleClickOrHold(context, button, wasQueued, timeDiff, isSingleClick, isDoubleClick, isHold);
        SharedPreferences prefs =context.getSharedPreferences(ApplicationConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        if (prefs.getString(ApplicationConstants.IS_FIRST_TIME,"").equalsIgnoreCase(ApplicationConstants.YES)) {
            if(isSingleClick){
                //Open our app web view
                Intent i=new Intent(context, WebViewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            if(isDoubleClick){
                if(prefs.getString("song","").equals("") ){
                    Toast.makeText(context,"No song selected",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }else{
                    playMedia(Uri.parse(prefs.getString("song","")),context);
                }


            }
            if(isHold){
                //Open website
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + prefs.getString("phone","+919999999999")));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        }

    }



    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        // Button was removed
    }

    public void playMedia(Uri file,Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
