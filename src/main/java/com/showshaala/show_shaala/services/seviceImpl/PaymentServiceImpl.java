package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Payment;
import com.showshaala.show_shaala.entities.ShowSeats;
import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.payload.responseDto.TheaterResponseDto;
import com.showshaala.show_shaala.providers.BookingStatus;
import com.showshaala.show_shaala.providers.PaymentStatus;
import com.showshaala.show_shaala.repositories.PaymentRepo;
import com.showshaala.show_shaala.repositories.ShowRepo;
import com.showshaala.show_shaala.repositories.ShowSeatsRepo;
import com.showshaala.show_shaala.repositories.TicketRepo;
import com.showshaala.show_shaala.repositories.UserRepo;
import com.showshaala.show_shaala.services.PaymentService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

  @Autowired
  private TicketRepo ticketRepo;
  @Autowired
  private ShowSeatsRepo showSeatsRepo;
  @Autowired
  private ShowRepo showRepo;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private PaymentRepo paymentRepo;

  @Override
  public ServiceResponse<BookingResponseDto> pay(Long ticketId, PaymentStatus status,
      Principal principal) {
    Optional<Ticket> ticket = ticketRepo.findById(ticketId);
    Payment payment;
    try{
      payment = paymentRepo.findByTicketId(ticketId);
    }catch(Exception e){
      return new ServiceResponse<>(false, null, e.getMessage());
    }





    if (ticket.isEmpty()) {
      return new ServiceResponse<>(false, null, "no ticket is present with the specified ticketId");
    }
    if (status == PaymentStatus.FAIL) {
      payment.setStatus(PaymentStatus.FAIL);
      paymentRepo.save(payment);
      return new ServiceResponse<>(false, null, "Please pay.. to confirm your booking");
    }
    if(ticket.get().getPayment().getStatus() == PaymentStatus.PAID){
      List<ShowSeatResponse> seatResponses = new ArrayList<>();
      for (ShowSeats seat : ticket.get().getSeatList()) {
        seatResponses.add(modelMapper.map(seat, ShowSeatResponse.class));
      }
      BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
          .ticketId(ticketId)
          .amount(ticket.get().getAmount())
          .theaterResponseDto(modelMapper.map(ticket.get().getShow().getScreen().getTheater(),
              TheaterResponseDto.class))
          .showResponseDto(modelMapper.map(ticket.get().getShow(), ShowResponseDto.class))
          .ScreenName(ticket.get().getScreenName())
          .seatList(seatResponses)
          .bookedAt(ticket.get().getBookedAt())
          .build();
      return new ServiceResponse<>(false, bookingResponseDto, "you have already paid. Here's your ticket details..");
    }
    if (!Objects.equals(ticket.get().getUser().getEmail(), principal.getName())) {
      return new ServiceResponse<>(false, null,
          "no ticket registered with this id against your user id..");
    }
    List<ShowSeatResponse> seatResponses = new ArrayList<>();
    for (ShowSeats seat : ticket.get().getSeatList()) {
      seat.setStatus(BookingStatus.BOOKED);
      showSeatsRepo.save(seat);
    }

    List<Ticket> ticketList = ticket.get().getShow().getTicketList();
    ticketList.add(ticket.get());
    ticket.get().getShow().setTicketList(ticketList);
    List<Ticket> ticketList1 = ticket.get().getUser().getTicketList();
    ticketList1.add(ticket.get());
    ticket.get().getUser().setTicketList(ticketList1);
    ticket.get().setBookedAt(new Date());
    showRepo.save(ticket.get().getShow());
    userRepo.save(ticket.get().getUser());

    for(ShowSeats seat : ticket.get().getSeatList()){
      seatResponses.add(modelMapper.map(seat, ShowSeatResponse.class));
    }
//    TicketDto ticketDto = TicketDto.builder()
//        .screenName(ticket.getScreenName())
//        .amount(ticket.getAmount())
//        .user(ticket.getUser())
//        .seatList(ticket.getSeatList())
//        .show(ticket.getShow())
//        .build();

    BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
        .ticketId(ticketId)
        .amount(ticket.get().getAmount())
        .theaterResponseDto(modelMapper.map(ticket.get().getShow().getScreen().getTheater(),
            TheaterResponseDto.class))
        .showResponseDto(modelMapper.map(ticket.get().getShow(), ShowResponseDto.class))
        .ScreenName(ticket.get().getScreenName())
        .seatList(seatResponses)
        .bookedAt(ticket.get().getBookedAt())
        .build();
    payment.setStatus(PaymentStatus.PAID);
    paymentRepo.save(payment);
    return new ServiceResponse<>(true, bookingResponseDto, "your ticket is confirmed...ENJOY YOUR MOVIE");

}

}
