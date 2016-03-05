package com.njit.buddy.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import com.njit.buddy.app.CommentActivity;
import com.njit.buddy.app.HugActivity;
import com.njit.buddy.app.ProfileActivity;
import com.njit.buddy.app.R;
import com.njit.buddy.app.entity.Post;
import com.njit.buddy.app.network.task.BellTask;
import com.njit.buddy.app.network.task.FlagTask;
import com.njit.buddy.app.network.task.HugTask;
import com.njit.buddy.app.util.DateParser;
import com.njit.buddy.app.util.Log;

/**
 * @author toyknight on 10/8/2015.
 */
public class PostView extends RelativeLayout {

    private Post post_data;

    public PostView(Context context) {
        this(context, null);
    }

    public PostView(Context context, Post post_data) {
        super(context);
        this.post_data = post_data;
        View.inflate(getContext(), R.layout.view_post, this);
        ImageView btn_flag = (ImageView) findViewById(R.id.btn_flag);
        btn_flag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryFlag();
            }
        });
        ImageView btn_bell = (ImageView) findViewById(R.id.btn_bell);
        btn_bell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryBell();
            }
        });
        View btn_hug = findViewById(R.id.btn_hug);
        btn_hug.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onHug();
            }
        });
        View btn_comment = findViewById(R.id.btn_comment);
        btn_comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCommentActivity();
            }
        });
        View tv_username = findViewById(R.id.tv_username);
        tv_username.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileActivity();
            }
        });
        updateView();
    }

    public Post getPostData() {
        return post_data;
    }

    private int getUID() {
        SharedPreferences preferences = getContext().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        return preferences.getInt(getResources().getString(R.string.key_uid), 0);
    }

    public void setBellVisible(boolean visible) {
        View bell = findViewById(R.id.btn_bell);
        if (visible) {
            bell.setVisibility(VISIBLE);
        } else {
            bell.setVisibility(INVISIBLE);
        }
    }

    public void updateView() {
        if (getPostData() != null) {
            String post_username = getPostData().getUsername();
            String content = getPostData().getContent();
            String date = new DateParser().toString(getPostData().getTimestamp());
            int hug_count = getPostData().getHugs();
            boolean flagged = getPostData().isFlagged();
            boolean belled = getPostData().isBelled();
            boolean hugged = getPostData().isHugged();

            ((TextView) findViewById(R.id.tv_username)).setText(post_username);
            ((TextView) findViewById(R.id.tv_date)).setText(date);
            ((TextView) findViewById(R.id.tv_content)).setText(content);
            //flag button
            ImageView btn_flag = (ImageView) findViewById(R.id.btn_flag);
            btn_flag.setImageDrawable(flagged ?
                    getResources().getDrawable(R.drawable.ic_flag_selected) :
                    getResources().getDrawable(R.drawable.ic_flag_unselected));
            //bell button
            ImageView btn_bell = (ImageView) findViewById(R.id.btn_bell);
            btn_bell.setImageDrawable(belled ?
                    getResources().getDrawable(R.drawable.ic_bell_selected) :
                    getResources().getDrawable(R.drawable.ic_bell_unselected));
            btn_bell.setVisibility(getPostData().getCategory() == Post.ASK ? View.INVISIBLE : View.VISIBLE);
            //hug button
            ImageView icon_hug = (ImageView) findViewById(R.id.icon_hug);
            TextView tv_hug_count = (TextView) findViewById(R.id.tv_hug_count);
            if (getPostData().getUID() == getUID()) {
                icon_hug.setImageDrawable(getResources().getDrawable(R.drawable.ic_hug_unselected));
                tv_hug_count.setText(Integer.toString(hug_count));
            } else {
                icon_hug.setImageDrawable(hugged ?
                        getResources().getDrawable(R.drawable.ic_hug_selected) :
                        getResources().getDrawable(R.drawable.ic_hug_unselected));
            }
            View btn_comment = findViewById(R.id.btn_comment);
            btn_comment.setVisibility(getPostData().getCategory() == Post.ASK ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void onHug() {
        if (getPostData().getUID() == getUID()) {
            gotoHugActivity();
        } else {
            tryHug();
        }
    }

    public void gotoHugActivity() {
        Intent intent = new Intent(getContext(), HugActivity.class);
        intent.putExtra("pid", getPostData().getPID());
        getContext().startActivity(intent);
    }

    public void gotoCommentActivity() {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        getContext().startActivity(intent);
    }

    public void gotoProfileActivity() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(getContext().getString(R.string.key_uid), getPostData().getUID());
        getContext().startActivity(intent);
        ((Activity)getContext()).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void tryFlag() {
        FlagTask task = new FlagTask() {
            @Override
            public void onSuccess(Integer result) {
                getPostData().setFlagged(!getPostData().isFlagged());
                updateView();
            }

            @Override
            public void onFail(int error_code) {
                Log.error("Flag", error_code);
            }
        };
        task.execute(getPostData().getPID());
    }

    public void tryBell() {
        BellTask task = new BellTask() {
            @Override
            public void onSuccess(Integer result) {
                getPostData().setBelled(!getPostData().isBelled());
                updateView();
            }

            @Override
            public void onFail(int error_code) {
                Log.error("Bell", error_code);
            }
        };
        task.execute(getPostData().getPID());
    }

    public void tryHug() {
        HugTask task = new HugTask() {
            @Override
            public void onSuccess(Integer result) {
                if (getPostData().isHugged()) {
                    getPostData().setHugs(getPostData().getHugs() - 1);
                } else {
                    getPostData().setHugs(getPostData().getHugs() + 1);
                }
                getPostData().setHugged(!getPostData().isHugged());
                updateView();
            }

            @Override
            public void onFail(int error_code) {
                Log.error("Hug", error_code);
            }
        };
        task.execute(getPostData().getPID());
    }

}
