package com.io.myapplication.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.io.myapplication.R;
import com.io.myapplication.constants.ApplicationConstants;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends BaseActivity {
    private Button bSave,bGrab;
    private EditText etPhone;
    private TextView tvSelectMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bSave = (Button) findViewById(R.id.bsave);
        bGrab=(Button)findViewById(R.id.bgrab);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvSelectMusic = (TextView) findViewById(R.id.selectmusic);

        if (!getFromPreferences(ApplicationConstants.IS_FIRST_TIME).equalsIgnoreCase(ApplicationConstants.YES)) {
            bGrab.setVisibility(View.GONE);

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    FlicManager.getInstance(MainActivity.this, new FlicManagerInitializedCallback() {
                        @Override
                        public void onInitialized(FlicManager manager) {
                            manager.initiateGrabButton(MainActivity.this);
                        }
                    });
                } catch (FlicAppNotInstalledException err) {
                    Toast.makeText(MainActivity.this, "App is not installed", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        ApplicationConstants.REQUEST_READ_PHONE_STATE_PERMISSION);
            }
        }else{
            bGrab.setVisibility(View.GONE);
        }

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saveToPreferences("phone", etPhone.getText().toString());
                    showToast("Primary Number saved");

            }
        });

        tvSelectMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });

        bGrab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        FlicManager.getInstance(MainActivity.this, new FlicManagerInitializedCallback() {
                            @Override
                            public void onInitialized(FlicManager manager) {
                                manager.initiateGrabButton(MainActivity.this);
                            }
                        });
                    } catch (FlicAppNotInstalledException err) {
                        Toast.makeText(MainActivity.this, "App is not installed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            ApplicationConstants.REQUEST_READ_PHONE_STATE_PERMISSION);
                }
            }
        });

    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {


        if (resultCode == RESULT_OK && requestCode == 10) {
            Uri uriSound = data.getData();
            saveToPreferences("song",uriSound.toString());
            tvSelectMusic.setTextColor(getResources().getColor(R.color.gomalan_green_bg));
            tvSelectMusic.setText("Song selected: "+uriSound.toString());
        } else {
            FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
                @Override
                public void onInitialized(FlicManager manager) {
                    if (data != null) {
                        FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                        if (button != null) {
                            button.registerListenForBroadcast(FlicBroadcastReceiverFlags.ALL | FlicBroadcastReceiverFlags.REMOVED);
                            Toast.makeText(MainActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                            saveToPreferences(ApplicationConstants.IS_FIRST_TIME, ApplicationConstants.YES);
                        } else {
                            Toast.makeText(MainActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ApplicationConstants.REQUEST_READ_PHONE_STATE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
                            @Override
                            public void onInitialized(FlicManager manager) {
                                manager.initiateGrabButton(MainActivity.this);
                            }
                        });
                    } catch (FlicAppNotInstalledException err) {
                        Toast.makeText(this, "App is not installed", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    showToast("Permission Denied, please give permission to continue");
                    finish();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
