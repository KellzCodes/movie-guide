package com.kelldavis.movieguide.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Certification implements Parcelable {

    @SerializedName("certification")
    private String certification;
    @SerializedName("iso_3166_1")
    private String iso_3166_1;

    public Certification(String certification, String iso_3166_1) {
        this.certification = certification;
        this.iso_3166_1 = iso_3166_1;
    }

    public Certification(Parcel parcel) {
        certification = parcel.readString();
        iso_3166_1 = parcel.readString();
    }

    public String getCertification() {
        return certification;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(certification);
        parcel.writeString(iso_3166_1);
    }

    public static final Creator<Certification> CREATOR = new Creator<Certification>() {
        @Override
        public Certification createFromParcel(Parcel parcel) {
            return new Certification(parcel);
        }

        @Override
        public Certification[] newArray(int i) {
            return new Certification[i];
        }
    };
}
