package no.hvl.dat153.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;

import no.hvl.dat153.database.QuizImageDAOOld;
import no.hvl.dat153.databinding.ActivityDatabaseBinding;
import no.hvl.dat153.utils.ActivityUtils;
import no.hvl.dat153.view.QuizImageAdapter;

public class DatabaseActivity extends AppCompatActivity {

    private final QuizImageAdapter adapter = new QuizImageAdapter(QuizImageDAOOld.get().getAllQuizImages());
    private ActivityDatabaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                        QuizImageDAOOld.get().sort(true);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("A-Z", (dialog, id) -> {
                        // Sorts the database A-Z.
                        QuizImageDAOOld.get().sort(false);
                        adapter.notifyDataSetChanged();
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
}