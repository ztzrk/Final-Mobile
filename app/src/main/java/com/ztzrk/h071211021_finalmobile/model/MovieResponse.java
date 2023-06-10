package com.ztzrk.h071211021_finalmobile.model;import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieResponse implements Parcelable {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("id")
    @Expose
    private int id;

    public MovieResponse() {}

    protected MovieResponse(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    public static final Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {
        @Override
        public MovieResponse createFromParcel(Parcel in) {
            return new MovieResponse(in);
        }

        @Override
        public MovieResponse[] newArray(int size) {
            return new MovieResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(backdropPath);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
    }

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

    private void toFormat() {
        String outputDate = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(releaseDate);
            outputDate = outputFormat.format(date); // Output: June 09, 2023
        } catch (ParseException e) {
            e.printStackTrace();
        }
        releaseDate =  outputDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
