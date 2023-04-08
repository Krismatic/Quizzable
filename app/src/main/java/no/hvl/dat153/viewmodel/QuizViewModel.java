package no.hvl.dat153.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Stack;

import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.model.QuizImage;

public class QuizViewModel extends AndroidViewModel {
    private QuizImageRepository repo;
    private LiveData<List<QuizImage>> allQuizImages;

    private List<QuizImage> currentQuizImages;
    private Stack<Integer> order;

    private boolean hardMode;
    private boolean checked;
    private int current;
    private int counter;
    private int correctCounter;
    private int correctOption;
    private String correctName;
    private int elapsedTime;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repo = new QuizImageRepository(application);
        allQuizImages = repo.getAllQuizImages();
    }

    public LiveData<List<QuizImage>> getAllQuizImages() {
        return allQuizImages;
    }

    public List<QuizImage> getCurrentQuizImages() {
        return currentQuizImages;
    }

    public void setCurrentQuizImages(List<QuizImage> currentQuizImages) {
        this.currentQuizImages = currentQuizImages;
    }

    public Stack<Integer> getOrder() {
        return order;
    }

    public void setOrder(Stack<Integer> order) {
        this.order = order;
    }

    public boolean isHardMode() {
        return hardMode;
    }

    public void setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public int getCorrectCounter() {
        return correctCounter;
    }

    public void setCorrectCounter(int correctCounter) {
        this.correctCounter = correctCounter;
    }

    public void incrementCorrectCounter() {
        correctCounter++;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectName() {
        return correctName;
    }

    public void setCorrectName(String correctName) {
        this.correctName = correctName;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void incrementElapsedTime() {
        elapsedTime++;
    }
}
