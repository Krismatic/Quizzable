package no.hvl.dat153.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import no.hvl.dat153.model.QuizImage;

@Database(entities = {QuizImage.class}, version = 1)
public abstract class QuizImageRoomDatabase extends RoomDatabase {

    public abstract QuizImageDAO quizImageDAO();
    private static QuizImageRoomDatabase INSTANCE;

    static QuizImageRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuizImageRoomDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        QuizImageRoomDatabase.class, "quiz_image_database").build();
            }
        }
        return INSTANCE;
    }
}
