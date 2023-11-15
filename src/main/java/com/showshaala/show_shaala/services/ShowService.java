package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ShowEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponse;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import java.util.List;

public interface ShowService {

  ServiceResponse<ShowResponseDto> addShow(ShowEntryDto showEntryDto);

  ServiceResponse<ShowResponseDto> getShow(Long id);

  ServiceResponse<List<ShowResponse>> getShowsByMovieDateAndCity(String date, Long movieId, String city);

//  List<Date> searchShowDatesByMovie(Integer movieId);

}
