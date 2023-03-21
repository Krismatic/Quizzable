package no.hvl.dat153.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_images")
public class QuizImage implements Comparable<QuizImage> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private Bitmap image;

    public QuizImage(String name, Bitmap image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int compareTo(QuizImage qi) {
        return name.compareTo(qi.getName());
    }
}
