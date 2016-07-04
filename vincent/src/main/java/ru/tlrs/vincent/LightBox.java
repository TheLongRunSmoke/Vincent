package ru.tlrs.vincent;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Copyright (C) 2016 Alexander Varakosov.
 *
 * Licensed under the MIT License(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://github.com/TheLongRunSmoke/vincent/blob/master/LICENSE
 *
 */

public final class LightBox extends AppCompatDialogFragment {

    static final String TAG = LightBox.class.getSimpleName();

    private static volatile LightBox instance;

    private static final String LOG_TAG = "LightBox";

    private int parentId;
    private boolean isOverlay;
    private boolean isPitch2Zoom;
    private String imgUri;
    private int groupId;
    private String mDesc;

    enum Mode {
        NULL,
        MODAL,
        OVERLAY
    }

    enum ViewAttr{
        PARENT_ID("id"),
        OVERLAY_MODE("isOverlayMode"),
        P2Z_MODE("isPitch2Zoom"),
        URI("imgUri"),
        GROUP_ID("groupId"),
        DESC("desc");

        private final String mKey;

        ViewAttr(String key){
            mKey = key;
        }

        public String getKey(){
            return mKey;
        }
    }

    public static LightBox getInstance() {
        LightBox localInstance = instance;
        if (localInstance == null) {
            synchronized (LightBox.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LightBox();
                }
            }
        }
        return localInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(LOG_TAG, "onCreateView: ");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getFields(getArguments());
        return createView(isOverlay, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageView)view.findViewById(R.id.fullImage)).setImageDrawable(((ImageView)getActivity().findViewById(parentId)).getDrawable());
        ((TextView)view.findViewById(R.id.description)).setText(mDesc);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    private void getFields(Bundle bundle) {
        parentId = bundle.getInt(ViewAttr.PARENT_ID.getKey(), 0);
        isOverlay = bundle.getBoolean(ViewAttr.OVERLAY_MODE.getKey(), false);
        isPitch2Zoom = bundle.getBoolean(ViewAttr.P2Z_MODE.getKey(), false);
        imgUri = bundle.getString(ViewAttr.URI.getKey());
        groupId = bundle.getInt(ViewAttr.GROUP_ID.getKey());
        mDesc = bundle.getString(ViewAttr.DESC.getKey());
        Log.d(TAG, "getFields: " + imgUri);
        Log.d(TAG, "getFields: " + mDesc);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        Log.d(LOG_TAG, "onCreate: ");
    }

    public View createView(boolean isOverlay, ViewGroup container) {
        View result;
        if (isOverlay){
            result = View.inflate(this.getActivity(), R.layout.overlay, container);
        }else{
            result = View.inflate(this.getActivity(), R.layout.modal, container);
        }
        return result;
    }

    public Mode getCurrentMode(ViewGroup container) {
        try {
            if (container.findViewById(R.id.modal) != null){
                return Mode.MODAL;
            }else{
                return Mode.OVERLAY;
            }
        }catch (NullPointerException e){
            return Mode.NULL;
        }
    }

}
