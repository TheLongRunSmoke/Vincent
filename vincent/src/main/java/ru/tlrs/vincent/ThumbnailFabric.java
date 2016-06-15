package ru.tlrs.vincent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

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

public class ThumbnailFabric {

    public static byte[] makeFrom(String uri) {
        byte[] imageData = null;
        try {
            final int THUMBNAIL_SIZE = 64;
            FileInputStream fis = new FileInputStream(uri);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
            imageBitmap.recycle();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
        } catch (Exception ex) {
            return imageData;
        }
        return imageData;
    }


}
