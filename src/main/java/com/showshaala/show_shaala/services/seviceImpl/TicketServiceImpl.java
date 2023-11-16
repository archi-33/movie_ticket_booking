package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Payment;
import com.showshaala.show_shaala.entities.Show;
import com.showshaala.show_shaala.entities.ShowSeats;
import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.requestDto.BookingRequestDto;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.payload.responseDto.TheaterResponseDto;
import com.showshaala.show_shaala.providers.BookingStatus;
import com.showshaala.show_shaala.providers.PaymentStatus;
import com.showshaala.show_shaala.repositories.ShowRepo;
import com.showshaala.show_shaala.repositories.ShowSeatsRepo;
import com.showshaala.show_shaala.repositories.TicketRepo;
import com.showshaala.show_shaala.repositories.UserRepo;
import com.showshaala.show_shaala.services.PaymentService;
import com.showshaala.show_shaala.services.TicketService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketServiceImpl implements TicketService {

  @Autowired
  private ShowRepo showRepo;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ShowSeatsRepo showSeatsRepo;

  @Autowired
  private TicketRepo ticketRepo;

  @Autowired
  private ModelMapper modelMapper;


  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Override
  public ServiceResponse<?> bookTicket(BookingRequestDto bookingRequestDto, Principal principal) {
//    UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
//    String email = userDetails.getUsername();

    User user = userRepo.findByEmail(principal.getName()).get();

    Optional<Show> show = showRepo.findById(bookingRequestDto.getShowId());
    if (show.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find any show with the specified id");
    }
    List<ShowSeats> showSeatsList = show.get().getShowSeatList();
    List<ShowSeats> requestedSeats = new ArrayList<>();

    for (ShowSeats seat : showSeatsList) {
      if (seat.getStatus() == BookingStatus.FREE) {
        for (ShowSeatResponse req_seat : bookingRequestDto.getSeatList()) {
          if (Objects.equals(seat.getSeatNumber(), req_seat.getSeatNumber())) {
            if(requestedSeats.contains(seat))
              continue;
            requestedSeats.add(seat);
          }
        }
      }
    }
//    System.out.println(requestedSeats);
//    System.out.println("==============================================================================");

    double amount=0.0;

    for (ShowSeats seat : requestedSeats) {
      seat.lockSeats();
      seat.setShow(show.get());

      showSeatsRepo.save(seat);
      amount += seat.getRate();
    }
    if (amount > 100) {
      amount += amount * 18 / 100;
    } else {
      amount += amount * 12 / 100;
    }

//    List<ShowSeats> requestedSeats = bookTicketRequestDto.getRequestedSeats();
//    List<ShowSeats> showSeatsList = show.get().getShowSeatList();
//    showSeatsList.removeAll(seatsAvailabilityService.getUnavailableSeats(show.get()));

//    List<ShowSeats> bookSeats = new ArrayList<>();
//     for(ShowSeats seat : showSeatsList) {
//       seat.setStatus(BookingStatus.LOCKED);
//       showSeatsRepo.save(seat);
//       bookSeats.add(seat);
//
//     }

    Ticket ticket = Ticket.builder()
        .user(user)
        .show(show.get())
        .seatList(requestedSeats)
        .amount(amount)
        .build();

    for (ShowSeats seat : requestedSeats) {
      seat.setTicket(ticket);

    }

    ticket.setScreenName(ticket.getShow().getScreen().getScreenName());

    Payment pay = new Payment(ticket);
    ticket.setPayment(pay);

    Ticket savedTicket = ticketRepo.save(ticket);

    return new ServiceResponse<>(true, "ticketId: " + savedTicket.getTicketId(),
        "Please proceed to payment against the provided ticketId");

//    ticket.setPayment();

//    ticket.setPayment(paymentService.validatePayment(ticket));
//    ticketsRepo.save(ticket);

//    if (ticket.getPayment().getStatus() == PaymentStatus.SUCCESS) {
//      for(ShowSeats seat: ticket.getSeatList()){
//        seat.setStatus(BookingStatus.Booked);
//        seat.set_booked(true);
//        showSeatsRepo.save(seat);
//      }
//      List<Ticket> ticketList= ticket.getShow().getTicketList();
//      ticketList.add(ticket);
//      ticket.getShow().setTicketList(ticketList);
//      List<Ticket> ticketList1 =ticket.getUser().getTicketList();
//      ticketList1.add(ticket);
//      ticket.getUser().setTicketList(ticketList1);
//      showRepo.save(ticket.getShow());
//      userRepo.save(ticket.getUser());
//      TicketDto ticketDto = TicketDto.builder()
//          .screenName(ticket.getScreenName())
//          .amount(ticket.getAmount())
////          .user(ticket.getUser())
//          .seatList(ticket.getSeatList())
//          .show(ticket.getShow())
//          .build();
//      return new ResponseEntity<>(ticketDto, HttpStatus.OK);
//    }else{
////      for(ShowSeats seat: bookTicketRequestDto.getRequestedSeats()){
////        showSeatsRepo.save(seat);
////      }
////
//      return new ResponseEntity<>("Please pay to confirm your ticket.. ", HttpStatus.BAD_REQUEST);
//    }
  }

  @Override
  public ServiceResponse<List<BookingResponseDto>> bookingHistory(Principal principal) {
    User user = userRepo.findByEmail(principal.getName()).get();
    List<Ticket> ticketList;
    try {
//      ticketList = ticketRepo.findAllByUserId(user.getUserId(), PaymentStatus.PAID);
      ticketList = ticketRepo.findAllByUserId(user.getUserId());
    } catch (Exception e) {
      return new ServiceResponse<>(false, null, e.getMessage());
    }
    List<BookingResponseDto> finalList = new ArrayList<>();
    for (Ticket ticket : ticketList) {
      if (ticket.getPayment().getStatus() == PaymentStatus.FAIL) {
        continue;
      }
      List<ShowSeatResponse> seatResponses = new ArrayList<>();
      for (ShowSeats seat : ticket.getSeatList()) {
        seatResponses.add(modelMapper.map(seat, ShowSeatResponse.class));
      }
      BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
          .ticketId(ticket.getTicketId())
          .amount(ticket.getAmount())
          .theaterResponseDto(modelMapper.map(ticket.getShow().getScreen().getTheater(),
              TheaterResponseDto.class))
          .showResponseDto(modelMapper.map(ticket.getShow(), ShowResponseDto.class))
          .ScreenName(ticket.getScreenName())
          .seatList(seatResponses)
          .bookedAt(ticket.getBookedAt())
          .cancelled(ticket.isCancelled())
          .build();
      finalList.add(bookingResponseDto);

    }
    return new ServiceResponse<>(true, finalList, "here's the list of your bookings til date..");


  }

//  public ServiceResponse<?> cancelBooking(Long id, Principal principal) {
//
//    Optional<Ticket> ticket = ticketRepo.findById(id);
//
//    if (ticket.isPresent()) {
//      if(!Objects.equals(ticket.get().getUser().getEmail(), principal.getName())){
//        return new ServiceResponse<>(false, null, "you do not have any tickets associated with u with the specified id..");
//      }
//      Ticket t = ticket.get();
//      t.setCancelled(true);
//      ticketRepo.save(t);
//
//
////      List<ShowSeats> showSeats = t.getSeatList();
//      for(ShowSeats seat: t.getSeatList()){
//        seat.setAvailable();
//        showSeatsRepo.save(seat);
//      }
//
//
//      // Process the refund after cancellation charges
////      User user = t.getUser();
////      double refundAmount = t.getAmount() * 0.9;
//      paymentService.processRefund(t);
//    }else {
//      return new ServiceResponse<>(false, null, "cannot find any ticket with the specified id");
//    }
//    return null;
//  }

//  @Override
//  public Ticket getTicket(int id) {
//    return null;
//  }

}
