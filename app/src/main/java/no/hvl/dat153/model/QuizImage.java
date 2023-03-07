package no.hvl.dat153.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class QuizImage implements Comparable<QuizImage> {
    private String name;
    private Bitmap image;

    public QuizImage(String name, Bitmap image) {
        this.name = name;
        this.image = image;
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
