package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 3/5/2016.
 */
public abstract class CommentCreateTask extends AsyncTask<Object, Void, Integer> implements ResponseHandler<Integer> {

    @Override
    protected Integer doInBackground(Object... params) {
        int pid = (Integer) params[0];
        String content = (String) params[1];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("pid", pid);
            request_body.put("content", content);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/comment/create", request_body.toString());
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

    @Override
    protected final void onPostExecute(Integer response_code) {
        if (response_code == ResponseCode.BUDDY_OK) {
            onSuccess(response_code);
        } else {
            onFail(response_code);
        }
    }

}
