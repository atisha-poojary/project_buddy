package com.njit.buddy.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.entity.Comment;
import com.njit.buddy.app.network.task.CommentCreateTask;
import com.njit.buddy.app.network.task.CommentListTask;
import com.njit.buddy.app.util.Log;
import com.njit.buddy.app.widget.BuddyScrollListener;
import com.njit.buddy.app.widget.BuddyScrollView;
import com.njit.buddy.app.widget.CommentView;

import java.util.ArrayList;

/**
 * @author toyknight 12/6/2015.
 */
public class CommentActivity extends AppCompatActivity {

    private EditText input_comment;

    private int current_page;

    private int pid;

    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initComponents();

        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);
        uid = intent.getIntExtra("uid", 0);
        ((TextView) findViewById(R.id.tv_username)).setText(intent.getStringExtra("username"));
        ((TextView) findViewById(R.id.tv_date)).setText(intent.getStringExtra("date"));
        ((TextView) findViewById(R.id.tv_post_content)).setText(intent.getStringExtra("content"));

        tryRefreshCommentList();
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_comment));
        }
        input_comment = (EditText) findViewById(R.id.input_comment);
        input_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int action_id, KeyEvent event) {
                if (action_id == EditorInfo.IME_ACTION_SEND) {
                    InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ime.hideSoftInputFromWindow(
                            input_comment.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    tryComment(input_comment.getText().toString());
                    return true;
                }
                return false;
            }
        });
        BuddyScrollView comment_scroll_view = (BuddyScrollView) findViewById(R.id.scroll_view_comments);
        comment_scroll_view.setScrollListener(new BuddyScrollListener() {
            @Override
            public void onBottomReached() {
                tryReadMoreComments();
            }
        });
        findViewById(R.id.tv_username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileActivity(uid);
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void gotoProfileActivity(int uid) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(getString(R.string.key_uid), uid);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void tryComment(String content) {
        CommentCreateTask task = new CommentCreateTask() {
            @Override
            public void onSuccess(Integer result) {
                input_comment.setText("");
                tryRefreshCommentList();
            }

            @Override
            public void onFail(int error_code) {
            }
        };
        task.execute(pid, content);
    }

    public void tryRefreshCommentList() {
        current_page = 0;
        CommentListTask task = new CommentListTask() {
            @Override
            public void onSuccess(ArrayList<Comment> post_list) {
                setCommentList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                Log.error("News", error_code);
            }
        };
        task.execute(pid, current_page);
    }

    public void tryReadMoreComments() {
        CommentListTask task = new CommentListTask() {
            @Override
            public void onSuccess(ArrayList<Comment> post_list) {
                addCommentList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                Log.error("News", error_code);
            }
        };
        task.execute(pid, ++current_page);
    }

    private void setCommentList(ArrayList<Comment> comment_list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.comments_layout);
        layout.removeAllViews();
        for (Comment comment : comment_list) {
            CommentView comment_view = new CommentView(this, comment);
            layout.addView(comment_view);
        }
    }

    private void addCommentList(ArrayList<Comment> comment_list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.comments_layout);
        for (Comment comment : comment_list) {
            CommentView comment_view = new CommentView(this, comment);
            layout.addView(comment_view);
        }
    }

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }

    };

}
