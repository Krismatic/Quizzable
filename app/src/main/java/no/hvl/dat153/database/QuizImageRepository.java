package no.hvl.dat153.database;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.model.QuizImageData;
import no.hvl.dat153.utils.DatabaseUtils;

public class QuizImageRepository {
    private LiveData<List<QuizImage>> allQuizImages;
    private QuizImageRoomDatabase database;
    private QuizImageDAO quizImageDAO;
    private Application application;

    public LiveData<List<QuizImage>> getAllQuizImages() {
        return allQuizImages;
    }

    public QuizImageRepository(Application application) {
        this.application = application;

        database = QuizImageRoomDatabase.getDatabase(application);
        quizImageDAO = database.quizImageDAO();

        allQuizImages = quizImageDAO.getAllQuizImages();
    }

    public void insertQuizImages(final QuizImageData... quizImages) {
        database.databaseWriteExecutor.execute(() -> {
            for (QuizImageData quizImageData : quizImages) {
                if (quizImageDAO.find(quizImageData.getName()).size() == 0) {
                    String path = saveToInternalStorage(quizImageData);
                    QuizImage quizImage = new QuizImage(quizImageData.getName(), path);
                    quizImageDAO.insertQuizImage(quizImage);
                }
            }
        });
    }

    public Future<Boolean> insertQuizImage(final QuizImageData quizImageData) {
        return database.databaseWriteExecutor.submit(() -> {
            if (quizImageDAO.find(quizImageData.getName()).size() == 0) {
                String path = saveToInternalStorage(quizImageData);
                QuizImage quizImage = new QuizImage(quizImageData.getName(), path);
                quizImageDAO.insertQuizImage(quizImage);
                return true;
            } else return false;
        });
    }

    public Future<Boolean> deleteQuizImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> {
            List<QuizImage> searchResults = quizImageDAO.find(name);
            if (searchResults.size() > 0) {
                quizImageDAO.deleteQuizImage(name);
                deleteFromInternalStorage(searchResults.get(0).getPath());
                return quizImageDAO.find(name).size() == 0;
            } else return false;
        });
    }

    public Future<QuizImage> findQuizImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> quizImageDAO.find(name).get(0));
    }

    public void clearDatabase() {
        database.databaseWriteExecutor.execute(() -> quizImageDAO.clearDatabase());
    }

    private String saveToInternalStorage(QuizImageData quizImageData) {
        String name = quizImageData.getName();
        Bitmap image = quizImageData.getBitmap();

        ContextWrapper cw = new ContextWrapper(application.getApplicationContext());
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

    private boolean deleteFromInternalStorage(String path) {
        return new File(path).delete();
    }

    private Bitmap getBitmapFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
