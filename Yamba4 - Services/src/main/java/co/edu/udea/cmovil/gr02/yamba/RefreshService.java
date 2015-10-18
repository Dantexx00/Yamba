package co.edu.udea.cmovil.gr02.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

public class RefreshService extends IntentService {
    public boolean isEmpty;
    private static final String TAG = RefreshService.class.getSimpleName();

    public RefreshService() {
        super("RefreshService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            Log.d(TAG, "onStarted");
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(this); //*
            final String username = prefs.getString("username", ""); //*
            final String password = prefs.getString("password", ""); //*

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                isEmpty = true;
                return;
            } //Verificar que no hayan campos vacíos

            YambaClient cloud = new YambaClient(username, password); /*Se crea un nuevo
    cliente yamba*/
            try {
                List<YambaStatus> timeline = cloud.getTimeline(20); /* Obtener linea de
       tiempo, los últimos 20 estados*/
                for (YambaStatus status : timeline) { //
                    Log.d(TAG,
                            String.format("%s: %s", status.getUser(), status.getMessage())); //Imprimir estados en consola
                    values.clear();
                    values.put(StatusContract.Column.ID, status.getId());
                    values.put(StatusContract.Column.USER, status.getUser());
                    values.put(StatusContract.Column.MESSAGE, status.getMessage());
                    values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                    db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                }
            } catch (YambaClientException e) { //
                Log.e(TAG, "Failed to fetch the timeline", e);
                e.printStackTrace();
            }
        }
        return;
        }


        @Override
    public void onDestroy(){
        if(isEmpty){
            Toast.makeText(this,"Please update your username and password",Toast.LENGTH_LONG).show();
            isEmpty=false;
        }
        Log.d(TAG,"onDestroyed");
    }


}
