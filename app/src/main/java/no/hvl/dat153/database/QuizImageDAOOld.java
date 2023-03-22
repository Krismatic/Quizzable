package no.hvl.dat153.database;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hvl.dat153.R;
import no.hvl.dat153.model.QuizImage;

public class QuizImageDAOOld {
    private static QuizImageDAOOld quizImageDAO;
    private List<QuizImage> quizImages;
    private Map<String, QuizImage> map;

    private QuizImageDAOOld() {
        this.quizImages = new ArrayList<>();
        this.map = new HashMap<>();
    }

    /**
     * Gets all of the quiz images.
     * @return a list of all items in the database
     */
    public List<QuizImage> getAllQuizImages() {
        return quizImages;
    }

    /**
     * Gets all the names of the quiz images
     * @return a list of all names in the database
     */
    public List<String> getAllNames() {
        return new ArrayList<String>(map.keySet());
    }

    /**
     * Gets a specific quiz image.
     * @param name the name of the quiz image
     * @return the quiz image
     */
    public QuizImage getQuizImage(String name) {
        return map.get(name.toLowerCase());
    }

    /**
     * Adds a quiz image to the database.
     * @param name the name of the quiz image
     * @param image the bitmap of the quiz image
     * @return whether the item was successfully added or not
     */
    public boolean addQuizImage(String name, Bitmap image) {
        String lcName = name.toLowerCase();
        if (map.containsKey(lcName)) return false;
        //QuizImage quizImage = new QuizImage(lcName, image);
        //quizImages.add(quizImage);
        //map.put(quizImage.getName(), quizImage);
        return true;
    }

    /**
     * Removes a quiz image from the database.
     * @param name the name of the quiz image
     * @return whether the item was successfully removed or not
     */
    public boolean removeQuizImage(String name) {
        String lcName = name.toLowerCase();
        boolean listSuccess = quizImages.remove(map.get(lcName));
        boolean mapSuccess = map.remove(lcName) != null;

        return listSuccess && mapSuccess;
    }

    /**
     * Sorts the list in the database.
     * @param reverse whether the list should be sorted in reverse order or not
     * @return the sorted list
     */
    public List<QuizImage> sort(boolean reverse) {
        if (!reverse) Collections.sort(quizImages);
        else Collections.sort(quizImages, Collections.reverseOrder());
        return quizImages;
    }

    /**
     * Gets the DAO.
     * @return the DAO
     */
    public static QuizImageDAOOld get() {
        return quizImageDAO;
    }

    /**
     * Starts the DAO and adds default entries, as long as the DAO has not been started before.
     */
    public static void start(Resources resources) {
        if (quizImageDAO == null) {
            quizImageDAO = new QuizImageDAOOld();
            quizImageDAO.addQuizImage("cat", BitmapFactory.decodeResource(resources, R.drawable.cat));
            quizImageDAO.addQuizImage("dog", BitmapFactory.decodeResource(resources, R.drawable.dog));
            quizImageDAO.addQuizImage("rabbit", BitmapFactory.decodeResource(resources, R.drawable.rabbit));
            quizImageDAO.addQuizImage("gromit mug", BitmapFactory.decodeResource(resources, R.drawable.gromit_mug));
            quizImageDAO.addQuizImage("isopod", BitmapFactory.decodeResource(resources, R.drawable.isopod));
            quizImageDAO.addQuizImage("coca cola", BitmapFactory.decodeResource(resources, R.drawable.coca_cola));
            quizImageDAO.addQuizImage("saul goodman", BitmapFactory.decodeResource(resources, R.drawable.saul_goodman));
            // quizImageDAO.addQuizImage("", BitmapFactory.decodeResource(resources, R.drawable.));
        }
    }
}
