package no.hvl.dat153.utils;

import static java.lang.Math.sqrt;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

import no.hvl.dat153.R;
import no.hvl.dat153.model.QuizImage;

public class DatabaseUtils {

    public static QuizImage[] getDefaults(Resources resources) {
        // Default entries
        QuizImage[] quizImages = {
                new QuizImage("cat", BitmapFactory.decodeResource(resources, R.drawable.cat)),
                new QuizImage("dog", BitmapFactory.decodeResource(resources, R.drawable.dog)),
                new QuizImage("rabbit", BitmapFactory.decodeResource(resources, R.drawable.rabbit)),
                new QuizImage("gromit mug", BitmapFactory.decodeResource(resources, R.drawable.gromit_mug)),
                new QuizImage("isopod", BitmapFactory.decodeResource(resources, R.drawable.isopod)),
                new QuizImage("coca cola", BitmapFactory.decodeResource(resources, R.drawable.coca_cola)),
                new QuizImage("saul goodman", BitmapFactory.decodeResource(resources, R.drawable.saul_goodman))
                // new QuizImage("?", BitmapFactory.decodeResource(resources, R.drawable.?))
        };

        return quizImages;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        final int MAX_SIZE = 10000;
        if (bitmap.getByteCount() > MAX_SIZE) {
            double dividend = sqrt(bitmap.getByteCount() / MAX_SIZE);

            int newWidth = (int) (bitmap.getWidth() / dividend);
            int newHeight = (int) (bitmap.getHeight() / dividend);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            bitmap.recycle();
            return scaledBitmap;
        } else return bitmap;
    }
}
