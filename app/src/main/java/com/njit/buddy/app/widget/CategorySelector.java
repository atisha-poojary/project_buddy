package com.njit.buddy.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.njit.buddy.app.R;

/**
 * @author toyknight 2/11/2016.
 */
public class CategorySelector extends LinearLayout {

    private String[] content;

    private int current_index;

    private CategorySelectorListener listener;

    public CategorySelector(Context context) {
        super(context);
        initialize();
    }

    public CategorySelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CategorySelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        View.inflate(getContext(), R.layout.view_category_selector, this);
        //load categories
        String[] categories = getResources().getStringArray(R.array.category);
        content = new String[categories.length + 1];
        content[0] = getResources().getString(R.string.label_all);
        System.arraycopy(categories, 0, content, 1, categories.length);
        //initialize
        findViewById(R.id.btn_previous).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousButtonClicked();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
        current_index = 0;
        update();
    }

    public void setCategorySelectorListener(CategorySelectorListener listener) {
        this.listener = listener;
    }

    public void reset() {
        current_index = 0;
        update();
    }

    private void onPreviousButtonClicked() {
        if (current_index > 0) {
            current_index--;
        } else {
            current_index = content.length - 1;
        }
        update();
    }

    private void onNextButtonClicked() {
        if (current_index < content.length - 1) {
            current_index++;
        } else {
            current_index = 0;
        }
        update();
    }

    public void update() {
        TextView text_category = (TextView) findViewById(R.id.text_categoty);
        text_category.setText(content[current_index]);
        fireCategoryChangeEvent();
    }

    public int getCurrentCategoryIndex() {
        return current_index - 1;
    }

    private void fireCategoryChangeEvent() {
        if (listener != null) {
            listener.onSelectedCategoryChange();
        }
    }

}
