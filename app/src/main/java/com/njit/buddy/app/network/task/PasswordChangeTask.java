package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 4/10/2016.
 */
public abstract class PasswordChangeTask extends AsyncTask<String, Void, JSONObject> implements ResponseHandler<Integer> {

    @Override
    protected JSONObject doInBackground(String... params) {
        String old_password = params[0];
        String new_password = params[1];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("old_password", old_password);
            request_body.put("new_password", new_password);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/password/change", request_body.toString());
            return new JSONObject(result);
        } catch (JSONException ex) {
            Log.d("Password Change", ex.toString());
            return null;
        } catch (IOException ex) {
            Log.d("Password Change", ex.toString());
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
                    onSuccess(response_code);
                } else {
                    Log.d("Password Change", "Error code: " + response_code);
                    onFail(response_code);
                }
            } catch (JSONException ex) {
                Log.d("Password Change", ex.toString());
                onFail(ResponseCode.SERVER_ERROR);
            }
        }
    }

}
