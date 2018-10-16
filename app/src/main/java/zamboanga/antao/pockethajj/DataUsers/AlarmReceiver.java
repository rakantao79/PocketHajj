package zamboanga.antao.pockethajj.DataUsers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;


import zamboanga.antao.pockethajj.NavigationActivity;
import zamboanga.antao.pockethajj.R;

/**
 * Created by abdulrahmanantao on 01/09/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {


    private static final int MY_NOTIFICATION_ID = 4;
    NotificationManager nfm;
    Notification ntf;


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm Recieved", Toast.LENGTH_LONG).show();

        Intent myIntent = new Intent(context, NavigationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION );

        ntf = new NotificationCompat.Builder(context)
                .setContentTitle("Your alarm Worked")
                .setContentText("some details")
                .setTicker("Notification")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND) //PLAY DEFAULT SOUND
                .setAutoCancel(true) // REMOVE ALARM NOTIFICATION JUST BY SWIPE
                .setSmallIcon(R.mipmap.ic_launcher) //SHOWED IN STATUS BAR
                .build();

        nfm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nfm.notify(MY_NOTIFICATION_ID, ntf);

    }
}
