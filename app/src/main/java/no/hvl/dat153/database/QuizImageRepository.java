package no.hvl.dat153.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import no.hvl.dat153.model.QuizImage;

public class QuizImageRepository {
    private LiveData<List<QuizImage>> allQuizImages;
    private LiveData<List<QuizImage>> allQuizImagesAsc;
    private LiveData<List<QuizImage>> allQuizImagesDesc;
    private QuizImageRoomDatabase database;
    private QuizImageDAO quizImageDAO;

    public LiveData<List<QuizImage>> getAllQuizImages() {
        return allQuizImages;
    }

    public LiveData<List<QuizImage>> getAllQuizImages(String sort) {
        if (sort.equalsIgnoreCase("asc")) return allQuizImagesAsc;
        else if (sort.equalsIgnoreCase("desc")) return allQuizImagesDesc;
        else return null;
    }

    public QuizImageRepository(Application application) {
        database = QuizImageRoomDatabase.getDatabase(application);
        quizImageDAO = database.quizImageDAO();

        allQuizImages = quizImageDAO.getAllQuizImages();
        allQuizImagesAsc = quizImageDAO.getAllQuizImagesAsc();
        allQuizImagesDesc = quizImageDAO.getAllQuizImagesDesc();
    }

    public Future<Boolean> insertQuizImage(final QuizImage quizImage) {
        return database.databaseWriteExecutor.submit(() -> {
            if (quizImageDAO.find(quizImage.getName()).size() == 0) {
                quizImageDAO.insertQuizImage(quizImage);
                return true;
            } else return false;
        });
    }

    public Future<Boolean> deleteQuizImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> {
            quizImageDAO.deleteQuizImage(name);
            return quizImageDAO.find(name).size() == 0;
        });
    }

    public Future<QuizImage> findQuizImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> quizImageDAO.find(name).get(0));
    }
}
