package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.entities.Theater;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ScreenEntryDto;
import com.showshaala.show_shaala.payload.requestDto.TheaterEntryDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import java.util.List;

public interface TheaterService {
  ServiceResponse<Theater> addTheater(TheaterEntryDto theaterEntryDto);

  ServiceResponse<Theater> getTheater(Long id);


  ServiceResponse<List<Theater>> fetchByZipcode(Integer zipcode);

  ServiceResponse<List<ShowResponseDto>> fetchAllShowsInTheater(Long theaterId);

//  List<Theater> fetchAllTheatersByMovie(Integer movieId);
  //  ServiceResponse<Screen> createScreensInTheater(ScreenEntryDto screenEntryDto);

  ServiceResponse<List<Theater>> fetchByCity(String city);




}
