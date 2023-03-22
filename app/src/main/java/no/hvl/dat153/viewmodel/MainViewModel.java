package no.hvl.dat153.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.model.QuizImage;

public class MainViewModel extends AndroidViewModel {

    private QuizImageRepository repo;
    private LiveData<List<QuizImage>> allQuizImages;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repo = new QuizImageRepository(application);
        allQuizImages = repo.getAllQuizImages();
    }

    public void insertSeveral(QuizImage... quizImages) {
        repo.insertQuizImages(quizImages);
    }
}
