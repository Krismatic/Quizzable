package no.hvl.dat153.utils;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

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
}
