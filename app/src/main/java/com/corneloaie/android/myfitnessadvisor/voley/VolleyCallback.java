package com.corneloaie.android.myfitnessadvisor.voley;

import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Cornel-PC on 08/11/2017.
 */

public class VolleyCallback {
    public void onSucces() {
    }

    ;

    public void onSuccess(JSONObject object) {
    }

    ;

    public void onSuccess(JSONArray array) {
    }

    ;

    public void onError(VolleyError error) {
        Log.e(this.getClass().getSimpleName(), "Api error = " + error.getMessage());
    }
}
