package com.corneloaie.android.myfitnessadvisor;

import android.app.Application;

import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;


public class DefaultApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        VolleyHelper.initInstance(getApplicationContext());
        CertificateManager.allowAllCertificates();
    }


}
