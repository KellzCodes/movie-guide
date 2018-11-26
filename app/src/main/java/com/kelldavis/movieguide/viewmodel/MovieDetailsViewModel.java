package com.kelldavis.movieguide.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.room.MovieDAO;
import com.kelldavis.movieguide.room.MovieDatabase;

import java.util.List;

import static com.kelldavis.movieguide.utilities.Constants.LOG_TAG;

public class MovieDetailsViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MovieDetailsViewModel(Application application) {
        super(application);
        Log.d(LOG_TAG, "Initializing Movie Mutable LiveData object inside ViewModel");
        MovieDAO movieDao = MovieDatabase.getInstance(application).movieDao();
        movies = movieDao.getFavoritesMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movies;
    }
}

