package com.njit.buddy.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.njit.buddy.app.entity.Profile;
import com.njit.buddy.app.network.Connector;
import com.njit.buddy.app.network.task.ProfileViewTask;

/**
 * @author toyknight 8/16/2015.
 */
public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initComponents();
        ProfileViewTask task = new ProfileViewTask() {
            @Override
            public void onSuccess(Profile result) {
                ((TextView) findViewById(R.id.tv_username)).setText(result.getUsername());
            }

            @Override
            public void onFail(int error_code) {
            }
        };
        task.execute(getUID());
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_account));
        }

        Button btn_change_password = (Button) findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPasswordChangeActivity();
            }
        });

        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        ((TextView) findViewById(R.id.tv_username)).setText(getString(R.string.label_loading));
    }

    private void gotoBuddyActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void gotoPasswordChangeActivity() {
        Intent intent = new Intent(this, PasswordChangeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private int getUID() {
        SharedPreferences preferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        return preferences.getInt(getResources().getString(R.string.key_uid), 0);
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getResources().getString(R.string.key_authorization));
        editor.remove(getResources().getString(R.string.key_tab));
        editor.remove(getResources().getString(R.string.key_uid));
        editor.apply();
        Connector.setAuthorization(null);
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            gotoBuddyActivity();
        }

    };

}
