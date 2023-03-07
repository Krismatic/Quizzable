package no.hvl.dat153.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import no.hvl.dat153.database.QuizImageDAO;
import no.hvl.dat153.databinding.ActivityAddEntryBinding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddEntryActivity extends AppCompatActivity {

    private ActivityAddEntryBinding binding;
    private Bitmap bitmap;
    private String name;
    private ImageView image;
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Checks if the data is valid.
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // Sets the image URI as the received URI.
                    Uri imageUri = result.getData().getData();

                    try {
                        // Converts the URI to bitmap.
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        this.bitmap = BitmapFactory.decodeStream(inputStream);
                        // Sets the image view on the screen as the bitmap.
                        this.image.setImageBitmap(this.bitmap);
                    } catch (FileNotFoundException e) {
                        e.getStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        name = binding.addItemInput.getText().toString();
        image = binding.addItemImage;

        // Sets the name as the entered text.
        binding.addItemInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });

        // Allows the user to pick an image from their gallery.
        binding.galleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        // Allows the user to add the data to the database.
        binding.addButton.setOnClickListener(view -> {
            // Checks if the required data was entered.
            if (name != null && name.length() > 0 && image.getDrawable() != null) {
                // Attempts to add the data to the database.
                if (QuizImageDAO.get().addQuizImage(name, bitmap)) {
                    Toast.makeText(getApplicationContext(), "Item added to database!", Toast.LENGTH_SHORT).show();
                    // Finishes the activity.
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "This item already exists in the database!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Checks what is missing.
                if (name == null || name.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "You need to enter a name!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You need to pick an image!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}