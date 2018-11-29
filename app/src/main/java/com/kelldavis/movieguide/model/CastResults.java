package com.kelldavis.movieguide.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Cast response.
 */
public class CastResults {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets cast.
     *
     * @return the cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     * Sets cast.
     *
     * @param cast the cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}

