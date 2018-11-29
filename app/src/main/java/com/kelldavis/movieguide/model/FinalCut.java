package com.kelldavis.movieguide.model;

import java.util.List;

import io.reactivex.Observable;

/**
 * Final cut.
 */
public class FinalCut {
    private Observable<List<Review>> mReviewList;
    private Observable<List<Video>> mVideoList;
    private Observable<List<Cast>> mCastList;
    Movie mMovie;

    /**
     * Gets reviews.
     *
     * @return the reviews
     */
    public Observable<List<Review>> getReviews() {
        return mReviewList;
    }

    /**
     * Sets reviews.
     *
     * @param reviewList the review list
     */
    public void setReviews(Observable<List<Review>> reviewList) {
        mReviewList = reviewList;
    }

    /**
     * Gets videos.
     *
     * @return the videos
     */
    public Observable<List<Video>> getVideos() {
        return mVideoList;
    }

    /**
     * Sets videos.
     *
     * @param videoList the video list
     */
    public void setVideos(Observable<List<Video>> videoList) {
        mVideoList = videoList;
    }

    /**
     * Gets casts.
     *
     * @return the casts
     */
    public Observable<List<Cast>> getCasts() {
        return mCastList;
    }

    /**
     * Sets casts.
     *
     * @param castList the cast list
     */
    public void setCasts(Observable<List<Cast>> castList) {
        mCastList = castList;
    }

    /**
     * Gets movie.
     *
     * @return the movie
     */
    public Movie getMovie() {
        return mMovie;
    }

    /**
     * Sets movie.
     *
     * @param movie the movie
     */
    public void setMovie(Movie movie) {
        mMovie = movie;
    }

    /**
     * Instantiates a new Final cut.
     *
     * @param reviewList the review list
     * @param videoList  the video list
     * @param castList   the cast list
     * @param movie      the movie
     */
    public FinalCut(Observable<List<Review>> reviewList, Observable<List<Video>> videoList, Observable<List<Cast>> castList, Movie movie) {
        mReviewList = reviewList;
        mVideoList = videoList;
        mCastList = castList;
        mMovie = movie;
    }

}

