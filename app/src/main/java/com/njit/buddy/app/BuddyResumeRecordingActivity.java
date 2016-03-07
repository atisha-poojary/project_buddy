package com.njit.buddy.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.njit.buddy.app.network.task.BuddyUseTask;

/**
 * @author toyknight 3/6/2016.
 */
public class BuddyResumeRecordingActivity extends AppCompatActivity {

    private boolean intent = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (!intent) {
            new BuddyUseTask().execute();
        }
        intent = false;
    }

    @Override
    public void startActivity(Intent intent) {
        this.intent = true;
        super.startActivity(intent);
    }

}
