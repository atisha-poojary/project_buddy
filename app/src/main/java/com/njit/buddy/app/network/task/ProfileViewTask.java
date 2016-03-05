package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.entity.Profile;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author toyknight 11/23/2015.
 */
public abstract class ProfileViewTask extends AsyncTask<Integer, Void, JSONObject> implements ResponseHandler<Profile> {

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer uid = params[0];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("uid", uid);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/profile/view", request_body.toString());
            return new JSONObject(result);
        } catch (JSONException ex) {
            Log.d("JSON", ex.toString());
            return null;
        } catch (IOException ex) {
            Log.d("Network", ex.toString());
            return null;
        }
    }

    @Override
    protected final void onPostExecute(JSONObject result) {
        if (result == null) {
            onFail(ResponseCode.SERVER_ERROR);
        } else {
            try {
                int response_code = result.getInt("response_code");
                if (response_code == ResponseCode.BUDDY_OK) {
                    Profile profile = new Profile();
                    profile.setUsername(result.getString("username"));
                    profile.setDescription(result.has("description") ? result.getString("description") : "Hidden");
                    profile.setBirthday(result.has("birthday") ? result.getString("birthday") : "Hidden");
                    profile.setGender(result.has("gender") ? result.getString("gender") : "Hidden");
                    profile.setSexuality(result.has("sexuality") ? result.getString("sexuality") : "Hidden");
                    profile.setRace(result.has("race") ? result.getString("race") : "Hidden");
                    onSuccess(profile);
                } else {
                    onFail(response_code);
                }
            } catch (JSONException ex) {
                Log.d("Error", ex.toString());
                onFail(ResponseCode.SERVER_ERROR);
            }
        }
    }

}
