package com.io.myapplication.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.io.myapplication.constants.ApplicationConstants;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return boolean true if it is connecting to internet else false
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void saveToPreferences(String key, String value) {
        final SharedPreferences prefs = getSharedPreferences(ApplicationConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getFromPreferences(String key) {
        final SharedPreferences prefs = getSharedPreferences(ApplicationConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String value = prefs.getString(key, "");
        if (!value.isEmpty()) {
            return value;
        }
        return "";
    }
}
