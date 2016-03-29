package com.njit.buddy.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.njit.buddy.app.R;
import com.njit.buddy.app.entity.Post;
import com.njit.buddy.app.network.task.PostListTask;
import com.njit.buddy.app.widget.BuddyScrollListener;
import com.njit.buddy.app.widget.BuddyScrollView;
import com.njit.buddy.app.widget.PostView;

import java.util.ArrayList;

/**
 * @author toyknight 8/15/2015.
 */
public class AttentionFragment extends Fragment {

    private SwipeRefreshLayout swipe_container;
    private int current_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attention, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        swipe_container = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container_attention);
        swipe_container.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        tryRefreshAttentionList();
                    }
                }
        );
        BuddyScrollView post_scroll_view = (BuddyScrollView) getActivity().findViewById(R.id.scroll_view_attention);
        post_scroll_view.setScrollListener(new BuddyScrollListener() {
            @Override
            public void onBottomReached() {
                tryReadMorePosts();
            }
        });
    }

    public void tryRefreshAttentionList() {
        current_page = 0;
        PostListTask task = new PostListTask() {
            @Override
            public void onSuccess(ArrayList<Post> post_list) {
                swipe_container.setRefreshing(false);
                setAttentionList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.d("Attention", "Error code " + error_code);
            }
        };
        task.execute(current_page, -1, 1, 0);
    }

    public void tryReadMorePosts() {
        PostListTask task = new PostListTask() {
            @Override
            public void onSuccess(ArrayList<Post> post_list) {
                swipe_container.setRefreshing(false);
                addAttentionList(post_list);
            }

            @Override
            public void onFail(int error_code) {
                swipe_container.setRefreshing(false);
                Log.d("Attention", "Error code " + error_code);
            }
        };
        task.execute(++current_page, -1, 1, 0);

    }

    private void setAttentionList(ArrayList<Post> post_list) {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.attention_layout);
        layout.removeAllViews();
        for (Post post : post_list) {
            PostView post_view = new PostView(getActivity(), post);
            post_view.setBellVisible(false);
            layout.addView(post_view);
        }
    }

    private void addAttentionList(ArrayList<Post> post_list) {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.attention_layout);
        for (Post post : post_list) {
            PostView post_view = new PostView(getActivity(), post);
            post_view.setBellVisible(false);
            layout.addView(post_view);
        }
    }

}
