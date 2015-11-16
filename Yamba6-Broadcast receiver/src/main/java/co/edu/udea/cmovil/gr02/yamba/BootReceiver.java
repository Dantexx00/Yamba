package co.edu.udea.cmovil.gr02.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = BootReceiver.class.getSimpleName();
    public static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long interval=DEFAULT_INTERVAL/45;
        PendingIntent operation=PendingIntent.getService(context,-1,new Intent(context,
                RefreshService.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,System.currentTimeMillis(),interval,operation);
        Log.d(TAG,Long.toString(interval));
    }
}
