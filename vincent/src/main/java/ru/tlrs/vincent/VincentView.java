package ru.tlrs.vincent;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

public class VincentView extends ImageView implements View.OnClickListener{

    private Context mContext;

    private String mImgUri;
    private boolean mPinch2Zoom;
    private boolean mOverlayMode;
    private int mGroupId;
    private int mSrcId;

    private static final String LOG_TAG = "VincentView";

    public VincentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //TypedArray a = context.obtainStyledAttributes(
        //        attrs, com.android.internal.R.styleable.ImageView, 0, 0);
        //mSrcId = a.getInt(com.android.internal.R.styleable.ImageView_src, 0);
        //a.recycle();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VincentView, 0, 0);
        try {
            mImgUri = a.getString(R.styleable.VincentView_imageUri);
            mOverlayMode = a.getBoolean(R.styleable.VincentView_overlayMode, true);
            mPinch2Zoom = !mOverlayMode && a.getBoolean(R.styleable.VincentView_pinch2zoomEnable, true);
            mGroupId = a.getInt(R.styleable.VincentView_groupId, 0);
        } finally {
            a.recycle();
        }
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showLightBox(mContext);
    }

    private void showLightBox(Context context) {
        LightBox mLightBox = LightBox.getInstance();
        mLightBox.setArguments(fields());
        mLightBox.show(getContextFragmentManager(context), "LightBox");
    }

    private FragmentManager getContextFragmentManager(Context context) {
        FragmentManager result = null;
        try{
            Method method = context.getClass().getMethod("getSupportFragmentManager");
            try {
                result = (FragmentManager) method.invoke(context);
            }catch (IllegalAccessException e){
                Log.d(LOG_TAG, "getContextFragmentManager: IllegalAccessException");
            }catch (InvocationTargetException e){
                Log.d(LOG_TAG, "getContextFragmentManager: InvocationTargetException");
            }
        }catch (NoSuchMethodException e){
            Log.d(LOG_TAG, "showLightBox: Method getSupportFragmentManager() is not exist. Parent view class mast be derive from Compat classes.");
        }
        return result;
    }

    private Bundle fields(){
        Bundle result = new Bundle();
        result.putBoolean(LightBox.ViewAttr.OVERLAY_MODE.getKey(), mOverlayMode);
        result.putBoolean(LightBox.ViewAttr.P2Z_MODE.getKey(), mPinch2Zoom);
        result.putString(LightBox.ViewAttr.URI.getKey(), mImgUri);
        result.putInt(LightBox.ViewAttr.GROUP_ID.getKey(), mGroupId);
        result.putString(LightBox.ViewAttr.DESC.getKey(), getContentDescription().toString());
        return result;
    }

    public String getImgUri() {
        return mImgUri;
    }

    public void setImgUri(String mImgUri) {
        this.mImgUri = mImgUri;
    }

    public boolean isPinch2Zoom() {
        return mPinch2Zoom;
    }

    public void setPinch2Zoom(boolean mPinch2Zoom) {
        this.mPinch2Zoom = mPinch2Zoom;
    }

    public boolean isOverlayMode() {
        return mOverlayMode;
    }

    public void setOverlayMode(boolean mOverlayMode) {
        this.mOverlayMode = mOverlayMode;
    }

    public int getImgGroupId() {
        return mGroupId;
    }

    public void setImgGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }


}
