package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.entities.ScreenSeats;
import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.entities.Theater;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ScreenEntryDto;
import com.showshaala.show_shaala.payload.requestDto.TheaterEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.repositories.MovieRepo;
import com.showshaala.show_shaala.repositories.ScreenRepo;
import com.showshaala.show_shaala.repositories.ScreenSeatsRepo;
import com.showshaala.show_shaala.repositories.TheaterRepo;
import com.showshaala.show_shaala.services.TheaterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TheaterServiceImpl implements TheaterService {

  @Autowired
  private TheaterRepo theaterRepo;

  @Autowired
  private ScreenRepo screenRepo;

  @Autowired
  private ScreenSeatsRepo screenSeatsRepo;

  @Autowired
  private ScreenSeatsRepo theaterSeatsRepo;
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MovieRepo movieRepo;

  @Override
  public ServiceResponse<Theater> addTheater(TheaterEntryDto theaterEntryDto) {
//    Theater theater = Theater.builder()
//        .name(theaterEntryDto.getName())
//        .city(theaterEntryDto.getCity())
//        .zipcode(theaterEntryDto.getZipcode())
//        .build();
    Theater theater = modelMapper.map(theaterEntryDto, Theater.class);

//    theaterRepo.insertTheatreByParam(theater.getName(), theater.getAddress(), theater.getZipcode());
    Theater createdTheater = theaterRepo.save(theater);
    log.info("Theater saved: {}", createdTheater);
    if(createdTheater == null)
      return new ServiceResponse<>(false, null, "cannot create theater");

    return new ServiceResponse<>(true, createdTheater, "created a new theater...");
  }

  @Override
  public ServiceResponse<Theater> getTheater(Long id) {
    Optional<Theater> theater = theaterRepo.findById(id);
    if (theater.isPresent()) {
      return new ServiceResponse<>(true, theater.get(), "theater found");
    } else {
      return new ServiceResponse<>(false, null, "cannot find theater with specified theaterId");
    }
  }

  @Override
  public ServiceResponse<List<Theater>> fetchByZipcode(Integer zipcode) {
    List<Theater> foundList = theaterRepo.findAllByZipcode(zipcode);
    return new ServiceResponse<>(true, foundList, "found list of theaters");
  }


  @Override
  public ServiceResponse<List<ShowResponseDto>> fetchAllShowsInTheater(Long theaterId) {
    Optional<Theater> theater = theaterRepo.findById(theaterId);
    if (theater.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find theater with specified theaterId");
    }

    List<Show> listOfShows = new ArrayList<>();
    List<ShowResponseDto> shows = new ArrayList<>();
    for (Screen screen : theater.get().getScreens()) {
      listOfShows.addAll(screen.getShows());
    }
    for (Show show : listOfShows) {
      shows.add(modelMapper.map(show, ShowResponseDto.class));
    }
    return new ServiceResponse<>(true, shows, "found list of shows in theaterId: " + theaterId);
  }

  @Override
  public ServiceResponse<List<Theater>> fetchByCity(String city) {
    List<Theater> foundList;
    try {
      foundList = theaterRepo.findAllByCity(city);
    }catch (Exception e){
      return new ServiceResponse<>(false, null, e.getMessage());
    }
    return new ServiceResponse<>(true, foundList, "found list of theaters");
  }

//  @Override
//  public ServiceResponse<Screen> createScreensInTheater(ScreenEntryDto screenEntryDto) {
//    Optional<Theater> theater = theaterRepo.findById(screenEntryDto.getTheaterId());
//    if (theater.isEmpty()) {
//      return new ServiceResponse<>(false, null, "cannot find theater with specified theaterId");
//    }
//    Screen screen = createScreen(screenEntryDto.getScreenName(), screenEntryDto.getTotalNoOfSeats(),
//        theater.get());
//
//    theater.get().addScreenToTheater(screen);
////    Screen createdScreen = screenRepo.save(screen);
//
////    Theater createdTheater = theaterRepo.save(theater.get());
//
//    return new ServiceResponse<>(true, screen,
//        "successfully created a new screen in a theater..");
//  }

//  private Screen createScreen(final String screenName, Integer totalNoOfSeats,
//      final Theater theater) {
//    Screen screen = Screen.builder().screenName(screenName).totalNoOfSeats(totalNoOfSeats).build();
//    screen.setTheater(theater);
//
//    Screen createdScreen = screenRepo.save(screen);
//
//    Screen screen1 = generateSeatsInScreen(screen);
//    return createdScreen;
//  }
//
//  public Screen generateSeatsInScreen(Screen screen) {
//    ScreenSeats s1 = new ScreenSeats("1A", 100);
//    ScreenSeats s2 = new ScreenSeats("2A", 100);
//    ScreenSeats s3 = new ScreenSeats("3A", 100);
//    ScreenSeats s4 = new ScreenSeats("4A", 100);
//    ScreenSeats s5 = new ScreenSeats("5A", 100);
//    ScreenSeats s6 = new ScreenSeats("6A", 100);
//    ScreenSeats s7 = new ScreenSeats("7A", 100);
//    ScreenSeats s8 = new ScreenSeats("8A", 100);
//    ScreenSeats s9 = new ScreenSeats("9A", 100);
//    ScreenSeats s10 = new ScreenSeats("10A", 100);
//    List<ScreenSeats> seats = List.of(new ScreenSeats[]{s1, s2, s3, s4, s5, s6, s7, s8, s9, s10});
//    createScreenSeats(screen, seats);
//
//    screen.addScreenSeatsToScreen(seats);
//    return screen;
//
////    screenRepo.save(screen);
//  }
//
//  public void createScreenSeats(Screen screen, List<ScreenSeats> seats) {
//    for (ScreenSeats seat : seats) {
//      seat.setScreen(screen);
//      screenSeatsRepo.save(seat);
//    }
//  }



//  @Override
//  public List<Theater> fetchAllTheatersByMovie(Integer movieId) {
//    return null;
//  }

}
