package com.njit.buddy.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.ProfileActivity;
import com.njit.buddy.app.R;
import com.njit.buddy.app.entity.Comment;
import com.njit.buddy.app.util.DateParser;

/**
 * @author toyknight 3/5/2016.
 */
public class CommentView extends LinearLayout {

    private Comment comment;

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, Comment comment) {
        super(context);
        this.comment = comment;
        View.inflate(getContext(), R.layout.view_comment, this);
        initialize();
    }

    public Comment getComment() {
        return comment;
    }

    private void initialize() {
        String username = getComment().getUsername();
        long timestamp = getComment().getTimestamp();
        String content = getComment().getContent();
        String comment_info = String.format("%s [%s]", username, DateParser.toString(timestamp));
        ((TextView) findViewById(R.id.tv_comment_info)).setText(comment_info);
        ((TextView) findViewById(R.id.tb_comment_content)).setText(content);
        findViewById(R.id.tv_comment_info).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileActivity();
            }
        });
    }

    private void gotoProfileActivity() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(getContext().getString(R.string.key_uid), getComment().getUID());
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
