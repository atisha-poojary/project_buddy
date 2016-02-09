package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseValue;
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

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("email", email);
            request_body.put("username", username);
            request_body.put("password", password);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/register", request_body.toString());
            JSONObject response = new JSONObject(result);
            return response.getInt("responsevalue");
        } catch (JSONException ex) {
            Log.d("Login", ex.toString());
            return ResponseValue.BUDDY_BAD_REQUEST;
        } catch (IOException ex) {
            Log.d("Login", ex.toString());
            return ResponseValue.BUDDY_BAD_REQUEST;
        }
    }

    @Override
    protected final void onPostExecute(final Integer response_code) {
        if (response_code == ResponseValue.BUDDY_OK) {
            onSuccess(response_code);
        } else {
            onFail(response_code);
        }
    }

}
