package com.njit.buddy.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.njit.buddy.app.network.ResponseCode;
import com.njit.buddy.app.network.task.PasswordChangeTask;
import com.njit.buddy.app.util.PasswordValidator;

/**
 * @author toyknight 4/10/2016.
 */
public class PasswordChangeActivity extends AppCompatActivity {

    private EditText m_old_password;
    private EditText m_new_password;
    private EditText m_new_password_confirm;

    private View progress_view;
    private View password_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        m_old_password = (EditText) findViewById(R.id.m_old_password);
        m_new_password = (EditText) findViewById(R.id.m_new_password);
        m_new_password_confirm = (EditText) findViewById(R.id.m_confirm_password);

        progress_view = findViewById(R.id.password_progress);
        password_form = findViewById(R.id.password_form);

        initComponents();
    }

    @SuppressWarnings("ResourceType")
    private void initComponents() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_back);

            getSupportActionBar().getCustomView().findViewById(R.id.btn_back).setOnClickListener(btn_back_click_listener);
            TextView tv_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title);
            tv_title.setText(getResources().getString(R.string.title_activity_password_change));
        }

        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptChangePassword();
            }
        });
    }

    private void attemptChangePassword() {
        String old_password = m_old_password.getText().toString();
        String new_password = m_new_password.getText().toString();
        String new_password_confirm = m_new_password_confirm.getText().toString();

        PasswordValidator password_validator = new PasswordValidator();
        if (TextUtils.isEmpty(old_password)) {
            m_old_password.setError(getResources().getString(R.string.error_field_required));
            m_old_password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(new_password)) {
            m_new_password.setError(getResources().getString(R.string.error_field_required));
            m_new_password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(new_password_confirm)) {
            m_new_password_confirm.setError(getResources().getString(R.string.error_field_required));
            m_new_password_confirm.requestFocus();
            return;
        }
        if (!password_validator.validate(new_password)) {
            m_new_password.setError(getResources().getString(R.string.error_invalid_password));
            m_new_password.requestFocus();
            return;
        }
        if (!new_password.equals(new_password_confirm)) {
            m_new_password_confirm.setError(getResources().getString(R.string.error_password_mismatch));
            m_new_password_confirm.requestFocus();
            return;
        }
        showProgress(true);
        new PasswordChangeTask() {
            @Override
            public void onSuccess(Integer result) {
                showProgress(false);
                m_old_password.setText("");
                m_new_password.setText("");
                m_new_password_confirm.setText("");
                showToast(getString(R.string.message_password_changed));
            }

            @Override
            public void onFail(int error_code) {
                onPasswordChangeFail(error_code);
            }
        }.execute(old_password, new_password);
    }

    private void onPasswordChangeFail(int error_code) {
        showProgress(false);
        switch (error_code) {
            case ResponseCode.SERVER_ERROR:
                showToast(getString(R.string.message_server_error));
                break;
            case ResponseCode.PASSWORD_OR_EMAIL_MISS_MATCH:
                m_old_password.setError(getString(R.string.error_incorrect_password));
                m_old_password.requestFocus();
                break;
            case ResponseCode.PASSWORD_NOT_VALID:
                m_new_password.setError(getString(R.string.message_invalid_password));
                m_new_password.requestFocus();
                break;
            default:
                showToast(getString(R.string.message_unknown_error));
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Shows the progress UI and hides the register form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            password_form.setVisibility(show ? View.GONE : View.VISIBLE);
            password_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    password_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
            progress_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
            password_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void gotoAccountActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private View.OnClickListener btn_back_click_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            gotoAccountActivity();
        }

    };

}
