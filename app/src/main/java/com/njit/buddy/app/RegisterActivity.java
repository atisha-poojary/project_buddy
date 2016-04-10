package com.njit.buddy.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.njit.buddy.app.network.ResponseCode;
import com.njit.buddy.app.network.task.RegisterTask;
import com.njit.buddy.app.util.EmailValidator;
import com.njit.buddy.app.util.PasswordValidator;

/**
 * @author toyknight 11/1/2015.
 */
public class RegisterActivity extends Activity {

    private EditText m_email;
    private EditText m_username;
    private EditText m_password;
    private EditText m_password_confirm;

    private View progress_view;
    private View register_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        m_email = (EditText) findViewById(R.id.et_email);
        m_username = (EditText) findViewById(R.id.et_username);
        m_password = (EditText) findViewById(R.id.et_password);
        m_password_confirm = (EditText) findViewById(R.id.et_password_confirm);

        progress_view = findViewById(R.id.register_progress);
        register_form = findViewById(R.id.register_form);

        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginPage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        gotoLoginPage();
    }

    public void attemptRegister() {
        String email = m_email.getText().toString();
        String username = m_username.getText().toString();
        String password = m_password.getText().toString();
        String password_confirm = m_password_confirm.getText().toString();

        EmailValidator email_validator = new EmailValidator();
        PasswordValidator password_validator = new PasswordValidator();
        if (TextUtils.isEmpty(email)) {
            m_email.setError(getResources().getString(R.string.error_field_required));
            m_email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            m_username.setError(getResources().getString(R.string.error_field_required));
            m_username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            m_password.setError(getResources().getString(R.string.error_field_required));
            m_password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password_confirm)) {
            m_password_confirm.setError(getResources().getString(R.string.error_field_required));
            m_password_confirm.requestFocus();
            return;
        }
        if (!email_validator.validate(email)) {
            m_email.setError(getResources().getString(R.string.error_invalid_email));
            m_email.requestFocus();
            return;
        }
        if (!password_validator.validate(password)) {
            m_password.setError(getResources().getString(R.string.error_invalid_password));
            m_password.requestFocus();
            return;
        }
        if (!password.equals(password_confirm)) {
            m_password_confirm.setError(getResources().getString(R.string.error_password_mismatch));
            m_password_confirm.requestFocus();
            return;
        }
        showProgress(true);
        new RegisterTask() {
            @Override
            public void onSuccess(Integer result) {
                showProgress(false);
                gotoLoginPageAndLogin();
            }

            @Override
            public void onFail(int error_code) {
                onRegistrationFail(error_code);
            }
        }.execute(email, username, password);
    }

    public void gotoLoginPageAndLogin() {
        String email = m_email.getText().toString();
        String password = m_password.getText().toString();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }

    public void gotoLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onRegistrationFail(int error_code) {
        showProgress(false);
        switch (error_code) {
            case ResponseCode.SERVER_ERROR:
                showToast(getString(R.string.message_server_error));
                break;
            case ResponseCode.EMAIL_NOT_AVAILABLE:
                m_email.setError(getString(R.string.message_email_unavailable));
                m_email.requestFocus();
                break;
            case ResponseCode.EMAIL_NOT_VALID:
                m_email.setError(getString(R.string.message_invalid_email));
                m_email.requestFocus();
                break;
            case ResponseCode.PASSWORD_NOT_VALID:
                m_password.setError(getString(R.string.message_invalid_password));
                m_password.requestFocus();
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

            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
            register_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    register_form.setVisibility(show ? View.GONE : View.VISIBLE);
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
            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
