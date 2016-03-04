package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.entity.Hug;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author toyknight 11/23/2015.
 */
public abstract class HugListTask extends AsyncTask<Integer, Void, JSONObject> implements ResponseHandler<ArrayList<Hug>> {

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer pid = params[0];
        Integer page = params[1];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("pid", pid);
            request_body.put("page", page);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/post/hug/list", request_body.toString());
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
                    JSONArray hugs = result.getJSONArray("hugs");
                    ArrayList<Hug> hug_list = new ArrayList<Hug>();
                    for (int i = 0; i < hugs.length(); i++) {
                        JSONObject hug = hugs.getJSONObject(i);
                        hug_list.add(new Hug(hug.getInt("uid"), hug.getString("username")));
                    }
                    onSuccess(hug_list);
                } else {
                    onFail(response_code);
                }
            } catch (JSONException ex) {
                onFail(ResponseCode.SERVER_ERROR);
            }
        }
    }

}
