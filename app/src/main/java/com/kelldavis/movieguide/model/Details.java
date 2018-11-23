package com.kelldavis.movieguide.model;

import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("runtime")
    private int runtime;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("homepage")
    private String homepage;

    public Details(int runtime, String originalTitle, String homepage) {
        this.runtime = runtime;
        this.originalTitle = originalTitle;
        this.homepage = homepage;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getHomepage() {
        return homepage;
    }
}

