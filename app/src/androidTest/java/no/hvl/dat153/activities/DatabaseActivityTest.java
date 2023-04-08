package no.hvl.dat153.activities;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.atomic.AtomicInteger;

import no.hvl.dat153.R;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DatabaseActivityTest {

    public ActivityScenario<DatabaseActivity> activityScenario;

    private static String name;

    @BeforeClass
    public static void setUpClass() {
        name = (int) (Math.random()*1000+1) + "";
    }

    @Test
    public void test_addImage() {
        Intents.init();

        activityScenario = activityScenario.launch(DatabaseActivity.class);

        AtomicInteger atomicBefore = new AtomicInteger(-1);

        activityScenario.onActivity(activity -> {
            activity.getAllQuizImages().observe(activity, quizImages -> {
                atomicBefore.set(quizImages.size());
            });
        });

        int before = atomicBefore.get();

        onView(withId(R.id.db_add_button)).perform(click());

        // Creates an intent for gallery picking.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Uri imageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.espresso);
        Intent galleryIntent = new Intent();
        galleryIntent.setData(imageUri);

        // Sets a predefined response to the ACTION_PICK action.
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, galleryIntent);
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

        // Enters name, picks image and adds the entry.
        onView(withId(R.id.add_item_input)).perform(replaceText(name));
        onView(withId(R.id.gallery_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());

        // Relaunches the database activity.
        activityScenario = activityScenario.launch(DatabaseActivity.class);

        AtomicInteger atomicAfter = new AtomicInteger(-1);

        activityScenario.onActivity(activity -> {
            activity.getAllQuizImages().observe(activity, quizImages -> {
                atomicAfter.set(quizImages.size());
            });
        });

        int after = atomicAfter.get();

        Intents.release();

        assertEquals(before + 1, after);
    }

    @Test
    public void test_deleteImage() {
        activityScenario = activityScenario.launch(DatabaseActivity.class);

        AtomicInteger atomicBefore = new AtomicInteger(-1);

        activityScenario.onActivity(activity -> {
            activity.getAllQuizImages().observe(activity, quizImages -> {
                atomicBefore.set(quizImages.size());
            });
        });

        int before = atomicBefore.get();
        // Clicks the delete button on the item created by the other test.
        onView(allOf(withId(R.id.item_delete), hasSibling(withText(name)))).perform(click());
        // Accepts the delete prompt.
        onView(withId(android.R.id.button1)).perform(click());

        AtomicInteger atomicAfter = new AtomicInteger(-1);

        activityScenario.onActivity(activity -> {
            activity.getAllQuizImages().observe(activity, quizImages -> {
                atomicAfter.set(quizImages.size());
            });
        });

        int after = atomicAfter.get();
        assertEquals(before - 1, after);
    }
}
