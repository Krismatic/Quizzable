package no.hvl.dat153.model;

import static java.lang.Math.sqrt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "quiz_images")
public class QuizImage implements Comparable<QuizImage> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    // FIXME: Does not work with database, convert to byte[].
    private byte[] imageBytes;

    public QuizImage(String name, byte[] imageBytes) {
        this.id = id;
        this.name = name;
        this.imageBytes = imageBytes;
    }

    public QuizImage(String name, Bitmap bitmap) {
        this.id = id;
        this.name = name;
        this.imageBytes = bitmapToByteArray(bitmap);
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

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Bitmap getBitmap() {
        return byteArrayToBitmap(imageBytes);
    }

    public void setBitmap(Bitmap bitmap) {
        this.imageBytes = bitmapToByteArray(bitmap);
    }

    @Override
    public int compareTo(QuizImage qi) {
        return name.compareTo(qi.getName());
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        // Resizes the bitmap if it is too big.
        Bitmap bmp = resizeBitmap(bitmap);
        // Converts to byte array.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        System.out.println(byteArray.length);
        return byteArray;
    }

    private static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static Bitmap resizeBitmap(Bitmap bitmap) {
        final int MAX_SIZE = 100000;
        if (bitmap.getByteCount() > MAX_SIZE) {
            double dividend = sqrt(bitmap.getByteCount() / MAX_SIZE);

            int newWidth = (int) (bitmap.getWidth() / dividend);
            int newHeight = (int) (bitmap.getHeight() / dividend);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            bitmap.recycle();
            return scaledBitmap;
        } else return bitmap;
    }
}
