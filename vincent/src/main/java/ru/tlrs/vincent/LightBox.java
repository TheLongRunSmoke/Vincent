package ru.tlrs.vincent;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private static volatile LightBox instance;

    private static final String LOG_TAG = "LightBox";

    private boolean isOverlay;
    private boolean isPitch2Zoom;
    private int imgUri;
    private int groupId;
    private String mDesc;

    enum Mode {
        NULL,
        MODAL,
        OVERLAY
    }

    enum ViewAttr{
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
        getFields(getArguments());
        ((ImageView)container.findViewById(R.id.fullImage)).setImageResource(imgUri);
        ((TextView)container.findViewById(R.id.description)).setText(mDesc);
        return createView(isOverlay, container);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    private void getFields(Bundle bundle) {
        isOverlay = bundle.getBoolean(ViewAttr.OVERLAY_MODE.getKey(), false);
        isPitch2Zoom = bundle.getBoolean(ViewAttr.P2Z_MODE.getKey(), false);
        imgUri = bundle.getInt(ViewAttr.URI.getKey());
        groupId = bundle.getInt(ViewAttr.GROUP_ID.getKey());
        mDesc = bundle.getString(ViewAttr.DESC.getKey());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
