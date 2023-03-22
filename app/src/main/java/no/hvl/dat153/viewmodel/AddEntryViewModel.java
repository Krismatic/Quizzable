package no.hvl.dat153.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.model.QuizImageData;

public class AddEntryViewModel extends AndroidViewModel {

    private QuizImageRepository repo;
    private LiveData<List<QuizImage>> allQuizImages;

    public AddEntryViewModel(@NonNull Application application) {
        super(application);
        repo = new QuizImageRepository(application);
        allQuizImages = repo.getAllQuizImages();
    }

    public LiveData<List<QuizImage>> getAllQuizImages() {
        return allQuizImages;
    }

    public Future<Boolean> insert(QuizImageData quizImage) {
        return repo.insertQuizImage(quizImage);
    }
}
