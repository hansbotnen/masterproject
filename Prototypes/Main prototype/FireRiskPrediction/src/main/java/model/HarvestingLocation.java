package model;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
public class HarvestingLocation {

    private @Id
    ObjectId id;
    private double latitude;

    private double longitude;

    private double nearestLat;

    private double nearestLong;
    private int count; //number of subscribers

    public HarvestingLocation(){};

    public HarvestingLocation(double px, double py, double nx, double ny){
        latitude=py;
        longitude=px;
        nearestLat=ny;
        nearestLong=nx;
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
