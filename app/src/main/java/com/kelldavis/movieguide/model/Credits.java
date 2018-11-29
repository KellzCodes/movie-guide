package com.kelldavis.movieguide.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<Cast> cast;
    @SerializedName("crew")
    private List<Crew> crew;

    public Credits(int id, List<Cast> cast, List<Crew> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public int getId() {
        return id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }
}

