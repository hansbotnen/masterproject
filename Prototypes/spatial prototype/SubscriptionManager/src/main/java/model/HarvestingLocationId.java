package model;

import java.io.Serializable;
import java.util.Objects;

public class HarvestingLocationId implements Serializable {
    private double longitude;
    private double latitude;
    private double nearestLong;
    private double nearestLat;

    public HarvestingLocationId(){}
    public HarvestingLocationId(double longitude, double latitude, double nearestLong, double nearestLat) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.nearestLong = nearestLong;
        this.nearestLat = nearestLat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarvestingLocationId that = (HarvestingLocationId) o;
        return Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.nearestLong, nearestLong) == 0 &&
                Double.compare(that.nearestLat, nearestLat) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, nearestLong, nearestLat);
    }
}
