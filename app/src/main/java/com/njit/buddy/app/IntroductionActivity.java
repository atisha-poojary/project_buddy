package com.njit.buddy.app;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.app.Activity;


/** Created by Indraneel on 2/22/2016 */

public class IntroductionActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.njit.buddy.app.R.layout.activity_introduction);

        ImageView intro_page = (ImageView) findViewById(com.njit.buddy.app.R.id.intro_page);
        intro_page.setImageResource(com.njit.buddy.app.R.drawable.ic_introduction);

        intro_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoBuddyPage();
            }
        });
    }
        public void gotoBuddyPage() {
            Intent intent = new Intent(this, BuddyActivity.class);
            startActivity(intent);
            //showProgress(false);
            finish();
        }
}