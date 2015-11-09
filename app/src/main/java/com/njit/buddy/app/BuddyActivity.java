package com.njit.buddy.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.njit.buddy.app.fragment.AttentionFragment;
import com.njit.buddy.app.fragment.MoodFragment;
import com.njit.buddy.app.fragment.MoreFragment;
import com.njit.buddy.app.fragment.NewsFragment;
import com.njit.buddy.app.network.task.PostCreateTask;
import com.njit.buddy.app.network.task.NewsListTask;
import org.json.JSONArray;

/**
 * @author toyknight 8/16/2015.
 */
public class BuddyActivity extends AppCompatActivity implements View.OnClickListener {

    private final int TAB_NEWS = 0x001;
    private final int TAB_ATTENTION = 0x002;
    private final int TAB_MOOD = 0x003;
    private final int TAB_MORE = 0x004;

    private int current_tab;

    private NewsFragment news_fragment;
    private AttentionFragment attention_fragment;
    private MoodFragment mood_fragment;
    private MoreFragment more_fragment;

    private View tab_news_layout;
    private View tab_attention_layout;
    private View tab_mood_layout;
    private View tab_more_layout;

    private AlertDialog category_list;
    private AlertDialog post_dialog;
    private EditText content_input;

    private int selected_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);

        initComponents();

        setTabSelection(TAB_NEWS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BuddyActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getResources().getString(R.string.key_tab), current_tab);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BuddyActivity.this);
        setTabSelection(preferences.getInt(getResources().getString(R.string.key_tab), TAB_NEWS));
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_buddy);
        }

        //initialize bottom tabs
        tab_news_layout = findViewById(R.id.tab_news_layout);
        tab_attention_layout = findViewById(R.id.tab_attention_layout);
        tab_mood_layout = findViewById(R.id.tab_mood_layout);
        tab_more_layout = findViewById(R.id.tab_more_layout);
        tab_news_layout.setOnClickListener(this);
        tab_attention_layout.setOnClickListener(this);
        tab_mood_layout.setOnClickListener(this);
        tab_more_layout.setOnClickListener(this);

        View btn_create_post = findViewById(R.id.btn_create_post);
        btn_create_post.setOnClickListener(this);

        createDialogs();
    }

    private void createDialogs() {
        //create category list
        AlertDialog.Builder category_builder = new AlertDialog.Builder(this);
        category_builder.setTitle(R.string.msg_category)
                .setItems(R.array.category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPostDialog(which);
                    }
                });
        category_list = category_builder.create();
        //create post dialog
        AlertDialog.Builder post_builder = new AlertDialog.Builder(this);
        post_builder.setTitle(R.string.msg_say_something);
        content_input = new EditText(this);
        post_builder.setView(content_input);
        post_builder.setPositiveButton(getResources().getString(R.string.label_post), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tryPost();
            }
        });
        post_builder.setNegativeButton(getResources().getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        post_dialog = post_builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_news_layout:
                setTabSelection(TAB_NEWS);
                break;
            case R.id.tab_attention_layout:
                setTabSelection(TAB_ATTENTION);
                break;
            case R.id.tab_mood_layout:
                setTabSelection(TAB_MOOD);
                break;
            case R.id.tab_more_layout:
                setTabSelection(TAB_MORE);
                break;
            case R.id.btn_create_post:
                category_list.show();
                break;
        }
    }

    private void setTabSelection(int index) {
        clearTabSelection();
        //begin a new fragment transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //hide all the fragments in case multiple fragments are shown at the same time
        hideFragments(transaction);
        switch (index) {
            case TAB_NEWS:
                //change the news tab background to checked
                tab_news_layout.setBackgroundResource(R.drawable.background_tab_checked);
                //show the news fragment
                if (news_fragment == null) {
                    news_fragment = new NewsFragment();
                    transaction.add(R.id.content, news_fragment);
                } else {
                    transaction.show(news_fragment);
                    tryUpdate();
                }
                updateActionBar(getResources().getString(R.string.tab_news), true);
                current_tab = TAB_NEWS;
                break;
            case TAB_ATTENTION:
                //change the attention tab background to checked
                tab_attention_layout.setBackgroundResource(R.drawable.background_tab_checked);
                //show the attention fragment
                if (attention_fragment == null) {
                    attention_fragment = new AttentionFragment();
                    transaction.add(R.id.content, attention_fragment);
                } else {
                    transaction.show(attention_fragment);
                }
                attention_fragment.tryUpdateAttentionList();
                updateActionBar(getResources().getString(R.string.tab_attention), false);
                current_tab = TAB_ATTENTION;
                break;
            case TAB_MOOD:
                //change the mood tab background to checked
                tab_mood_layout.setBackgroundResource(R.drawable.background_tab_checked);
                //show the mood fragment
                if (mood_fragment == null) {
                    mood_fragment = new MoodFragment();
                    transaction.add(R.id.content, mood_fragment);
                } else {
                    transaction.show(mood_fragment);
                }
                updateActionBar(getResources().getString(R.string.tab_mood), false);
                current_tab = TAB_MOOD;
                break;
            case TAB_MORE:
                //change the more tab background to checked
                tab_more_layout.setBackgroundResource(R.drawable.background_tab_checked);
                //show the more fragment
                if (more_fragment == null) {
                    more_fragment = new MoreFragment();
                    transaction.add(R.id.content, more_fragment);
                } else {
                    transaction.show(more_fragment);
                }
                updateActionBar(getResources().getString(R.string.tab_more), false);
                current_tab = TAB_MORE;
                break;
        }
        transaction.commit();
    }

    private void clearTabSelection() {
        tab_news_layout.setBackgroundResource(R.drawable.background_tab_unchecked);
        tab_attention_layout.setBackgroundResource(R.drawable.background_tab_unchecked);
        tab_mood_layout.setBackgroundResource(R.drawable.background_tab_unchecked);
        tab_more_layout.setBackgroundResource(R.drawable.background_tab_unchecked);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (news_fragment != null) {
            transaction.hide(news_fragment);
        }
        if (attention_fragment != null) {
            transaction.hide(attention_fragment);
        }
        if (mood_fragment != null) {
            transaction.hide(mood_fragment);
        }
        if (more_fragment != null) {
            transaction.hide(more_fragment);
        }
    }

    private void updateActionBar(String title, boolean show_add_btn) {
        ((TextView) findViewById(R.id.abs_title)).setText(title);
        if (show_add_btn) {
            findViewById(R.id.btn_create_post).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btn_create_post).setVisibility(View.INVISIBLE);
        }
    }

    private void showPostDialog(int selected_category) {
        this.selected_category = selected_category;
        content_input.setText("");
        category_list.dismiss();
        post_dialog.show();
    }

    private void tryPost() {
        String content = content_input.getText().toString();
        post_dialog.dismiss();
        PostCreateTask task = new PostCreateTask();
        task.execute(Integer.toString(selected_category), content);
        tryUpdate();
    }

    private void tryUpdate() {
        NewsListTask task = new NewsListTask() {
            @Override
            public void onSuccess(JSONArray result) {
                if (result == null) {
                    Log.d("Error", "Cannot fetch post list");
                } else {
                    news_fragment.updateNews(result);
                }
            }

            @Override
            public void onFail(int error_code) {

            }
        };
        task.execute(0, 10);
    }

}
