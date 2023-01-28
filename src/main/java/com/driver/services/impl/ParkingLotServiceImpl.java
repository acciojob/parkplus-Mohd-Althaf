package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name,address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour)
    {
        Spot spot = new Spot(pricePerHour);
        if(numberOfWheels==2)
            spot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels==4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else spot.setSpotType(SpotType.OTHERS);
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spotRepository1.save(spot);
        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {

        Spot spot = spotRepository1.findById(spotId).get();
        spotRepository1.delete(spot);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).get();

        List<Spot> parkinglist = spot.getParkingLot().getSpotList();
        for(Spot spot1:parkinglist){
            if(spot1.getId()==spotId){
                parkinglist.remove(spot1);
            }
        }
        spot.getParkingLot().setSpotList(parkinglist);


        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);

        parkingLot.getSpotList().add(spot);
        spotRepository1.save(spot);
        parkingLotRepository1.save(parkingLot);

return spot;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        parkingLotRepository1.delete(parkingLot);
    }
}
