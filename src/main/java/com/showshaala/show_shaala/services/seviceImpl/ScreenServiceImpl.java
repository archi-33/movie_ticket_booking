package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.entities.ScreenSeats;
import com.showshaala.show_shaala.entities.Theater;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ScreenEntryDto;
import com.showshaala.show_shaala.repositories.ScreenRepo;
import com.showshaala.show_shaala.repositories.TheaterRepo;
import com.showshaala.show_shaala.services.ScreenService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScreenServiceImpl implements ScreenService {
  @Autowired
  private TheaterRepo theaterRepo;
  @Autowired
  private ScreenRepo screenRepo;

  @Override
  public ServiceResponse<Screen> addScreenToTheater(ScreenEntryDto screenEntryDto) {
    Optional<Theater> theater = theaterRepo.findById(screenEntryDto.getTheaterId());
    if (theater.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find theater with specified theaterId");
    }
    Screen screen = new Screen();
    screen.setTheater(theater.get());
    screen.setScreenName(screenEntryDto.getScreenName());
    screen.setTotalNoOfSeats(screenEntryDto.getTotalNoOfSeats());

    List<ScreenSeats> screenSeats = new ArrayList<>();
    char ch = 'A';
    int j =1;
    for (int i = 1; i <= screenEntryDto.getTotalNoOfSeats(); i++) {
      ScreenSeats screenSeat = new ScreenSeats();
      if(i==12){
        j=1;
        ch++;
      }
      screenSeat.setSeatNumber((j++)+""+ch);
      screenSeats.add(screenSeat);
      screenSeat.setScreen(screen);
    }
    screen.setScreenSeats(screenSeats);

    Screen savedScreen = screenRepo.save(screen);
    if(savedScreen==null)
      return  new ServiceResponse<>(false, null, "cannot save the screen");
    return new ServiceResponse<>(true, savedScreen, "successfully saved the screen ");
  }
}
