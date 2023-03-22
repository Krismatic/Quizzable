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

    /**
     * Gets the default entries of the database.
     * @param resources the application's resources
     * @return an array of quiz image data belonging to the default entries
     */
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

    /**
     * Gets the bitmap from an image in the storage.
     * @param path the path of the image
     * @return the bitmap of the image
     */
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
