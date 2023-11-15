package com.showshaala.show_shaala.services;

import com.showshaala.show_shaala.entities.Screen;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.ScreenEntryDto;

public interface ScreenService {
  ServiceResponse<Screen> addScreenToTheater(ScreenEntryDto screenEntryDto);

}
