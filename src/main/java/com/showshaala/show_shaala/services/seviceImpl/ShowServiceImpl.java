package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Movie;
import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.entities.ScreenSeats;
import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.entities.ShowSeats;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ShowEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.payload.responseDto.TheaterResponseDto;
import com.showshaala.show_shaala.providers.BookingStatus;
import com.showshaala.show_shaala.repositories.MovieRepo;
import com.showshaala.show_shaala.repositories.ScreenRepo;
import com.showshaala.show_shaala.repositories.ShowRepo;
import com.showshaala.show_shaala.repositories.ShowSeatsRepo;
import com.showshaala.show_shaala.services.ShowService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowServiceImpl implements ShowService {

  @Autowired
  private ShowRepo showRepo;

  @Autowired
  private ScreenRepo screenRepo;

  @Autowired
  private MovieRepo movieRepo;

  @Autowired
  private ShowSeatsRepo showSeatsRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ServiceResponse<ShowResponseDto> addShow(ShowEntryDto showEntryDto) {
    Optional<Screen> foundScreen = screenRepo.findById(showEntryDto.getScreenId());
    Optional<Movie> foundMovie = movieRepo.findById(showEntryDto.getMovieId());

    if (foundScreen.isEmpty() && foundMovie.isEmpty()) {
      return new ServiceResponse<>(false, null,
          "cannot find any screen or movie with the specified id..");
    } else if (foundScreen.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find screen with the specified id..");
    } else if (foundMovie.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find movie with the specified id..");
    }

    DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate showDate;
    LocalTime startTime;
    LocalTime endTime;
    try {
      showDate = LocalDate.parse(showEntryDto.getShowDate(), f1);
    } catch (DateTimeParseException e) {
      return new ServiceResponse<>(false, null, e.getMessage());
    }

    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalDate currentDate = currentDateTime.toLocalDate();

    if (showDate.isBefore(currentDate)) {
      return new ServiceResponse<>(false, null, "wrong showDate...");
    }
    if(showDate.isBefore(foundMovie.get().getReleaseDate())){
      return new ServiceResponse<>(false, null, "the movie you are trying to add is not released on the showDate ");
    }


    DateTimeFormatter f2 = DateTimeFormatter.ofPattern("HH:mm:ss");
    try {
      startTime = LocalTime.parse(showEntryDto.getStartTime(), f2);
      endTime = LocalTime.parse(showEntryDto.getEndTime(), f2);
    } catch (DateTimeParseException e) {
      return new ServiceResponse<>(false, null, e.getMessage());
    }
    LocalTime expectedEndTime = startTime.plus(foundMovie.get().getMovieDuration());
    if(expectedEndTime.isAfter(endTime)){
      return  new ServiceResponse<>(false, null, "show ends before the movie duration...");
    }

    Date currentInstant = Date.from(new Date().toInstant());

    Show show = Show.builder()
        .showDate(showDate)
        .startTime(startTime)
        .endTime(endTime)
        .createdOn(currentInstant)
        .movie(foundMovie.get())
        .screen(foundScreen.get())
        .build();

//    screen.getShows().add(show);

    foundScreen.get().addShowToScreen(show);
    Screen screen = screenRepo.save(foundScreen.get());
    generateShowEntitySeats(show.getScreen().getScreenSeats(), show);

    Show savedShow = showRepo.save(show);

    generateShowEntitySeats(show.getScreen().getScreenSeats(), show);

    return new ServiceResponse<>(true, modelMapper.map(savedShow, ShowResponseDto.class),
        "a new show is added");
  }

  void generateShowEntitySeats(List<ScreenSeats> seats, Show show) {
    List<ShowSeats> showSeatsList = new ArrayList<>();

    for (ScreenSeats seat : seats) {
      ShowSeats showSeats = ShowSeats.builder().seatNumber(seat.getSeatNumber()).
          rate(seat.getRate()).status(BookingStatus.FREE).build();

      showSeats.setShow(show);

      showSeatsList.add(showSeats);
    }

    showSeatsRepo.saveAll(showSeatsList);

    show.setShowSeatList(showSeatsList);
    showRepo.save(show);

  }

  @Override
  public ServiceResponse<ShowResponseDto> getShow(Long id) {
    Optional<Show> show = showRepo.findById(id);
    if (show.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find any show with the specified id..");
    }
    ShowResponseDto showResponseDto = modelMapper.map(show, ShowResponseDto.class);
    return new ServiceResponse<>(true, showResponseDto, "found the show..");
  }

  @Override
  public ServiceResponse<List<ShowResponse>> getShowsByMovieDateAndCity(String date, Long movieId, String city) {
    LocalDate showDate;
    try {
      showDate = LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      return new ServiceResponse<>(false, null, e.getMessage());
    }
    Optional<Movie> foundMovie = movieRepo.findById(movieId);
    if (foundMovie.isEmpty()) {
      return new ServiceResponse<>(false, null, "no movie exists by specified id...");
    }

    List<Show> shows = showRepo.findShowsByDateMovieAndCity(showDate, movieId, city);
    List<ShowResponse> showResponseList = new ArrayList<>();
    for(Show show: shows){
      ShowResponse showResponse = modelMapper.map(show, ShowResponse.class);
      showResponse.setMovieTitle(show.getMovie().getTitle());
      showResponse.setTheaterDetails(modelMapper.map(show.getScreen().getTheater(),
          TheaterResponseDto.class));
      showResponseList.add(showResponse);
    }

    return new ServiceResponse<>(true, showResponseList, "found shows...");
  }


  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Scheduled(fixedRate = 60000) //  runs every minute
  public void scheduledUnlock() {
    unlockSeat();
  }

  public void unlockSeat() {
    List<Show> showList = showRepo.findAll();
    List<ShowSeats> showSeatsList = new ArrayList<>();
    for (Show show : showList) {
      showSeatsList.addAll(show.getShowSeatList());
    }
    for (ShowSeats seat : showSeatsList) {
      if (seat.getStatus() == BookingStatus.LOCKED) {
        if (seat.isLockExpired()) {
          seat.setStatus(BookingStatus.FREE);
          showSeatsRepo.save(seat);
        }
      }
    }

  }

//  @Override
//  public List<Date> searchShowDatesByMovie(Integer movieId) {
//    List<Date> showsDate =showRepo.findAllByMovie(movieId);
//
//    return showsDate;
//  }

}
