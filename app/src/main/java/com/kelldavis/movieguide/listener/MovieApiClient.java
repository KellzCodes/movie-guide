package com.kelldavis.movieguide.listener;

import com.kelldavis.movieguide.model.Credits;
import com.kelldavis.movieguide.model.ImageResults;
import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.model.MovieResults;
import com.kelldavis.movieguide.model.ReviewResults;
import com.kelldavis.movieguide.model.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiClient {

    @GET("movie/popular")
    Call<MovieResults> getPopularMovies (@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies (@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/credits")
    Call<Credits> getMovieCredits (@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResults> getMovieReviews (@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails (@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("append_to_response") String append_to_response);

    @GET("movie/{movie_id}/videos")
    Call<VideoResults> getMovieVideos (@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/images")
    Call<ImageResults> getMovieImages (@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}

