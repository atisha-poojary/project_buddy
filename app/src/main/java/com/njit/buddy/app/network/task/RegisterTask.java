package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 11/2/2015.
 */
public abstract class RegisterTask extends AsyncTask<String, Void, Integer> implements ResponseHandler<Integer> {

    @Override
    protected Integer doInBackground(String... params) {
        String email = params[0];
        String username = params[1];
        String password = params[2];
        String verification = params[3];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("email", email);
            request_body.put("username", username);
            request_body.put("password", password);
            request_body.put("verification", verification);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/register", request_body.toString());
            JSONObject response = new JSONObject(result);
            return response.getInt("response_code");
        } catch (JSONException ex) {
            Log.d("Login", ex.toString());
            return ResponseCode.SERVER_ERROR;
        } catch (IOException ex) {
            Log.d("Login", ex.toString());
            return ResponseCode.SERVER_ERROR;
        }
    }

    @Override
    protected final void onPostExecute(Integer response_code) {
        if (response_code == ResponseCode.BUDDY_OK) {
            onSuccess(response_code);
        } else {
            onFail(response_code);
        }
    }

}
