package com.njit.buddy.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.entity.Post;
import com.njit.buddy.app.network.task.PostListTask;
import com.njit.buddy.app.widget.BuddyScrollListener;
import com.njit.buddy.app.widget.BuddyScrollView;
import com.njit.buddy.app.widget.PostView;

import java.util.ArrayList;

/**
 * @author toyknight 4/10/2016.
 */
public class UserPostsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe_container;
    private int current_page;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        uid = getIntent().getIntExtra(getString(R.string.key_uid), 0);
        initComponents();
        tryRefreshPostList();
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_user_posts));
        }
        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container_user_posts);
        swipe_container.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        tryRefreshPostList();
                    }
                }
        );
        BuddyScrollView post_scroll_view = (BuddyScrollView) findViewById(R.id.scroll_view_user_posts);
        post_scroll_view.setScrollListener(new BuddyScrollListener() {
            @Override
            public void onBottomReached() {
                tryReadMorePosts();
            }
        });
    }

    private void tryRefreshPostList() {
        current_page = 0;
        PostListTask task = new PostListTask() {
            @Override
            public void onSuccess(ArrayList<Post> post_list) {
                swipe_container.setRefreshing(false);
                setPostList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.d("Attention", "Error code " + error_code);
            }
        };
        task.execute(current_page, -1, 0, uid);
    }

    public void tryReadMorePosts() {
        PostListTask task = new PostListTask() {
            @Override
            public void onSuccess(ArrayList<Post> post_list) {
                swipe_container.setRefreshing(false);
                addPostList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.d("Attention", "Error code " + error_code);
            }
        };
        task.execute(++current_page, -1, 0, uid);
    }

    private void setPostList(ArrayList<Post> post_list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.user_posts_layout);
        layout.removeAllViews();
        for (Post post : post_list) {
            PostView post_view = new PostView(this, post);
            post_view.setBellVisible(false);
            layout.addView(post_view);
        }
    }

    private void addPostList(ArrayList<Post> post_list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.user_posts_layout);
        for (Post post : post_list) {
            PostView post_view = new PostView(this, post);
            post_view.setBellVisible(false);
            layout.addView(post_view);
        }
    }

    private void gotoProfileActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            gotoProfileActivity();
        }

    };

}
