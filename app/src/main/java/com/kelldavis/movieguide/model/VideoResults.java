package com.kelldavis.movieguide.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResults implements Parcelable {

    @SerializedName("results")
    private List<Video> results;

    public VideoResults(List<Video> results) {
        this.results = results;
    }

    public VideoResults(Parcel parcel) {
        parcel.readTypedList(results, Video.CREATOR);
    }

    public List<Video> getResults() {
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(results);
    }

    public static final Creator<VideoResults> CREATOR = new Creator<VideoResults>() {
        @Override
        public VideoResults createFromParcel(Parcel parcel) {
            return new VideoResults(parcel);
        }

        @Override
        public VideoResults[] newArray(int i) {
            return new VideoResults[i];
        }
    };
}

