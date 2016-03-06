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
public abstract class ProfileEditTask extends AsyncTask<Object, Void, Integer> implements ResponseHandler<Integer> {

    @Override
    protected Integer doInBackground(Object... params) {
        String username = (String) params[0];
        String description = (String) params[1];
        int description_open = (Integer) params[2];
        String birthday = (String) params[3];
        int birthday_open = (Integer) params[4];
        String gender = (String) params[5];
        int gender_open = (Integer) params[6];
        String sexuality = (String) params[7];
        int sexuality_open = (Integer) params[8];
        String race = (String) params[9];
        int race_open = (Integer) params[10];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("username", username);
            request_body.put("description", description);
            request_body.put("description_open", description_open);
            request_body.put("birthday", birthday);
            request_body.put("birthday_open", birthday_open);
            request_body.put("gender", gender);
            request_body.put("gender_open", gender_open);
            request_body.put("sexuality", sexuality);
            request_body.put("sexuality_open", sexuality_open);
            request_body.put("race", race);
            request_body.put("race_open", race_open);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/profile/edit", request_body.toString());
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
