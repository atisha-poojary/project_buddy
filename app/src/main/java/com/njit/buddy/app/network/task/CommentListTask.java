package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.entity.Comment;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author toyknight 3/5/2016.
 */
public abstract class CommentListTask
        extends AsyncTask<Integer, Void, JSONObject> implements ResponseHandler<ArrayList<Comment>> {

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer pid = params[0];
        Integer page = params[1];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("pid", pid);
            request_body.put("page", page);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/comment/list", request_body.toString());
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
                    JSONArray comments = result.getJSONArray("comments");
                    ArrayList<Comment> comment_list = new ArrayList<Comment>();
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject element = comments.getJSONObject(i);
                        comment_list.add(new Comment(
                                element.getInt("uid"),
                                element.getString("username"),
                                element.getLong("timestamp"),
                                element.getString("content")));
                    }
                    onSuccess(comment_list);
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
