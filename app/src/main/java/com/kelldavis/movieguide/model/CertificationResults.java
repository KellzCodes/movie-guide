package com.kelldavis.movieguide.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CertificationResults implements Parcelable {

    @SerializedName("countries")
    private
    List<Certification> countries;

    public CertificationResults(List<Certification> countries) {
        this.countries = countries;
    }

    public CertificationResults(Parcel parcel) {
        parcel.readTypedList(countries, Certification.CREATOR);
    }

    public List<Certification> getCertificationList() {
        return countries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(countries);
    }

    public static final Creator<CertificationResults> CREATOR = new Creator<CertificationResults>() {
        @Override
        public CertificationResults createFromParcel(Parcel parcel) {
            return new CertificationResults(parcel);
        }

        @Override
        public CertificationResults[] newArray(int i) {
            return new CertificationResults[i];
        }
    };
}

