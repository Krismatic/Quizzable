package no.hvl.dat153.utils;

import static java.lang.Math.sqrt;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import no.hvl.dat153.R;
import no.hvl.dat153.database.QuizImageRoomDatabase;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.model.QuizImageData;

public class DatabaseUtils {

    public static QuizImageData[] getDefaults(Resources resources) {
        // Default entries
        QuizImageData[] quizImages = {
                new QuizImageData("cat", BitmapFactory.decodeResource(resources, R.drawable.cat)),
                new QuizImageData("dog", BitmapFactory.decodeResource(resources, R.drawable.dog)),
                new QuizImageData("rabbit", BitmapFactory.decodeResource(resources, R.drawable.rabbit)),
                new QuizImageData("gromit mug", BitmapFactory.decodeResource(resources, R.drawable.gromit_mug)),
                new QuizImageData("isopod", BitmapFactory.decodeResource(resources, R.drawable.isopod)),
                new QuizImageData("coca cola", BitmapFactory.decodeResource(resources, R.drawable.coca_cola)),
                new QuizImageData("saul goodman", BitmapFactory.decodeResource(resources, R.drawable.saul_goodman))
                // new QuizImageData("?", BitmapFactory.decodeResource(resources, R.drawable.?))
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

    public String saveToInternalStorage(Context context, QuizImageData quizImageData) {
        String name = quizImageData.getName();
        Bitmap image = quizImageData.getBitmap();

        ContextWrapper cw = new ContextWrapper(context);
        // Path to the directory
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Path to the image
        File path = new File(directory, name + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Write image to the output stream.
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.getAbsolutePath();
    }

    public static Bitmap getBitmapFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
