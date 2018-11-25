package com.kelldavis.movieguide.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResults {

    @SerializedName("results")
    private List<Review> results;
    @SerializedName("total_results")
    private int totalResults;

    public ReviewResults(List<Review> results, int totalResults) {
        this.results = results;
        this.totalResults = totalResults;
    }

    public List<Review> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}

