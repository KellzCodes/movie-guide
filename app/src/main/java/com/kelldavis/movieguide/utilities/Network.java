package com.kelldavis.movieguide.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.kelldavis.movieguide.listener.MovieService;
import com.kelldavis.movieguide.model.Cast;
import com.kelldavis.movieguide.model.Credits;
import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.model.MovieResults;
import com.kelldavis.movieguide.model.Review;
import com.kelldavis.movieguide.model.ReviewResults;
import com.kelldavis.movieguide.model.Video;
import com.kelldavis.movieguide.model.VideoResults;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static final String SCHEMA = Constants.SCHEMA;
    private static final String API_KEY = Constants.API_KEY;
    private static final String BASE_URL = Constants.BASE_URL;
    private static final String LANGUAGE = Constants.LANGUAGE;
    public static final Retrofit RETROFIT = getRetrofitInstance();

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    private static Retrofit getRetrofitInstance(){
        Retrofit.Builder builder = new Retrofit.Builder();
        Uri uri = new Uri.Builder().scheme(SCHEMA).authority(BASE_URL).build();
        Log.e("response",uri.toString());
        builder.baseUrl(uri.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return builder.build();
    }

    public static Observable<List<Movie>> loadPopularMoviesList(int page){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<MovieResults> movieListResponse = movieService.getPopularMovieListResponse(API_KEY, page);
        return movieListResponse.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getResults());
    }

    public static Observable<List<Movie>> loadTopRatedMoviesList(int page){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<MovieResults> movieListResponse = movieService.getTopRatedMovieListResponse(API_KEY, page);
        return movieListResponse.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getResults());
    }

    public static Observable<List<Video>> loadVideoWithId(String id){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<VideoResults> videoListResponse = movieService.getVideoById(id, API_KEY);
        return videoListResponse.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getResults());
    }

    public static Observable<List<Review>> loadReviewWithId(String id){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<ReviewResults> reviewListResponse = movieService.getReviewById(id, API_KEY);
        return reviewListResponse.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getResults());
    }

    public static Observable<List<Cast>> loadCastWithId(String id){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<Credits> credits = movieService.getCastById(id, API_KEY);
        return credits.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getCast());
    }

    public static Observable<List<Movie>> getCustomMovieByQuery(String query,int page){
        MovieService movieService = RETROFIT.create(MovieService.class);
        Observable<MovieResults> movieListResponse = movieService.getCustomMovieByQuery(API_KEY, query, LANGUAGE, page);
        return movieListResponse.subscribeOn(Schedulers.io())
                .map(scheduler-> scheduler.getResults());
    }
}

