package no.hvl.dat153.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.model.QuizImageData;
import no.hvl.dat153.utils.DatabaseUtils;

public class QuizApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes the repository.
        // QuizImageRepository repo = new QuizImageRepository(this);
    }
}
