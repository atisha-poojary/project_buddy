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
        Integer start = params[1];
        Integer count = params[2];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("pid", pid);
            request_body.put("start", start);
            request_body.put("count", count);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/hug/list", request_body.toString());
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
//            ArrayList<Hug> hug_list = new ArrayList<Hug>();
//            for (int i = 0; i < result.length(); i++) {
//                try {
//                    JSONObject element = result.getJSONObject(i);
//                    Hug hug = new Hug(element.getInt("uid"), element.getString("username"));
//                    hug.setHuggedBack(element.getInt("hug_back") != 0);
//                    hug_list.add(hug);
//                } catch (JSONException ex) {
//                    Log.d("Error", ex.toString());
//                }
//            }
            onSuccess(null);
        }
    }

}
