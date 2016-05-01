package com.njit.buddy.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.BoolRes;
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
import java.lang.Boolean;
import java.util.Arrays;
import java.util.List;

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

        View btn_posts = findViewById(R.id.btn_posts);
        View btn_edit_description = findViewById(R.id.btn_edit_decription);
        View btn_edit_birthday = findViewById(R.id.btn_edit_birthday);
        View btn_edit_gender = findViewById(R.id.btn_edit_gender);
        View btn_edit_sexuality = findViewById(R.id.btn_edit_sexuality);
        View btn_edit_race = findViewById(R.id.btn_edit_race);

        btn_posts.setOnClickListener(this);
        btn_posts.setOnTouchListener(btn_touch_listener);
        btn_edit_description.setOnClickListener(this);
        btn_edit_birthday.setOnTouchListener(btn_touch_listener);
        btn_edit_birthday.setOnClickListener(this);
        btn_edit_gender.setOnTouchListener(btn_touch_listener);
        btn_edit_gender.setOnClickListener(this);
        btn_edit_sexuality.setOnTouchListener(btn_touch_listener);
        btn_edit_sexuality.setOnClickListener(this);
        btn_edit_race.setOnTouchListener(btn_touch_listener);
        btn_edit_race.setOnClickListener(this);

        ((TextView) findViewById(R.id.tv_username)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_description)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_birthday)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_gender)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_sexuality)).setText(getString(R.string.label_loading));
        ((TextView) findViewById(R.id.tv_race)).setText(getString(R.string.label_loading));
    }

    private void onProfileLoaded(Profile profile) {
        this.profile = profile;
        ((TextView) findViewById(R.id.tv_username)).setText(profile.getUsername());
        String description = profile.getDescription();
        ((TextView) findViewById(R.id.tv_description)).setText(description == null ? "Hidden" : description);
        String birthday = profile.getBirthday();
        ((TextView) findViewById(R.id.tv_birthday)).setText(birthday == null ? "Hidden" : birthday);
        String gender = profile.getGender();
        ((TextView) findViewById(R.id.tv_gender)).setText(description == null ? "Hidden" : gender);
        //if (gender == null) {
          //  ((TextView) findViewById(R.id.tv_gender)).setText("Hidden");
        //} else {
            //String[] genders = gender.split(",");
            //int first = Integer.parseInt(genders[0]);
            //((TextView) findViewById(R.id.tv_gender)).setText(
          //          getResources().getStringArray(R.array.gender)[first]);
        //}
        String sexuality = profile.getSexuality();
        ((TextView) findViewById(R.id.tv_sexuality)).setText(description == null ? "Hidden" : sexuality);
        //if (sexuality == null) {
          //  ((TextView) findViewById(R.id.tv_sexuality)).setText("Hidden");
        //} else {
          //  String[] sexualities = sexuality.split(",");
            //int first = Integer.parseInt(sexualities[0]);
            //((TextView) findViewById(R.id.tv_sexuality)).setText(
              //      getResources().getStringArray(R.array.sexuality)[first]);
        //}
        String race = profile.getRace();
        ((TextView) findViewById(R.id.tv_race)).setText(description == null ? "Hidden" : race);
        //if (race == null) {
          //  ((TextView) findViewById(R.id.tv_race)).setText("Hidden");
        //} else {
          //  String[] races = race.split(",");
            //int first = Integer.parseInt(races[0]);
            //((TextView) findViewById(R.id.tv_race)).setText(
              //      getResources().getStringArray(R.array.race)[first]);
        //}
        findViewById(R.id.btn_edit_decription).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.btn_edit_birthday).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.btn_edit_gender).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.btn_edit_sexuality).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.btn_edit_race).setVisibility(uid == getMyUID() ? View.VISIBLE : View.INVISIBLE);
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
        switch (v.getId()) {
            case R.id.btn_posts:
                gotoUserPostsActivity();
                break;
            case R.id.btn_edit_decription:
                editDescription();
                break;
            case R.id.btn_edit_birthday:
                editBirthday();
                break;
            case R.id.btn_edit_gender:
                editGender();
                break;
            case R.id.btn_edit_sexuality:
                editSexuality();
                break;
            case R.id.btn_edit_race:
                editRace();
                break;
        }
    }

    private void gotoUserPostsActivity() {
        Intent intent = new Intent(this, UserPostsActivity.class);
        intent.putExtra(getString(R.string.key_uid), uid);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void editDescription() {
        if (getMyUID() == uid) {
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
    }

    private void editBirthday() {
        if (getMyUID() == uid) {
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
            dialog_builder.setTitle("Enter your Birthday:");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            input.setText(getProfile().getBirthday());
            dialog_builder.setView(input);

            // Set up the buttons
            dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String birthday = input.getText().toString();
                    tryUpdateBirthday(birthday);
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
    }
    private void editGender() {
        if (getMyUID() == uid) {
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
            dialog_builder.setTitle("Enter your Gender:");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(getProfile().getGender());
            dialog_builder.setView(input);

           /* String[] genders = getResources().getStringArray(R.array.gender);

            final boolean[] checkedGenders = new boolean[]{
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
            };

            final List<String> genderList = Arrays.asList(genders);

            dialog_builder.setMultiChoiceItems(genders, checkedGenders, new DialogInterface.OnMultiChoiceClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface,int which, boolean isChecked){
                    checkedGenders[which]= isChecked;
                    String currentItem = genderList.get(which);

                    Toast.makeText(getApplicationContext(),
                            currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                }
            }
            ); */

            // Set up the buttons
            dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do something when click positive button

                    //tv.setText("Your preferred colors..... \n");
                    //for (int i = 0; i<checkedGenders.length; i++){
                    //  boolean checked = checkedGenders[i];
                    //if (checked) {
                    //  tv.setText(tv.getText() + genderList.get(i) + ", ");
                    //}
                    //}
                    //}
                    String gender = input.getText().toString();
                     tryUpdateGender(gender);
                    //}
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
    }
    private void editSexuality() {
        if (getMyUID() == uid) {
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
            dialog_builder.setTitle("Enter your Sexuality:");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(getProfile().getSexuality());
            dialog_builder.setView(input);

            /*
            String[] sexuality= getResources().getStringArray(R.array.sexuality);

            final boolean[] checkedSexuality = new boolean[]{
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
            };

            final List<String> sexualityList = Arrays.asList(sexuality);

            dialog_builder.setMultiChoiceItems(sexuality, checkedSexuality, new DialogInterface.OnMultiChoiceClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int which, boolean isChecked){
                            checkedSexuality[which]= isChecked;
                            String currentItem = sexualityList.get(which);

                            Toast.makeText(getApplicationContext(),
                                    currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                        }
                    }
            ); */

            // Set up the buttons
            dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               // @Override
                public void onClick(DialogInterface dialog, int which) {
                    String sexuality = input.getText().toString();
                    tryUpdateSexuality(sexuality);
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
    }
    private void editRace() {
        if (getMyUID() == uid) {
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
            dialog_builder.setTitle("Enter your Race:");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(getProfile().getRace());
            dialog_builder.setView(input);

            /*String[] race = new String[]{
                    "White",
                    "Black or African American",
                    "American Indian or Alaskan Native",
                    "Asian",
                    "Native Hawaiian or Other Pacific Islander"
            };

            final boolean[] checkedRace = new boolean[]{
                    false,
                    false,
                    false,
                    false,
                    false
            };

            final List<String> sexualityList = Arrays.asList(race);

            dialog_builder.setMultiChoiceItems(race, checkedRace, new DialogInterface.OnMultiChoiceClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int which, boolean isChecked){
                            checkedRace[which]= isChecked;
                            String currentItem = sexualityList.get(which);

                            Toast.makeText(getApplicationContext(),
                                    currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                        }
                    }
            ); */

            // Set up the buttons
            dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String race = input.getText().toString();
                    tryUpdateRace(race);
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

    private void tryUpdateBirthday(final String birthday) {
        ProfileEditTask task = new ProfileEditTask() {
            @Override
            public void onSuccess(Integer result) {
                getProfile().setBirthday(birthday);
                onProfileLoaded(getProfile());
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
            }
        };
        task.execute(
                getProfile().getUsername(),
                getProfile().getDescription(), getProfile().isDescriptionOpen() ? 1 : 0,
                birthday, getProfile().isBirthdayOpen() ? 1 : 0,
                getProfile().getGender(), getProfile().isGenderOpen() ? 1 : 0,
                getProfile().getSexuality(), getProfile().isSexualityOpen() ? 1 : 0,
                getProfile().getRace(), getProfile().isRaceOpen() ? 1 : 0);
    }

    private void tryUpdateGender(final String gender) {
        ProfileEditTask task = new ProfileEditTask() {
            @Override
            public void onSuccess(Integer result) {
                getProfile().setGender(gender);
                onProfileLoaded(getProfile());
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
            }
        };
        task.execute(
                getProfile().getUsername(),
                getProfile().getDescription(), getProfile().isDescriptionOpen() ? 1 : 0,
                getProfile().getBirthday(), getProfile().isBirthdayOpen() ? 1 : 0,
                gender, getProfile().isGenderOpen() ? 1 : 0,
                getProfile().getSexuality(), getProfile().isSexualityOpen() ? 1 : 0,
                getProfile().getRace(), getProfile().isRaceOpen() ? 1 : 0);
    }
    private void tryUpdateSexuality(final String sexuality) {
        ProfileEditTask task = new ProfileEditTask() {
            @Override
            public void onSuccess(Integer result) {
                getProfile().setSexuality(sexuality);
                onProfileLoaded(getProfile());
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
            }
        };
        task.execute(
                getProfile().getUsername(),
                getProfile().getDescription(), getProfile().isDescriptionOpen() ? 1 : 0,
                getProfile().getBirthday(), getProfile().isBirthdayOpen() ? 1 : 0,
                getProfile().getGender(), getProfile().isGenderOpen() ? 1 : 0,
                sexuality, getProfile().isSexualityOpen() ? 1 : 0,
                getProfile().getRace(), getProfile().isRaceOpen() ? 1 : 0);
    }
    private void tryUpdateRace(final String race) {
        ProfileEditTask task = new ProfileEditTask() {
            @Override
            public void onSuccess(Integer result) {
                getProfile().setRace(race);
                onProfileLoaded(getProfile());
            }

            @Override
            public void onFail(int error_code) {
                onOperationFail(error_code);
            }
        };
        task.execute(
                getProfile().getUsername(),
                getProfile().getDescription(), getProfile().isDescriptionOpen() ? 1 : 0,
                getProfile().getBirthday(), getProfile().isBirthdayOpen() ? 1 : 0,
                getProfile().getGender(), getProfile().isGenderOpen() ? 1 : 0,
                getProfile().getSexuality(), getProfile().isSexualityOpen() ? 1 : 0,
                race, getProfile().isRaceOpen() ? 1 : 0);
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
