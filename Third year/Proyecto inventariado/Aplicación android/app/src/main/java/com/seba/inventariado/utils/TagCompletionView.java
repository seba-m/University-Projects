package com.seba.inventariado.utils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seba.inventariado.R;
import com.seba.inventariado.model.Tag;
import com.tokenautocomplete.TokenCompleteTextView;

public class TagCompletionView extends TokenCompleteTextView<Tag> {

    public TagCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Tag tag) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) l.inflate(R.layout.tag_token, (ViewGroup) TagCompletionView.this.getParent(), false);
        ((TextView) view.findViewById(R.id.name)).setText(tag.getNombre());

        return view;
    }

    @Override
    public boolean shouldIgnoreToken(Tag token) {

        for (Tag tag : getObjects()) {
            if (tag.getNombre().equals(token.getNombre())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Tag defaultObject(String completionText) {
        return null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            final TagCompletionView tagCompletionView = (TagCompletionView) v;
            tagCompletionView.performCompletion();
            return true;
        }
        return false;
    }
}
