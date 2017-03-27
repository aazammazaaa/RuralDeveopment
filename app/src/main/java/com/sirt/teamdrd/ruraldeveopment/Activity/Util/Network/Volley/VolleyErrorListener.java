package com.sirt.teamdrd.ruraldeveopment.Activity.Util.Network.Volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.sirt.teamdrd.ruraldeveopment.R;

/**
 * Created by Aazam on 19/11/2016.
 */

public abstract class VolleyErrorListener implements Response.ErrorListener {
    private Context context;

    public VolleyErrorListener(Context context){
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(context != null){
            String errorMessage = context.getString(R.string.error_request_failure);
            if (error instanceof TimeoutError) {
                errorMessage = context.getString(R.string.error_network_timeout);
            }else if (error instanceof NoConnectionError) {
                errorMessage = context.getString(R.string.error_no_connection);
            }  else if (error instanceof AuthFailureError) {
                errorMessage = context.getString(R.string.error_auth_failure);
            } else if (error instanceof ServerError) {
                errorMessage = context.getString(R.string.error_server_failure);
            } else if (error instanceof NetworkError) {
                errorMessage = context.getString(R.string.error_network_failure);
            } else if (error instanceof ParseError) {
                errorMessage = context.getString(R.string.error_parsing_failure);
            }
            //ToastUtil.showToast(context,errorMessage,Toast.LENGTH_SHORT);
            handleVolleyError(error, errorMessage);
        }
    }

    public abstract void handleVolleyError(VolleyError error, String message);

}
