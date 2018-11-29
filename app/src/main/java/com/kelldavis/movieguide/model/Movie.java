package com.kelldavis.movieguide.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    private int movieId;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    private double popularity;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private double voteAverage;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    private String originalLanguage;

    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    private int runtime;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    private String homepage;

    @Ignore
    @SerializedName("genre_ids")
    private ArrayList<Integer> genreIds;

    @Ignore
    @SerializedName("images")
    private ImageResults images;

    @Ignore
    @SerializedName("videos")
    private VideoResults videos;

    @Ignore
    @SerializedName("releases")
    private CertificationResults releases;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @Ignore
    public Movie() {
    }

    //this constructor will be used by Room
    public Movie(int movieId, String posterPath, String backdropPath, String title, String overview, double popularity, double voteAverage, String releaseDate, String originalLanguage, int runtime, String originalTitle, String homepage, boolean isFavorite) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.originalTitle = originalTitle;
        this.homepage = homepage;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public Movie(int movieId, String posterPath, String backdropPath, String title, String overview, double popularity, double voteAverage, String releaseDate, String originalLanguage, int runtime, String originalTitle, String homepage, ArrayList<Integer> genreIds, ImageResults images, VideoResults videos, CertificationResults releases, boolean isFavorite) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.originalTitle = originalTitle;
        this.homepage = homepage;
        this.genreIds = genreIds;
        this.images = images;
        this.videos = videos;
        this.releases = releases;
        this.isFavorite = isFavorite;
    }


    @Ignore
    public Movie(Parcel source) {
        movieId = source.readInt();
        posterPath = source.readString();
        backdropPath = source.readString();
        title = source.readString();
        overview = source.readString();
        popularity = source.readDouble();
        voteAverage = source.readDouble();
        releaseDate = source.readString();
        originalLanguage = source.readString();
        runtime = source.readInt();
        originalTitle = source.readString();
        homepage = source.readString();
        genreIds = (ArrayList<Integer>) source.readSerializable();
        images = source.readParcelable(ImageResults.class.getClassLoader());
        videos = source.readParcelable(VideoResults.class.getClassLoader());
        releases = source.readParcelable(CertificationResults.class.getClassLoader());
        isFavorite = source.readByte() != 0;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public ImageResults getImages() {
        return images;
    }

    public void setImages(ImageResults images) {
        this.images = images;
    }

    public VideoResults getVideos() {
        return videos;
    }

    public void setVideos(VideoResults videos) {
        this.videos = videos;
    }

    public CertificationResults getReleases() {
        return releases;
    }

    public void setReleases(CertificationResults releases) {
        this.releases = releases;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(originalLanguage);
        dest.writeInt(runtime);
        dest.writeString(originalTitle);
        dest.writeString(homepage);
        dest.writeSerializable(genreIds);
        dest.writeParcelable(images, 0);
        dest.writeParcelable(videos, 0);
        dest.writeParcelable(releases, 0);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
