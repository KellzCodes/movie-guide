package com.kelldavis.movieguide.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.room.MovieRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        MovieRepository movieRepository = new MovieRepository(application);
        movies = movieRepository.getFavoriteMovies();
    }


    public LiveData<List<Movie>> getFavoriteMovies() {
        return movies;
    }
}

