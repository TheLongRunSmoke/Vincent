package ru.tlrs.vincent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Copyright (C) 2016 Alexander Varakosov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public final class LightBox extends AppCompatDialogFragment {

    static final String TAG = LightBox.class.getSimpleName();

    private static volatile LightBox instance;

    private View view;

    private int parentId;
    private Boolean isOverlay = null;
    private boolean isPitch2Zoom;
    private String imgUri;
    private int groupId;
    private String mDesc;

    enum ViewAttr{
        PARENT_ID,
        OVERLAY_MODE,
        P2Z_MODE,
        URI,
        GROUP_ID,
        DESC
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
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.light_box, container);
        if (isOverlay) {
            getDialog().setCanceledOnTouchOutside(true);
        }
        Log.d(TAG, "onCreateView: " + view.findViewById(R.id.fullImage).getMeasuredWidth() +" : "+view.findViewById(R.id.fullImage).getMeasuredHeight());
        ((ImageView)view.findViewById(R.id.fullImage)).setImageDrawable(((ImageView)getActivity().findViewById(parentId)).getDrawable());
        ((TextView)view.findViewById(R.id.description)).setText(mDesc);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        //Log.d(TAG, "onViewCreated: " + view.findViewById(R.id.fullImage).getMeasuredWidth() +" : "+view.findViewById(R.id.fullImage).getMeasuredHeight());
        //((ImageView)view.findViewById(R.id.fullImage)).setImageDrawable(((ImageView)getActivity().findViewById(parentId)).getDrawable());
        //((TextView)view.findViewById(R.id.description)).setText(mDesc);
    }

    private void getFields(Bundle bundle) {
        parentId = bundle.getInt(ViewAttr.PARENT_ID.name(), 0);
        isOverlay = bundle.getBoolean(ViewAttr.OVERLAY_MODE.name(), false);
        isPitch2Zoom = bundle.getBoolean(ViewAttr.P2Z_MODE.name(), false);
        imgUri = bundle.getString(ViewAttr.URI.name());
        groupId = bundle.getInt(ViewAttr.GROUP_ID.name());
        mDesc = bundle.getString(ViewAttr.DESC.name());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean prevOverlay = isOverlay;
        getFields(getArguments());
        if (prevOverlay != isOverlay || prevOverlay == null) {
            if (isOverlay) {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Vincent_Dialog_Overlay);
            } else {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Vincent_Dialog_Modal);
            }
        }
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss: ");
        System.gc();
        super.onDismiss(dialog);
    }
}
