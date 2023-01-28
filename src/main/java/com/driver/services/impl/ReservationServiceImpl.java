package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
               ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
               if(parkingLot==null) throw new Exception();
               User user = userRepository3.findById(userId).get();
               if(user==null)
                   throw new Exception();
               Spot spot = null;
               int min = Integer.MAX_VALUE;
               List<Spot> spotList = parkingLot.getSpotList();
               for(Spot spot1:spotList){
                   if(spot1.getPricePerHour()*timeInHours<min){
                       SpotType spotType = spot1.getSpotType();
                       if(spotType==SpotType.TWO_WHEELER && numberOfWheels==2)
                           spot = spot1;
                       else if (spotType==SpotType.FOUR_WHEELER && numberOfWheels<=4) {
                           spot = spot1;
                       }
                       else spot = spot1;
                   }
               }
               if(spot==null)
                   throw new Exception();

               Reservation reservation = new Reservation(numberOfWheels);
               reservation.setUser(user);
               reservation.setSpot(spot);
               List<Reservation> reservations = user.getReservationList();
               if(reservations==null){
                   reservations = new ArrayList<>();
               }
               reservations.add(reservation);
               user.setReservationList((com.sun.tools.javac.util.List<Reservation>) reservations);

               reservations = spot.getReservationList();
               if(reservations==null)
                   reservations = new ArrayList<>();
               spot.getReservationList().add(reservation);
               spot.setReservationList((com.sun.tools.javac.util.List<Reservation>) reservations);

               spotRepository3.save(spot);
               reservationRepository3.save(reservation);
               userRepository3.save(user);
               return reservation;
    }
}
