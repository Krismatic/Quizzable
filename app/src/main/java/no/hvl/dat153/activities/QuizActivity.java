package no.hvl.dat153.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import no.hvl.dat153.database.QuizImageDAOOld;
import no.hvl.dat153.databinding.ActivityQuizBinding;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.utils.DatabaseUtils;
import no.hvl.dat153.viewmodel.QuizViewModel;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;

    private ActivityQuizBinding binding;

    private final int MAXTIME = 30; // Hard mode max time (seconds)

    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            System.out.println(quizViewModel.getElapsedTime());
            quizViewModel.incrementElapsedTime();
            int elapsedTime = quizViewModel.getElapsedTime();
            binding.timerProgress.setProgress(MAXTIME - elapsedTime);
            if (elapsedTime < MAXTIME) {
                timerHandler.postDelayed(this, 1000);
            } else {
                check(-1);
                next();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        // Whether hard mode was toggled on or not.
        quizViewModel.setHardMode(intent.getStringExtra("difficulty").equals("hard"));

        // Initialize variables.
        quizViewModel.setCurrent(-1);
        quizViewModel.setCounter(0);
        quizViewModel.setCorrectCounter(0);
        quizViewModel.setChecked(false);
        /*
        current = -1;
        counter = 0;
        correctCounter = 0;
        checked = false;
        order = new Stack<>();
         */

        quizViewModel.getAllQuizImages().observe(this, quizImages -> {
            quizViewModel.setCurrentQuizImages(quizImages);

            Stack<Integer> order = new Stack<>();

            for (int i = 0; i < quizImages.size(); i++) {
                order.push(i);
            }

            // Shuffles the order.
            Collections.shuffle(order);

            binding.progressBar.setMax(order.size());
            binding.progressBar.setProgress(0);

            quizViewModel.setOrder(order);

            next();

            // Starts the timer if hard mode is on.
            if (quizViewModel.isHardMode()) {
                binding.timer.setVisibility(View.VISIBLE);
                binding.timerProgress.setMax(MAXTIME);
                startTimer();
            }
        });

        binding.option1.setOnClickListener(view -> {
            check(1);
            next();
        });

        binding.option2.setOnClickListener(view -> {
            check(2);
            next();
        });

        binding.option3.setOnClickListener(view -> {
            check(3);
            next();
        });

        binding.backButton.setOnClickListener(view -> finish());
    }

    /**
     * Checks if the picked option is correct.
     * @param option the picked option
     */
    private void check(int option) {
        if (quizViewModel.isChecked()) return;
        quizViewModel.setChecked(true);
        quizViewModel.incrementCounter();

        String correctNameCapitalized = quizViewModel.getCorrectName().substring(0, 1).toUpperCase() + quizViewModel.getCorrectName().substring(1);

        if (option == quizViewModel.getCorrectOption()) quizViewModel.incrementCorrectCounter();
        else if (option == -1) Toast.makeText(getApplicationContext(), "Timer ran out. The correct answer was: " + correctNameCapitalized, Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(), "Wrong answer. The correct answer was: " + correctNameCapitalized, Toast.LENGTH_SHORT).show();

        binding.progressBar.setProgress(quizViewModel.getCounter());
    }

    /**
     * Starts the next question.
     */
    private void next() {
        int correctCounter = quizViewModel.getCorrectCounter();
        int counter = quizViewModel.getCounter();
        // Checks if the quiz is finished.
        if (quizViewModel.getOrder().size() <= 0) {
            binding.correctAnswers.setText("You finished the quiz with " + correctCounter + " correct answer" + plural(correctCounter) + " out of " + counter + " question" + plural(counter));
            // Removes some views.
            binding.quizImage.setVisibility(View.INVISIBLE);
            binding.option1.setVisibility(View.GONE);
            binding.option2.setVisibility(View.GONE);
            binding.option3.setVisibility(View.GONE);
            binding.timer.setVisibility(View.GONE);
            binding.backButton.setVisibility(View.VISIBLE);
            // Stops the timer.
            stopTimer();
        } else {
            List<QuizImage> quizImages = quizViewModel.getCurrentQuizImages();
            binding.correctAnswers.setText(correctCounter + " correct answer" + plural(correctCounter) + " out of " + counter + " question" + plural(counter));
            // Gets and removes the next image index in the order.
            quizViewModel.setCurrent(quizViewModel.getOrder().pop());
            // Gets the current image.
            QuizImage currentImage = quizImages.get(quizViewModel.getCurrent());
            // Gets the name of the current image.
            quizViewModel.setCorrectName(currentImage.getName());
            // Creates a list of options.
            List<String> options = new ArrayList<>();
            // Adds the name of the current image to the options.
            options.add(quizViewModel.getCorrectName());

            Random random = new Random();
            // Adds two random options
            while (options.size() < 3) {
                String randomName = quizImages.get(random.nextInt(quizImages.size())).getName();
                if (!options.contains(randomName)) {
                    options.add(randomName);
                } else if (options.size() >= quizImages.size()) {
                    options.add("");
                }
            }
            // Shuffles the list of options
            Collections.shuffle(options);
            // Finds out which option is correct
            quizViewModel.setCorrectOption(options.indexOf(currentImage.getName()) + 1);
            // Sets the names onto their respective option buttons
            binding.option1.setText(options.get(0));
            binding.option2.setText(options.get(1));
            binding.option3.setText(options.get(2));
            // Sets the image view as the current image.
            binding.quizImage.setImageBitmap(DatabaseUtils.getBitmapFromStorage(currentImage.getPath()));

            quizViewModel.setChecked(false);
            // Restarts the timer if hard mode is on.
            if (quizViewModel.isHardMode()) startTimer();
        }
    }

    /**
     * Starts the timer.
     */
    private void startTimer() {
        timerHandler.removeCallbacks(timer);
        quizViewModel.setElapsedTime(0);
        binding.timerProgress.setProgress(MAXTIME);
        timerHandler.postDelayed(timer, 1000);
    }

    /**
     * Stops the timer.
     */
    private void stopTimer() {
        timerHandler.removeCallbacks(timer);
    }

    /**
     * Checks if the amount is 1.
     * @param i a number to be checked
     * @return "s" or empty string
     */
    private String plural(int i) {
        return i != 1 ? "s" : "";
    }

    public int getCounter() {
        return quizViewModel.getCounter();
    }

    public int getCorrectCounter() {
        return quizViewModel.getCorrectCounter();
    }

    public int getCorrectOption() {
        return quizViewModel.getCorrectOption();
    }
}