package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.entity.Authorization;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 10/22/2015.
 */
public abstract class LoginTask extends AsyncTask<String, Void, JSONObject> implements ResponseHandler<Authorization> {

    @Override
    protected JSONObject doInBackground(String... params) {
        String email = params[0];
        String password = params[1];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("email", email);
            request_body.put("password", password);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/login", request_body.toString());
            return new JSONObject(result);
        } catch (JSONException ex) {
            Log.d("Login", ex.toString());
            return null;
        } catch (IOException ex) {
            Log.d("Login", ex.toString());
            return null;
        }
    }

    @Override
    protected final void onPostExecute(JSONObject response) {
        if (response == null) {
            onFail(ResponseCode.SERVER_ERROR);
        } else {
            try {
                int response_code = response.getInt("response_code");
                if (response_code == ResponseCode.BUDDY_OK) {
                    int uid = response.getInt("uid");
                    String authorization = response.getString("authorization");
                    onSuccess(new Authorization(uid, authorization));
                } else {
                    Log.d("Login", "Error code: " + response_code);
                    onFail(response_code);
                }
            } catch (JSONException ex) {
                Log.d("Login", ex.toString());
                onFail(ResponseCode.SERVER_ERROR);
            }
        }
    }

}
