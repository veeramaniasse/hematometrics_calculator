package com.simats.hematometrics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;

public class Utils {

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        // Get the minimum dimension (width or height) of the original bitmap
        int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        // Create a new Bitmap object with the same minimum dimension for width and height
        Bitmap output = Bitmap.createBitmap(minSize, minSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, minSize, minSize);
        final RectF rectF = new RectF(rect);
        final float roundPx = minSize / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Calculate the left-top coordinates for cropping
        int left = (bitmap.getWidth() - minSize) / 2;
        int top = (bitmap.getHeight() - minSize) / 2;
        // Create a rect for center-cropping
        Rect srcRect = new Rect(left, top, left + minSize, top + minSize);
        // Draw the cropped bitmap onto the circular canvas
        canvas.drawBitmap(bitmap, srcRect, rect, paint);

        return output;
    }

    public static Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
