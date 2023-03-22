package no.hvl.dat153.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import no.hvl.dat153.R;
import no.hvl.dat153.database.QuizImageDAOOld;
import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.databinding.ActivityDatabaseBinding;
import no.hvl.dat153.model.QuizImageData;
import no.hvl.dat153.utils.ActivityUtils;
import no.hvl.dat153.utils.DatabaseUtils;
import no.hvl.dat153.view.QuizImageAdapter;
import no.hvl.dat153.viewmodel.DatabaseViewModel;

public class DatabaseActivity extends AppCompatActivity {

    private final QuizImageAdapter adapter = new QuizImageAdapter(new QuizImageRepository(getApplication()));
    private ActivityDatabaseBinding binding;

    private DatabaseViewModel databaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.toolbar);

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        // Sets up the recycler view.
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        databaseViewModel.getAllQuizImages().observe(this, quizImages -> {
            adapter.setItems(quizImages);
            adapter.notifyDataSetChanged();
        });

        // Listener for sort button.
        binding.dbSortButton.setOnClickListener(view -> {
            // Creates a builder for an alert dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Sets up the builder.
            builder.setMessage("Which order do you want to sort the database in?")
                    .setPositiveButton("Z-A", (dialog, id) -> {
                        // Sorts the database Z-A.
                        sort(true);
                    })
                    .setNegativeButton("A-Z", (dialog, id) -> {
                        // Sorts the database A-Z.
                        sort(false);
                    });
            // Creates the alert dialog.
            AlertDialog alert = builder.create();
            // Sets the title.
            alert.setTitle("Sorting prompt");
            // Shows the alert dialog.
            alert.show();
        });

        // Listener for add button.
        binding.dbAddButton.setOnClickListener(view -> ActivityUtils.startActivity(DatabaseActivity.this, AddEntryActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_reset) {
            // Creates a builder for an alert dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Sets up the builder.
            builder.setMessage("Do you want to reset the database? (This cannot be undone)")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Clears the database.
                        Future<Void> future = databaseViewModel.deleteAll();
                        // Gets the default entries.
                        QuizImageData[] data = DatabaseUtils.getDefaults(getResources());
                        // Wait for the program to finish clearing the database.
                        try {
                            future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // Insert default entries.
                        databaseViewModel.insertSeveral(data);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // Nothing happens
                    });
            // Creates the alert dialog.
            AlertDialog alert = builder.create();
            // Sets the title.
            alert.setTitle("Reset prompt");
            // Shows the alert dialog.
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * Sorts the displayed data.
     * @param reverse whether the data is sorted in reverse order or not
     */
    private void sort(boolean reverse) {
        databaseViewModel.getAllQuizImages().observe(this, quizImages -> {
            if (!reverse) Collections.sort(quizImages);
            else Collections.sort(quizImages, Collections.reverseOrder());
            adapter.setItems(quizImages);
            adapter.notifyDataSetChanged();
        });
    }
}