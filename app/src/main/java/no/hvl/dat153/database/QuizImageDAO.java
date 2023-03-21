package no.hvl.dat153.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import no.hvl.dat153.model.QuizImage;

@Dao
public interface QuizImageDAO {

    /**
     * Gets all of the quiz images.
     * @return a list of all items in the database
     */
    @Query("SELECT * FROM quizImages")
    LiveData<List<QuizImage>> getAllQuizImages();

    /**
     * Gets all the names of the quiz images
     * @return a list of all names in the database
     */
    @Query("SELECT name FROM quizImages")
    LiveData<List<String>> getAllNames();

    /**
     * Finds any quiz image with the given name in the database.
     * @param name the name of the quiz image
     * @return list of all quiz images found with the name
     */
    @Query("SELECT * FROM quizImages WHERE name = :name")
    List<QuizImage> find(String name);

    /**
     * Inserts a quiz image into the database.
     * @param quizImage the quiz image to be inserted
     */
    @Insert
    void insertQuizImage(QuizImage quizImage);

    /**
     * Removes a quiz image from the database.
     * @param name the name of the quiz image to be removed
     */
    @Query("DELETE FROM quizImages WHERE name = :name")
    void deleteQuizImage(String name);
}
