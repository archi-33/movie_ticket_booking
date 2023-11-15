package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import java.util.List;
import lombok.NonNull;

public interface SeatAvlService {

  ServiceResponse<List<ShowSeatResponse>> getAvailableSeats(@NonNull Long showId);

}
