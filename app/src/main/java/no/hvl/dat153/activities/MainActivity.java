package no.hvl.dat153.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import no.hvl.dat153.R;
import no.hvl.dat153.database.QuizImageDAOOld;
import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.database.QuizImageRoomDatabase;
import no.hvl.dat153.databinding.ActivityMainBinding;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.model.QuizImageData;
import no.hvl.dat153.utils.ActivityUtils;
import no.hvl.dat153.utils.DatabaseUtils;
import no.hvl.dat153.viewmodel.MainViewModel;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private String difficulty = "easy";

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checks if the database exists.
        boolean dbExists = QuizImageRoomDatabase.exists();
        System.out.println(dbExists);
        // Gets the view model.
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // If the database did not exist, adds in the default entries.
        if (!dbExists) {
            QuizImageData[] defaultEntries = DatabaseUtils.getDefaults(getResources());
            mainViewModel.insertSeveral(defaultEntries);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Switches difficulty if the switch is switched.
        binding.content.difficultySwitch.setOnCheckedChangeListener(this::switchDifficulty);
        // Adds listeners for the buttons used to go to other activities.
        binding.content.quizButton.setOnClickListener(view -> ActivityUtils.startActivity(MainActivity.this, QuizActivity.class, "difficulty", difficulty));
        binding.content.dbButton.setOnClickListener(view -> ActivityUtils.startActivity(MainActivity.this, DatabaseActivity.class));
        binding.addEntryButton.setOnClickListener(view -> ActivityUtils.startActivity(MainActivity.this, AddEntryActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Switches the difficulty of the quiz.
     * @param o unused object received from method reference operator
     * @param hardMode whether hard mode is toggled or not
     */
    private void switchDifficulty(Object o, boolean hardMode) {
        if (hardMode) difficulty = "hard";
        else difficulty = "easy";
    }
}