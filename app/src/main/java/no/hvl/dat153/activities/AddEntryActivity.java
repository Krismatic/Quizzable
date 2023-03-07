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
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        this.bitmap = BitmapFactory.decodeStream(inputStream);
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

        name = binding.animalInput.getText().toString();
        image = binding.animalImage;

        binding.animalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });

        binding.galleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.addButton.setOnClickListener(view -> {
            if (name != null && name.length() > 0 && image.getDrawable() != null) {
                if (QuizImageDAO.get().addQuizImage(name, bitmap)) {
                    Toast.makeText(getApplicationContext(), "Item added to database!", Toast.LENGTH_SHORT).show();
                    //System.out.println(AnimalDAO.get().getAllAnimals().size());
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "This item already exists in the database!", Toast.LENGTH_SHORT).show();
                }
            } else {
                System.out.println(name);
                if (name == null || name.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "You need to enter a name!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You need to pick an image!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}