package com.example.oderingfood.models;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomRunninTextview extends TextView{

    public CustomRunninTextview(Context context) {
        super(context);
    }

    public CustomRunninTextview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRunninTextview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomRunninTextview(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }


}
