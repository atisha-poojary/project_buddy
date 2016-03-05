package com.njit.buddy.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.njit.buddy.app.entity.Profile;
import com.njit.buddy.app.network.ResponseCode;
import com.njit.buddy.app.network.task.ProfileViewTask;

/**
 * @author toyknight 8/16/2015.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponents();
        int uid = getIntent().getIntExtra(getString(R.string.key_uid), 0);
        ProfileViewTask task = new ProfileViewTask() {
            @Override
            public void onSuccess(Profile result) {
                onProfileLoaded(result);
            }

            @Override
            public void onFail(int error_code) {
                onProfileLoadingFail(error_code);
            }
        };
        task.execute(uid);
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_profile));
        }

        View btn_birthday = findViewById(R.id.btn_birthday);
        View btn_sex = findViewById(R.id.btn_sex);
        View btn_sexuality = findViewById(R.id.btn_sexuality);
        View btn_race = findViewById(R.id.btn_race);

        btn_birthday.setOnTouchListener(btn_touch_listener);
        btn_birthday.setOnClickListener(this);
        btn_sex.setOnTouchListener(btn_touch_listener);
        btn_sex.setOnClickListener(this);
        btn_sexuality.setOnTouchListener(btn_touch_listener);
        btn_sexuality.setOnClickListener(this);
        btn_race.setOnTouchListener(btn_touch_listener);
        btn_race.setOnClickListener(this);

        ((TextView) findViewById(R.id.tv_username)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_description)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_birthday_value)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_gender_value)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_sexuality_value)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_race_value)).setText(getString(R.string.label_loading));
    }

    private void onProfileLoaded(Profile profile) {
        this.profile = profile;
        ((TextView) findViewById(R.id.tv_username)).setText(profile.getUsername());
        ((TextView) findViewById(R.id.tv_description)).setText(profile.getDescription());
        ((TextView) findViewById(R.id.tv_birthday_value)).setText(profile.getBirthday());
        String gender = profile.getGender();
        if (gender.equals("Hidden")) {
            ((TextView) findViewById(R.id.tv_gender_value)).setText(gender);
        } else {
            String[] genders = gender.split(",");
            int first = Integer.parseInt(genders[0]);
            ((TextView) findViewById(R.id.tv_gender_value)).setText(
                    getResources().getStringArray(R.array.gender)[first]);
        }
        String sexuality = profile.getSexuality();
        if (sexuality.equals("Hidden")) {
            ((TextView) findViewById(R.id.tv_sexuality_value)).setText(sexuality);
        } else {
            String[] sexualities = sexuality.split(",");
            int first = Integer.parseInt(sexualities[0]);
            ((TextView) findViewById(R.id.tv_sexuality_value)).setText(
                    getResources().getStringArray(R.array.sexuality)[first]);
        }
        String race = profile.getGender();
        if (race.equals("Hidden")) {
            ((TextView) findViewById(R.id.tv_race_value)).setText(race);
        } else {
            String[] races = race.split(",");
            int first = Integer.parseInt(races[0]);
            ((TextView) findViewById(R.id.tv_race_value)).setText(
                    getResources().getStringArray(R.array.race)[first]);
        }
    }

    private void onProfileLoadingFail(int error_code) {
        switch (error_code) {
            case ResponseCode.USER_NOT_FOUND:
                showToast(getString(R.string.message_user_not_found));
                break;
            case ResponseCode.LOGIN_REQUIRED:
                showToast(getString(R.string.message_login_required));
                break;
            default:
                showToast(getString(R.string.message_unknown_error));
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private Profile getProfile() {
        return profile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_birthday:
                break;
            case R.id.btn_sex:
                break;
            case R.id.btn_sexuality:
                break;
            case R.id.btn_race:
                break;
        }
    }

    private View.OnTouchListener btn_touch_listener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundResource(R.drawable.border_checked);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setBackgroundResource(R.drawable.border_unchecked);
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.setBackgroundResource(R.drawable.border_unchecked);
                    break;
            }
            return false;
        }

    };

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

    };

}
