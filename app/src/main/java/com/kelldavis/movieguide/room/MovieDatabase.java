package com.kelldavis.movieguide.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.kelldavis.movieguide.model.Movie;

import static com.kelldavis.movieguide.utilities.Constants.LOG_TAG;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movie";

    //singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile MovieDatabase movieDatabaseInstance;

    public abstract MovieDAO movieDao();

    public static MovieDatabase getInstance(Context context) {

        if (movieDatabaseInstance == null) {
            synchronized (LOCK) {
                if (movieDatabaseInstance == null) {
                    movieDatabaseInstance = Room.databaseBuilder(context, MovieDatabase.class,
                            MovieDatabase.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.d(LOG_TAG, "Initializing the database instance");
                }
            }
        }
        return movieDatabaseInstance;
    }
}

