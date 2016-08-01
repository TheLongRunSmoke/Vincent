package ru.tlrs.vincent;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Copyright (C) 2016 Alexander Varakosov.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
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

    private String mSrc;

    private Boolean prevOverlay;

    enum ViewAttr {
        PARENT_ID,
        OVERLAY_MODE,
        P2Z_MODE,
        URI,
        GROUP_ID,
        DESC,
        SRC
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        inflateView(inflater, container);
        final ImageView fullImage = (ImageView) view.findViewById(R.id.fullImage);
        fullImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fullImage.getViewTreeObserver().removeOnPreDrawListener(this);
                /*int height = fullImage.getMeasuredHeight();
                int width = fullImage.getMeasuredWidth();
                Log.d(TAG, "onPreDraw: " + String.format("height: %s, width: %s", height, width) );
                //Rect view = new Rect(0,0,width, height);
                Drawable drawable = ((ImageView) getActivity().findViewById(parentId)).getDrawable();
                drawable = drawable.mutate();
                //Rect draw = drawable.getBounds();
                int h = drawable.getIntrinsicHeight();
                int w = drawable.getIntrinsicWidth();
                //drawable.setBounds(0,0,w,h);
                drawable.setBounds(new Rect(0,0,width,height));
                //ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1.0f ,1.0f );
                fullImage.setImageDrawable(drawable);*/
                fullImage.setImageURI(null);
                fullImage.setImageURI(Uri.parse("resource://"+mSrc));
                return false;
            }
        });

        //((ImageView) view.findViewById(R.id.fullImage)).setImageDrawable(((ImageView) getActivity().findViewById(parentId)).getDrawable());
        ((TextView) view.findViewById(R.id.description)).setText(mDesc);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
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
        mSrc = bundle.getString(ViewAttr.SRC.name());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prevOverlay = isOverlay;
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

    private void inflateView(LayoutInflater inflater, ViewGroup container) {
        long start = System.nanoTime();
        if (isOverlay) {
            view = inflater.inflate(R.layout.overlay, container);
            getDialog().setCanceledOnTouchOutside(true);
        } else {
            view = inflater.inflate(R.layout.modal, container);
        }
        long elapsedTime = System.nanoTime() - start;
        Log.d(TAG, "onCreateView: time = " + elapsedTime / 1000 + " us");
    }
}
