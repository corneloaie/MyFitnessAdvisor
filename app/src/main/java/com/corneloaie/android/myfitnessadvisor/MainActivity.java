package com.corneloaie.android.myfitnessadvisor;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    public static final String URL = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=228MYG&redirect_uri=myapp%3A%2F%2Fscreen&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        textView = (TextView) findViewById(R.id.textView);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(URL));
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if(uri!=null){
            textView.setText(uri.toString());
            System.out.println(uri.toString());
        }

    }
}
