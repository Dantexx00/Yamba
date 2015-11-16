package co.edu.udea.cmovil.gr02.yamba;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;


import com.thenewcircle.yamba.client.YambaClient;


public class StatusActivity extends Activity {

    private static final String TAG=StatusActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        if(savedInstanceState==null){
            StatusFragment fragment=new StatusFragment();
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content,fragment,fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }
    public boolean onOptionItemSelected(MenuItem item){

        int id=item.getItemId();
        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
