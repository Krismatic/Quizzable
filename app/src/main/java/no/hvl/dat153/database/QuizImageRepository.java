package no.hvl.dat153.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import no.hvl.dat153.model.QuizImage;

public class QuizImageRepository {
    private MutableLiveData<List<QuizImage>> searchResults = new MutableLiveData<>();
    private LiveData<List<QuizImage>> allQuizImages;
    private QuizImageDAO quizImageDAO;

    public QuizImageRepository(Application application) {
        QuizImageRoomDatabase db;
        db = QuizImageRoomDatabase.getDatabase(application);
        quizImageDAO = db.quizImageDAO();
        allQuizImages = quizImageDAO.getAllQuizImages();
    }
}
