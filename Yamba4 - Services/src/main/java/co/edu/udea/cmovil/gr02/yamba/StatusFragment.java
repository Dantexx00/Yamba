package co.edu.udea.cmovil.gr02.yamba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.thenewcircle.yamba.client.YambaClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {

    private final static String TAG = "statusFragents";
    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;
    private ProgressDialog progress;
    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_status,container,false);
        //return inflater.inflate(R.layout.fragment_status, container, false);
        //getActionBar().setTitle("Yamba2-Gr02");
        //getActionBar().setIcon(R.mipmap.my_icon);
        mButtonTweet = (Button) v.findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) v.findViewById(R.id.status_text);
        mTextCount = (TextView) v.findViewById(R.id.status_text_count);
        mTextCount.setText(Integer.toString(140));
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();
        deshabiltar_boton();
        mButtonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status=mTextStatus.getText().toString();
                PostTask postTask=new PostTask();
                postTask.execute(status);
                Log.d(TAG,"onClicked button");
            }
        });
        mTextStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count=140-s.length();
                mTextCount.setText(Integer.toString(count));
                if(count <50){
                    mTextCount.setTextColor(Color.RED);
                }else{
                    mTextCount.setTextColor(mDefaultColor);
                }
            }
        });
        Log.d(TAG, "onCreatedView");
        return v;
    }
    //Método para postear
    /*public void postear(View v){
        String status = mTextStatus.getText()+"";
        progress=new ProgressDialog(this);
        new PostTask().execute(status);
        Log.d(TAG, "onClicked");
        InputMethodManager input=(InputMethodManager)v.getgetSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(mTextStatus.getWindowToken(), 0);
    }*/
    //Metodo para deshabilitar el boton cuando no se ha copiado nada.
    public void deshabiltar_boton(){
        mButtonTweet.setEnabled(false);
        mTextStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String status = mTextStatus.getText().toString();
                if (status.length() > 0) {
                    mButtonTweet.setEnabled(true);
                } else {
                    mButtonTweet.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String status = mTextStatus.getText().toString();
                if (status.length() > 01) {
                    mButtonTweet.setEnabled(true);
                } else {
                    mButtonTweet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String status = mTextStatus.getText().toString();
                if ((status.length() > 0)&&(status.length()<=140)) {
                    mButtonTweet.setEnabled(true);
                } else {
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
            progress=ProgressDialog.show(getActivity(),"Posting","Please wait");
            progress.setCancelable(true);
            //progress.setTitle("Enviando post...");
            //progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String username = prefs.getString("username", "")+"";
                String password = prefs.getString("password", "")+"";

                // Check that username and password are not empty
                // If empty, Toast a message to set login info and bounce to
                // SettingActivity
                // Hint: TextUtils.
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    getActivity().startActivity(
                            new Intent(getActivity(), SettingsActivity.class));
                    return "Please update your username and password";
                }

                YambaClient cloud = new YambaClient(username, password);
                cloud.postStatus(params[0]);

                Log.d(TAG, "Successfully posted to the cloud: " + params[0]);
                return "Successfully posted";
            } catch (Exception e) {
                Log.e(TAG, "Failed to post to the cloud", e);
                e.printStackTrace();
                return "Failed to post";
            }
        }

        @Override
        protected void  onPostExecute(String result){
            progress.dismiss();
            if(getActivity()!=null && result!=null)
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            mTextStatus.setText("");
        }
    }
}
