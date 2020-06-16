package model;

import lombok.Data;

import java.util.List;

@Data
public class HarvestingLocationList {
    List<HarvestingLocation> locations;

    public HarvestingLocationList(List<HarvestingLocation> locations) {
        this.locations = locations;
    }

    public HarvestingLocationList(){}
}
