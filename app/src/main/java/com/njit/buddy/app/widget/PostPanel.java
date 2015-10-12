package com.njit.buddy.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.njit.buddy.app.R;
import com.njit.buddy.app.network.HugTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author toyknight on 10/8/2015.
 */
@SuppressLint("ViewConstructor")
public class PostPanel extends RelativeLayout {

    private final JSONObject post_data;

    public PostPanel(Context context, JSONObject post_data) {
        super(context);
        this.post_data = post_data;
        inflate(getContext(), R.layout.panel_post, this);

        if (getPostData() != null) {
            try {
                String username = getPostData().getString("username");
                Long date_time = getPostData().getLong("date_time");
                String date_string = SimpleDateFormat.getDateInstance().format(new Date(date_time));
                String content = getPostData().getString("content");
                ((TextView) findViewById(R.id.tv_username)).setText(username);
                ((TextView) findViewById(R.id.tv_date)).setText(date_string);
                ((TextView) findViewById(R.id.tv_content)).setText(content);
                Button btn_hug = (Button) findViewById(R.id.btn_hug);
                btn_hug.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryHug();
                    }
                });
                updateHug();
            } catch (JSONException ex) {
                Log.d("JSON", ex.toString());
            }
        }
    }

    public JSONObject getPostData() {
        return post_data;
    }

    public void updateHug() throws JSONException {
        Button btn_hug = (Button) findViewById(R.id.btn_hug);
        int hug = getPostData().getInt("hug");
        btn_hug.setText(getResources().getString(R.string.label_hug) + ": " + hug);
        btn_hug.setEnabled(getPostData().getInt("huged") == 0);
    }

    public void tryHug() {
        HugTask task = new HugTask() {
            @Override
            protected void onPostExecute(Integer response_code) {
                if (response_code == 1) {
                    try {
                        int hug = getPostData().getInt("hug");
                        getPostData().put("hug", hug + 1);
                        getPostData().put("huged", 1);
                        updateHug();
                    } catch (JSONException ex) {
                        Log.d("HUG", ex.toString());
                    }
                } else {
                    Log.d("HUG", "Error code " + response_code);
                }
            }
        };
        try {
            Integer pid = post_data.getInt("pid");
            task.execute(pid);
        } catch (JSONException ex) {
            Log.d("HUG", ex.toString());
        }
    }

}