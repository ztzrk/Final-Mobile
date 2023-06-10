package com.ztzrk.h071211021_finalmobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TvResponse implements Parcelable {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("id")
    @Expose
    private int id;

    public TvResponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }


    // Parcelable implementation
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(firstAirDate);
        dest.writeString(name);
        dest.writeString(backdropPath);
        dest.writeDouble(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR
    public static final Creator<TvResponse> CREATOR = new Creator<TvResponse>() {
        @Override
        public TvResponse createFromParcel(Parcel in) {
            return new TvResponse(in);
        }

        @Override
        public TvResponse[] newArray(int size) {
            return new TvResponse[size];
        }
    };

    // Constructor for parcelable deserialization
    private TvResponse(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        firstAirDate = in.readString();
        name = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readDouble();
    }
}


