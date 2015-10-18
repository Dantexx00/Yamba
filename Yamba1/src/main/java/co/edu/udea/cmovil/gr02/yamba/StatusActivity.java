package co.edu.udea.cmovil.gr02.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;


public class StatusActivity extends Activity {

    private final static String TAG = StatusActivity.class.getSimpleName();
    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mButtonTweet = (Button) findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) findViewById(R.id.status_text);
        mTextCount = (TextView) findViewById(R.id.status_text_count);
        mTextCount.setText(Integer.toString(140));
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();
        deshabiltar_boton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //Método para postear
    public void postear(View v){
        String status = mTextStatus.getText()+"";
        progress=new ProgressDialog(this);
        new PostTask().execute(status);
        Log.d(TAG, "onClicked");
        InputMethodManager input=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(mTextStatus.getWindowToken(),0);
        mTextStatus.setText("");
    }
    //Metodo para deshabilitar el boton cuando no se ha copiado nada.
    public void deshabiltar_boton(){
        mButtonTweet.setEnabled(false);
        mTextStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String status=mTextStatus.getText().toString();
                if(status.length()>0){
                    mButtonTweet.setEnabled(true);
                }else{
                    mButtonTweet.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String status=mTextStatus.getText().toString();
                if(status.length()>01){
                    mButtonTweet.setEnabled(true);
                }else{
                    mButtonTweet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String status=mTextStatus.getText().toString();
                if(status.length()>0){
                    mButtonTweet.setEnabled(true);
                }else{
                    mButtonTweet.setEnabled(false);
                }

            }
        });
    }

    //Subclase para postear
    class PostTask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute(){
            super.onPreExecute();

           progress.setTitle("Enviando post...");
           progress.show();
        }

        @Override
        protected String doInBackground(String... params){
            try{
                YambaClient cloud = new YambaClient("student", "password");
                cloud.postStatus(params[0]);

                Log.d(TAG, "Successfull posted to the cloud: " + params[0]);
                return "Successfully posted";
            }catch(Exception e){
                Log.e(TAG, "Failed to post to the cloud", e);
                e.printStackTrace();
                return "Failed to post";
            }

        }

        @Override
        protected void  onPostExecute(String result){
          progress.dismiss();
            if(this!=null && result!=null)
                Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

}
