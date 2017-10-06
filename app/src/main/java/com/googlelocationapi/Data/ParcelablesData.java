package com.googlelocationapi.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ParcelablesData implements Parcelable {

    Double latitude;
    Double longitude;
    String icon;
    String name;
    float rating;
    String vicinity;
    String address;

    ArrayList<Results> resultList = new ArrayList<>();

    public ParcelablesData(ArrayList<Results> resultList){
        this.resultList = resultList;
    }

    protected ParcelablesData(Parcel in) {
        for(int i=0; i<resultList.size(); i++){
            Results results = resultList.get(i);
            address = results.getFormatted_address();
            name = results.getName();
            icon = results.getIcon();
            Geometry geometry = results.getGeometry();
            LocationLatLng location = geometry.getLocation();
            latitude = location.getLat();
            longitude = location.getLng();
            rating = results.getRating();
        }
        this.address = in.readString();
        this.latitude=in.readDouble();
        this.longitude=in.readDouble();
        this.icon=in.readString();
        this.name=in.readString();
        this.rating=in.readFloat();
        this.vicinity=in.readString();
    }

    public static Creator<ParcelablesData> getCREATOR() {
        return CREATOR;
    }


    public static final Creator<ParcelablesData> CREATOR = new Creator<ParcelablesData>() {
        @Override
        public ParcelablesData createFromParcel(Parcel in) {
            return new ParcelablesData(in);
        }

        @Override
        public ParcelablesData[] newArray(int size) {
            return new ParcelablesData[size];
        }
    };

    @Override
    public int describeContents() {
        return resultList.size();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        /*parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(icon);
        parcel.writeString(name);
        parcel.writeFloat(rating);*/
        //parcel.writeString(vicinity);
    }

    public ArrayList<Results> getResultList() {
        return resultList;
    }
}
