package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 4/11/2016.
 */
public abstract class VerificationSendingTask
        extends AsyncTask<String, Void, Integer> implements ResponseHandler<Integer> {

    @Override
    protected Integer doInBackground(String... params) {
        String email = params[0];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("email", email);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/verification", request_body.toString());
            JSONObject response = new JSONObject(result);
            return response.getInt("response_code");
        } catch (JSONException ex) {
            Log.d("Verification", ex.toString());
            return ResponseCode.SERVER_ERROR;
        } catch (IOException ex) {
            Log.d("Verification", ex.toString());
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
