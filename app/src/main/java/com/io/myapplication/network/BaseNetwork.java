package com.io.myapplication.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.io.myapplication.model.BaseModel;
import com.io.myapplication.network.util.CustomVolleyRequestQueue;
import com.io.myapplication.network.util.NetworkUtil;
import com.io.myapplication.ui.activity.BaseActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by akash on 31/7/16.
 */
public class BaseNetwork {

    private Context mContext;
    private BaseModel mBaseModel;


    public BaseNetwork(Context context, BaseModel baseModel) {
        mContext = context;
        mBaseModel = baseModel;
    }

    /**
     * Generates the generic request header
     *
     * @return RequestHeader HashMap<String,String>
     */
    public HashMap<String, String> getRequestHeaderForAuthorization() {
        HashMap<String, String> requestHeader = new HashMap<>();

        //requestHeader.put("Content-Type", "application/json");
        requestHeader.put("X-CLIENT-ID", "MAPI");
        requestHeader.put("X-CLIENT-USER", "gomalon-apis");
        requestHeader.put("X-CLIENT-KEY", "Z29tYWxvbi1hcGlz");
        return requestHeader;


    }

    /**
     * Handle response(JSONObject) as per requirement
     *
     * @param response    The network call response in the form of JSONObject
     * @param requestType The network Request Type
     */
    void handleResponse(JSONObject response, int requestType) {
        try {

            mBaseModel.parseAndNotifyResponse(response, requestType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle all errors
     *
     * @param error Error generated due to network call
     */
    void handleError(VolleyError error, int requestType) {
        if ((mContext instanceof BaseActivity) && NetworkUtil.isNetworkProblem(error)) {
            /*((BaseActivity) mContext).hideProgressDialog();
            ((BaseActivity) mContext).showNoInternetLayout();*/
        }
        else if ((mContext instanceof BaseActivity)  ) {
            /*((BaseActivity) mContext).hideProgressDialog();*/

        }

        String errorMessage = null;
        int code;
        String message = "";
        try {
            errorMessage = new String(error.networkResponse.data, "UTF-8");

            JSONObject jsonObject = new JSONObject(errorMessage);
            code = jsonObject.getInt("code");
            if (code != 500) {
                JSONObject errorObj = jsonObject.getJSONObject("error");
                message = errorObj.getString("message");
            } else if (code == 500) {

                message = jsonObject.getString("message");
            }


            //JSONArray errorArray = new JSONArray(jsonObject.get("errors").toString());
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            /*((BaseActivity) mContext).hideProgressDialog();*/
            Toast.makeText(mContext, "Something went wrong ,please try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public void displayMessage(String toastString) {
        Toast.makeText(mContext, toastString, Toast.LENGTH_LONG).show();
    }


    /**
     * @param methodType   Type of network call eg, GET,POST, etc.
     * @param url          The url to hit
     * @param paramsObject JsonObject for POST request, null for GET request
     * @param requestType  Type of Network request
     */
    public void getJSONObjectForRequest(int methodType, String url, JSONObject paramsObject, final int requestType) {
        if (NetworkUtil.isInternetConnected(mContext)) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (methodType, url, paramsObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponse(response, requestType);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleError(error, requestType);
                        }
                    }) {

               /* @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }*/

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getRequestHeaderForAuthorization();
                }
            };
            int socketTimeout = 5000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);

            CustomVolleyRequestQueue.getInstance(mContext).getRequestQueue().add(jsObjRequest);
        } else {
         /*   ((BaseActivity) mContext).hideProgressDialog();
            ((BaseActivity) mContext).showNoInternetLayout();*/

        }
    }


}
