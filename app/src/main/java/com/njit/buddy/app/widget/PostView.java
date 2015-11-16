package com.njit.buddy.app.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.njit.buddy.app.R;
import com.njit.buddy.app.network.task.BellTask;
import com.njit.buddy.app.network.task.HugTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author toyknight on 10/8/2015.
 */
public class PostView extends RelativeLayout {

    private JSONObject post_data;

    public PostView(Context context) {
        this(context, null);
    }

    public PostView(Context context, JSONObject post_data) {
        super(context);
        this.post_data = post_data;
        View.inflate(getContext(), R.layout.view_post, this);

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
                TextView btn_bell = (TextView) findViewById(R.id.btn_bell);
                btn_bell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryBell();
                    }
                });
                updateButtons();
            } catch (JSONException ex) {
                Log.d("JSON", ex.toString());
            }
        }
    }

    public JSONObject getPostData() {
        return post_data;
    }

    public int getPostID() {
        try {
            return post_data.getInt("pid");
        } catch (JSONException ex) {
            return -1;
        }
    }

    public void setBellVisible(boolean visible) {
        View bell = findViewById(R.id.btn_bell);
        if (visible) {
            bell.setVisibility(VISIBLE);
        } else {
            bell.setVisibility(INVISIBLE);
        }
    }

    public void updateButtons() throws JSONException {
        //hug button
        Button btn_hug = (Button) findViewById(R.id.btn_hug);
        Button btn_hugged = (Button) findViewById(R.id.btn_hugged);
        int hugged = getPostData().getInt("hug");
        btn_hugged.setText(Integer.toString(hugged));
        btn_hug.setEnabled(getPostData().getInt("huged") == 0);
        //bell button
        TextView btn_bell = (TextView) findViewById(R.id.btn_bell);
        int belled = getPostData().getInt("attention");
        if (belled == 0) {
            btn_bell.setText("Bell");
        } else {
            btn_bell.setText("Belled");
        }
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
                        updateButtons();
                    } catch (JSONException ex) {
                        Log.d("HUG", ex.toString());
                    }
                } else {
                    Log.d("HUG", "Error code " + response_code);
                }
            }
        };
        task.execute(getPostID());
    }

    public void tryBell() {
        BellTask task = new BellTask() {
            @Override
            public void onSuccess(Integer result) {
                TextView btn_bell = (TextView) findViewById(R.id.btn_bell);
                String text = btn_bell.getText().toString().equals("Bell") ? "Belled" : "Bell";
                btn_bell.setText(text);
            }

            @Override
            public void onFail(int error_code) {
                Log.d("Bell", "Error code " + error_code);
            }
        };
        task.execute(getPostID());
    }

}
