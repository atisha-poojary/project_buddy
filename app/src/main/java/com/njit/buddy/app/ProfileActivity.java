package com.njit.buddy.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.njit.buddy.app.entity.Profile;
import com.njit.buddy.app.network.ResponseCode;
import com.njit.buddy.app.network.task.ProfileEditTask;
import com.njit.buddy.app.network.task.ProfileViewTask;

/**
 * @author toyknight 8/16/2015.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private int uid;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponents();
        uid = getIntent().getIntExtra(getString(R.string.key_uid), 0);
        ProfileViewTask task = new ProfileViewTask() {
            @Override
            public void onSuccess(Profile result) {
                onProfileLoaded(result);
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
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

        View btn_edit_description = findViewById(R.id.btn_edit_decription);
        View btn_birthday = findViewById(R.id.btn_birthday);
        View btn_gender = findViewById(R.id.btn_sex);
        View btn_sexuality = findViewById(R.id.btn_sexuality);
        View btn_race = findViewById(R.id.btn_race);

        btn_edit_description.setOnClickListener(this);
        btn_birthday.setOnTouchListener(btn_touch_listener);
        btn_birthday.setOnClickListener(this);
        btn_gender.setOnTouchListener(btn_touch_listener);
        btn_gender.setOnClickListener(this);
        btn_sexuality.setOnTouchListener(btn_touch_listener);
        btn_sexuality.setOnClickListener(this);
        btn_race.setOnTouchListener(btn_touch_listener);
        btn_race.setOnClickListener(this);

        //temporarily hide these buttons
        btn_birthday.setVisibility(View.INVISIBLE);
        btn_gender.setVisibility(View.INVISIBLE);
        btn_sexuality.setVisibility(View.INVISIBLE);
        btn_race.setVisibility(View.INVISIBLE);

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
        String description = profile.getDescription();
        ((TextView) findViewById(R.id.tv_description)).setText(description == null ? "Hidden" : description);
        String birthday = profile.getBirthday();
        ((TextView) findViewById(R.id.tv_birthday_value)).setText(birthday == null ? "Hidden" : birthday);
        String gender = profile.getGender();
        if (gender == null) {
            ((TextView) findViewById(R.id.tv_gender_value)).setText("Hidden");
        } else {
            String[] genders = gender.split(",");
            int first = Integer.parseInt(genders[0]);
            ((TextView) findViewById(R.id.tv_gender_value)).setText(
                    getResources().getStringArray(R.array.gender)[first]);
        }
        String sexuality = profile.getSexuality();
        if (sexuality == null) {
            ((TextView) findViewById(R.id.tv_sexuality_value)).setText("Hidden");
        } else {
            String[] sexualities = sexuality.split(",");
            int first = Integer.parseInt(sexualities[0]);
            ((TextView) findViewById(R.id.tv_sexuality_value)).setText(
                    getResources().getStringArray(R.array.sexuality)[first]);
        }
        String race = profile.getGender();
        if (race == null) {
            ((TextView) findViewById(R.id.tv_race_value)).setText("Hidden");
        } else {
            String[] races = race.split(",");
            int first = Integer.parseInt(races[0]);
            ((TextView) findViewById(R.id.tv_race_value)).setText(
                    getResources().getStringArray(R.array.race)[first]);
        }
        findViewById(R.id.btn_edit_decription).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
    }

    private void onOperationFail(int error_code) {
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

    private int getMyUID() {
        SharedPreferences preferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        return preferences.getInt(getResources().getString(R.string.key_uid), 0);
    }

    @Override
    public void onClick(View v) {
        if (getMyUID() == uid) {
            switch (v.getId()) {
                case R.id.btn_edit_decription:
                    editDescription();
                    break;
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
    }

    private void editDescription() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        dialog_builder.setTitle("Input your self-introduction");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(getProfile().getDescription());
        dialog_builder.setView(input);

        // Set up the buttons
        dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = input.getText().toString();
                tryUpdateDescription(description);
            }
        });
        dialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog_builder.show();
    }

    private void tryUpdateDescription(final String description) {
        ProfileEditTask task = new ProfileEditTask() {
            @Override
            public void onSuccess(Integer result) {
                getProfile().setDescription(description);
                onProfileLoaded(getProfile());
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
            }
        };
        task.execute(
                getProfile().getUsername(),
                description, getProfile().isDescriptionOpen() ? 1 : 0,
                getProfile().getBirthday(), getProfile().isBirthdayOpen() ? 1 : 0,
                getProfile().getGender(), getProfile().isGenderOpen() ? 1 : 0,
                getProfile().getSexuality(), getProfile().isSexualityOpen() ? 1 : 0,
                getProfile().getRace(), getProfile().isRaceOpen() ? 1 : 0);
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
