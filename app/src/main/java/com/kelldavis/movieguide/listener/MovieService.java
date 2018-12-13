package com.kelldavis.movieguide.listener;

import com.kelldavis.movieguide.model.Credits;
import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.model.MovieResults;
import com.kelldavis.movieguide.model.ReviewResults;
import com.kelldavis.movieguide.model.VideoResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("3/movie/top_rated")
    Observable<MovieResults> getTopRatedMovieListResponse(@Query("api_key") String api_key, @Query("page") int page);
    @GET("3/movie/popular")
    Observable<MovieResults> getPopularMovieListResponse(@Query("api_key") String api_key, @Query("page") int page);
    @GET("/3/movie/{id}")
    Observable<Movie> getMovieById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/reviews")
    Observable<ReviewResults> getReviewById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/videos")
    Observable<VideoResults> getVideoById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/credits")
    Observable<Credits> getCastById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/search/multi")
    Observable<MovieResults> getCustomMovieByQuery(@Query("api_key") String api_key, @Query("query") String query, @Query("language") String language, @Query("page") int page);
}
