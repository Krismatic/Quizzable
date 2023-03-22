package no.hvl.dat153.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;

import no.hvl.dat153.R;
import no.hvl.dat153.database.QuizImageDAOOld;
import no.hvl.dat153.database.QuizImageRepository;
import no.hvl.dat153.databinding.ActivityDatabaseBinding;
import no.hvl.dat153.utils.ActivityUtils;
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

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        // Sets up the recycler view.
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

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