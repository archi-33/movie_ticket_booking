package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.entities.ShowSeats;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.providers.BookingStatus;
import com.showshaala.show_shaala.repositories.ShowRepo;
import com.showshaala.show_shaala.repositories.ShowSeatsRepo;
import com.showshaala.show_shaala.services.SeatAvlService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatAvlServiceImpl implements SeatAvlService {

  @Autowired
  private ShowSeatsRepo showSeatsRepo;

  @Autowired
  private ShowRepo showRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ServiceResponse<List<ShowSeatResponse>> getAvailableSeats(@NonNull Long showId) {
    Optional<Show> show = showRepo.findById(showId);
    if (show.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find show with the specified id");
    }

    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalDate currentDate = currentDateTime.toLocalDate();

    if (show.get().getShowDate().isBefore(currentDate)) {
      return new ServiceResponse<>(false, null, "the show is no longer available");
    }

    List<ShowSeats> showSeatList = show.get().getShowSeatList();
    List<ShowSeatResponse> availableSeats = new ArrayList<>();

    for (ShowSeats seat : showSeatList) {
      if (seat.getStatus() == BookingStatus.FREE) {
        availableSeats.add(modelMapper.map(seat, ShowSeatResponse.class));
      } else if (seat.getStatus() == BookingStatus.LOCKED) {
        if (seat.isLockExpired()) {
          seat.setStatus(BookingStatus.FREE);
          showSeatsRepo.save(seat);
          availableSeats.add(modelMapper.map(seat, ShowSeatResponse.class));
        }
      }

    }
//    List<ShowSeats> unavailableSeats = getUnavailableSeats(show);

//    availableSeats.removeAll(unavailableSeats);
    return new ServiceResponse<>(true, availableSeats,
        "found list of available seats in the show..");
  }

}
