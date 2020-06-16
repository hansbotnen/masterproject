package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
@IdClass(HarvestingLocationId.class)
public class HarvestingLocation {
    @Id
    private double latitude;
    @Id
    private double longitude;
    @Id
    private double nearestLat;
    @Id
    private double nearestLong;
    private int count; //number of subscribers

    public HarvestingLocation(){};

    public HarvestingLocation(double px, double py, double nx, double ny){
        latitude=px;
        longitude=py;
        nearestLat=nx;
        nearestLong=ny;
        count=1;
    }

    public void increaseCount(){
        count++;
    }

    public void decreaseCount(){
        count--;
    }

    @Override
    public String toString() {
        return "Primary location: ("+latitude+", "+longitude+
                "), nearest station: ("+nearestLat+", "+nearestLong+")";
    }
}
