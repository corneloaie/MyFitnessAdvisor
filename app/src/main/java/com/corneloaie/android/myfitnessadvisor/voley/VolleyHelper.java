package com.corneloaie.android.myfitnessadvisor.voley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.corneloaie.android.myfitnessadvisor.voley.requests.RequestJSONObject;

import org.json.JSONObject;

/**
 * Created by Cornel-PC on 08/11/2017.
 */

public class VolleyHelper {

    private static final String apiBaseUrl = "https://api.fitbit.com";
    private static String TAG = "VolleyHelper";
    private static VolleyHelper volleyHelper;
    private static Context context;
    private RequestQueue requestQueue;
    private String token;

    private VolleyHelper(Context context) {
        VolleyHelper.context = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized void initInstance(Context context) {
        if (volleyHelper == null) {
            volleyHelper = new VolleyHelper(context);
        }
    }

    public static synchronized VolleyHelper getInstance() {
        if (volleyHelper == null) {
            throw new RuntimeException("Not initialized");
        }
        return volleyHelper;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void get(String resourceName, final VolleyCallback callback, final Context context) {
        RequestQueue localRequestQueue = getRequestQueue();
        String url = apiBaseUrl + "/" + resourceName;

        Response.ErrorListener errorListener = callback::onError;

        Response.Listener<JSONObject> responseListener = response -> {
            Log.e(TAG, "onResponse = \n " + response.toString());
            callback.onSuccess(response);
        };
        localRequestQueue.add(new RequestJSONObject(Request.Method.GET, url, null /* JSON object*/, responseListener,
                errorListener, token));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
