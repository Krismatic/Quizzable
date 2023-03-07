package no.hvl.dat153.activities;

import androidx.appcompat.app.AppCompatActivity;

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

import no.hvl.dat153.database.QuizImageDAO;
import no.hvl.dat153.databinding.ActivityQuizBinding;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.utils.ActivityUtils;

public class QuizActivity extends AppCompatActivity {

    private List<QuizImage> quizImages;
    private Stack<Integer> order;
    private boolean hardMode;

    private boolean checked;
    private int current;
    private int counter;
    private int correctCounter;
    private int correctOption;
    private String correctName;

    private ActivityQuizBinding binding;

    private final int MAXTIME = 30; // Hard mode max time (seconds)

    private int elapsedTime;
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            System.out.println(elapsedTime);
            elapsedTime++;
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

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        // Whether hard mode was toggled on or not.
        hardMode = intent.getStringExtra("difficulty").equals("hard");
        // List of all images and names in the database.
        quizImages = QuizImageDAO.get().getAllQuizImages();
        order = new Stack<>();
        for (int i = 0; i < quizImages.size(); i++) {
            order.push(i);
        }
        // Shuffles the order.
        Collections.shuffle(order);

        // Initialize variables.
        current = -1;
        counter = 0;
        correctCounter = 0;
        checked = false;

        binding.progressBar.setMax(order.size());
        binding.progressBar.setProgress(0);

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

        binding.backButton.setOnClickListener(view -> ActivityUtils.startActivity(QuizActivity.this, MainActivity.class));

        next();

        // Starts the timer if hard mode is on.
        if (hardMode) {
            binding.timer.setVisibility(View.VISIBLE);
            binding.timerProgress.setMax(MAXTIME);
            startTimer();
        }
    }

    /**
     * Checks if the picked option is correct.
     * @param option the picked option
     */
    private void check(int option) {
        if (checked) return;
        checked = true;
        counter++;

        String correctNameCapitalized = correctName.substring(0, 1).toUpperCase() + correctName.substring(1);

        if (option == correctOption) correctCounter++;
        else if (option == -1) Toast.makeText(getApplicationContext(), "Timer ran out. The correct answer was: " + correctNameCapitalized, Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(), "Wrong answer. The correct answer was: " + correctNameCapitalized, Toast.LENGTH_SHORT).show();

        binding.progressBar.setProgress(counter);
    }

    /**
     * Starts the next question.
     */
    private void next() {
        // Checks if the quiz is finished.
        if (order.size() <= 0) {
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
            System.out.println(correctCounter);
            binding.correctAnswers.setText(correctCounter + " correct answer" + plural(correctCounter) + " out of " + counter + " question" + plural(counter));
            // Gets and removes the next image index in the order.
            current = order.pop();
            // Gets the current image.
            QuizImage currentImage = quizImages.get(current);
            // Gets the name of the current image.
            correctName = currentImage.getName();
            // Creates a list of options.
            List<String> options = new ArrayList<>();
            // Adds the name of the current image to the options.
            options.add(correctName);

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
            correctOption = options.indexOf(currentImage.getName()) + 1;
            // Sets the names onto their respective option buttons
            binding.option1.setText(options.get(0));
            binding.option2.setText(options.get(1));
            binding.option3.setText(options.get(2));
            // Sets the image view as the current image.
            binding.quizImage.setImageBitmap(currentImage.getImage());

            checked = false;
            // Restarts the timer if hard mode is on.
            if (hardMode) startTimer();
        }
    }

    /**
     * Starts the timer.
     */
    private void startTimer() {
        timerHandler.removeCallbacks(timer);
        elapsedTime = 0;
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
}