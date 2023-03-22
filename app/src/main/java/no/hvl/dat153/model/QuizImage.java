package no.hvl.dat153.model;

import static java.lang.Math.sqrt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

import no.hvl.dat153.utils.DatabaseUtils;

@Entity(tableName = "quiz_images")
public class QuizImage implements Comparable<QuizImage> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String path;

    public QuizImage(String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(QuizImage qi) {
        return name.compareTo(qi.getName());
    }
}
