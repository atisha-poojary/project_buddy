package com.njit.buddy.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.entity.Hug;
import com.njit.buddy.app.network.task.HugListTask;
import com.njit.buddy.app.util.Log;
import com.njit.buddy.app.widget.HugView;
import com.njit.buddy.app.widget.BuddyScrollListener;
import com.njit.buddy.app.widget.BuddyScrollView;

import java.util.ArrayList;

/**
 * @author toyknight 11/23/2015.
 */
public class HugActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe_container;

    private int current_page;

    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pid = getIntent().getIntExtra("pid", -1);
        setContentView(R.layout.activity_hug);
        initComponents();

        tryRefreshHugList();
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_hug));
        }
        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container_hugs);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tryRefreshHugList();
            }
        });
        ((BuddyScrollView) findViewById(R.id.scroll_view_hugs)).setScrollListener(new BuddyScrollListener() {
            @Override
            public void onBottomReached() {
                tryReadMoreHugs();
            }
        });
    }

    public void tryRefreshHugList() {
        current_page = 0;
        HugListTask task = new HugListTask() {
            @Override
            public void onSuccess(ArrayList<Hug> result) {
                swipe_container.setRefreshing(false);
                setHugList(result);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.error("List Hug", error_code);
            }
        };
        task.execute(pid, current_page);
    }

    public void tryReadMoreHugs() {
        HugListTask task = new HugListTask() {
            @Override
            public void onSuccess(ArrayList<Hug> result) {
                swipe_container.setRefreshing(false);
                setHugList(result);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.error("List Hug", error_code);
            }
        };
        task.execute(pid, ++current_page);
    }

    public void setHugList(ArrayList<Hug> hug_list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.hug_list_layout);
        layout.removeAllViews();
        for (Hug hug : hug_list) {
            HugView post_view = new HugView(this, hug);
            layout.addView(post_view);
        }
    }

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }

    };

}
