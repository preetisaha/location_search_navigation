package com.googlelocationapi.Data;

public class Geometry {

    LocationLatLng location;
    Viewport viewport;

    public LocationLatLng getLocation() {
        return location;
    }

    public void setLocation(LocationLatLng location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
