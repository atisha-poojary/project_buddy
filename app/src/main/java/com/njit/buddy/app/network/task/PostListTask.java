package com.njit.buddy.app.network.task;

import android.os.AsyncTask;
import android.util.Log;
import com.njit.buddy.app.entity.Post;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.ResponseCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author by toyknight 10/3/2015.
 */
public abstract class PostListTask
        extends AsyncTask<Integer, Void, JSONObject> implements ResponseHandler<ArrayList<Post>> {

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer page = params[0];
        Integer category = params[1];
        Integer attention = params[2];

        try {
            JSONObject request_body = new JSONObject();
            request_body.put("page", page);
            request_body.put("category", category);
            request_body.put("attention", attention);

            String result = Connector.executePost(Connector.SERVER_ADDRESS + "/post/list", request_body.toString());
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
                    JSONArray posts = result.getJSONArray("posts");
                    ArrayList<Post> post_list = new ArrayList<Post>();
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject element = posts.getJSONObject(i);
                        Post post = new Post(
                                element.getInt("pid"),
                                element.getInt("uid"),
                                element.getString("username"),
                                element.getString("content"),
                                element.getInt("category"),
                                element.getLong("timestamp"));
                        post.setHugs(element.getInt("hugs"));
                        post.setFlagged(element.getInt("flagged") != 0);
                        post.setBelled(element.getInt("belled") != 0);
                        post.setHugged(element.getInt("hugged") != 0);
                        post_list.add(post);
                    }
                    onSuccess(post_list);
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
