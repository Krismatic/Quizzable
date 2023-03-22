package no.hvl.dat153.model;

import android.graphics.Bitmap;

public class QuizImageData {
    private String name;
    private Bitmap bitmap;

    public QuizImageData(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
