package com.kelldavis.movieguide.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.utilities.AppExecutors;

import java.util.List;

public class MovieRepository {

    private MovieDAO movieDao;
    private AppExecutors appExecutors;

    private LiveData<List<Movie>> movies;

    public MovieRepository(Application application) {
        //get reference to DAO to access database
        movieDao = MovieDatabase.getInstance(application).movieDao();
        movies = movieDao.getFavoritesMovies();

        //get reference to executor instance to handle background tasks
        appExecutors = AppExecutors.getExecutorInstance();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movies;
    }

    public boolean isFavorite(int movieId) {
        return movieDao.isFavorite(movieId);
    }

    public void updateMovieFavorite(int movieId, boolean isFavorite) {
        appExecutors.getDiskIO().execute(() -> {
            movieDao.updateMovieFavorite(movieId, isFavorite);
        });
    }

    public void addMovieToFavorites(Movie movie) {
        appExecutors.getDiskIO().execute(() -> {
            movieDao.addMovieToFavorites(movie);
        });
    }

    public void removeMovieFromFavorites(Movie movie) {
        appExecutors.getDiskIO().execute(() -> {
            movieDao.removeMovieFromFavorites(movie);
        });
    }

}
