package com.seba.inventariado.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seba.inventariado.R;

import org.apache.commons.lang3.StringUtils;

public class ValueSelector extends RelativeLayout {

    private long minValue = -999_999_999L;
    private long maxValue = 999_999_999L;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 100L;

    View rootView;
    EditText valueEditText;
    TextView titleTextView;
    View minusButton;
    View plusButton;

    Handler handler = new Handler();

    public ValueSelector(Context context) {
        super(context);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public long getValue() {
        return Long.parseLong(valueEditText.getText().toString());
    }

    public EditText getValueEditText() {
        return this.valueEditText;
    }

    public void setValue(long newValue) {
        long value = newValue;
        if (newValue < minValue) {
            value = minValue;
        } else if (newValue > maxValue) {
            value = maxValue;
        }

        valueEditText.setText(String.valueOf(value));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        rootView = inflate(context, R.layout.value_selector, this);
        valueEditText = rootView.findViewById(R.id.valueTextView);
        titleTextView = rootView.findViewById(R.id.valueLabel);
        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(v -> decrementValue());
        minusButton.setOnLongClickListener(
                arg0 -> {
                    minusButtonIsPressed = true;
                    handler.post(new AutoDecrementer());
                    return false;
                }
        );
        minusButton.setOnTouchListener((v, event) -> {
            if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                minusButtonIsPressed = false;
            }
            return false;
        });

        plusButton.setOnClickListener(v -> incrementValue());
        plusButton.setOnLongClickListener(
                arg0 -> {
                    plusButtonIsPressed = true;
                    handler.post(new AutoIncrementer());
                    return false;
                }
        );

        plusButton.setOnTouchListener((v, event) -> {
            if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                plusButtonIsPressed = false;
            }
            return false;
        });
    }

    private void incrementValue() {
        String currValString = valueEditText.getText().toString();
        if (!StringUtils.isBlank(currValString)) {
            long currentVal = Long.parseLong(currValString);
            if (currentVal < maxValue) {
                valueEditText.setText(String.valueOf(currentVal + 1));
            }
        } else {
            valueEditText.setText("1");
        }
    }

    private void decrementValue() {
        String currValString = valueEditText.getText().toString();
        if (!StringUtils.isBlank(currValString)) {
            long currentVal = Long.parseLong(currValString);
            if (currentVal > minValue) {
                valueEditText.setText(String.valueOf(currentVal - 1));
            }
        } else {
            valueEditText.setText("-1");
        }
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run() {
            if (plusButtonIsPressed) {
                incrementValue();
                handler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private class AutoDecrementer implements Runnable {
        @Override
        public void run() {
            if (minusButtonIsPressed) {
                decrementValue();
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }
}