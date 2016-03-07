package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 3/6/2016.
 */
public class BuddyUseTask extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/record", "{}");
            JSONObject response = new JSONObject(result);
            return response.getInt("response_code");
        } catch (JSONException ex) {
            Log.d("Network", ex.toString());
            return ResponseCode.SERVER_ERROR;
        } catch (IOException ex) {
            Log.d("Network", ex.toString());
            return ResponseCode.SERVER_ERROR;
        }
    }

}
