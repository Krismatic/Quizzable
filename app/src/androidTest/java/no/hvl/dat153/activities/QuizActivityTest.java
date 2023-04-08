package no.hvl.dat153.activities;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Intent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import no.hvl.dat153.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    public ActivityScenario<QuizActivity> activityScenario;

    @Test
    public void test_correctOptionPicked() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), QuizActivity.class);
        intent.putExtra("difficulty", "easy");

        activityScenario = ActivityScenario.launch(intent);

        activityScenario.onActivity(activity -> {
            new Thread(() -> {
                int correctOption = activity.getCorrectOption();

                int choice = -1;

                switch (correctOption) {
                    case 0:
                        choice = R.id.option_1;
                        break;
                    case 1:
                        choice = R.id.option_2;
                        break;
                    case 2:
                        choice = R.id.option_3;
                        break;
                    default:
                        fail("Correct option out of range.");
                }

                onView(withId(choice)).perform(click());
                assertTrue(activity.getCorrectCounter() == 1 && activity.getCounter() == 1);
            });
        });
    }

    @Test
    public void test_wrongOptionPicked() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), QuizActivity.class);
        intent.putExtra("difficulty", "easy");

        activityScenario = ActivityScenario.launch(intent);

        activityScenario.onActivity(activity -> {
            new Thread(() -> {
                int correctOption = activity.getCorrectOption();

                int choice = -1;

                switch (correctOption) {
                    case 0:
                        choice = R.id.option_2;
                        break;
                    case 1:
                        choice = R.id.option_3;
                        break;
                    case 2:
                        choice = R.id.option_1;
                        break;
                    default:
                        fail("Correct option out of range.");
                }

                onView(withId(choice)).perform(click());
                assertTrue(activity.getCorrectCounter() == 0 && activity.getCounter() == 1);
            });
        });
    }
}
