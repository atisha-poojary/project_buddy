package com.njit.buddy.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.ProfileActivity;
import com.njit.buddy.app.R;
import com.njit.buddy.app.entity.Hug;

/**
 * @author toyknight 11/23/2015.
 */
public class HugView extends LinearLayout {

    private Hug hug_data;

    public HugView(Context context) {
        this(context, null);
    }

    public HugView(Context context, Hug hug_data) {
        super(context);
        this.hug_data = hug_data;
        View.inflate(getContext(), R.layout.view_hug, this);

        findViewById(R.id.btn_hugger).setOnTouchListener(new OnTouchListener() {
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

        });
        findViewById(R.id.btn_hugger).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileActivity();
            }
        });
        updateView();
    }

    public Hug getHugData() {
        return hug_data;
    }

    public void updateView() {
        TextView tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(getHugData().getUsername());
        findViewById(R.id.btn_hug_back).setVisibility(INVISIBLE);
    }

    private void gotoProfileActivity() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(getContext().getString(R.string.key_uid), getHugData().getUID());
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
