package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.Payment;
import com.showshaala.show_shaala.entities.Refund;
import com.showshaala.show_shaala.entities.ShowSeats;
import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.InvoiceDto;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.responseDto.BookingResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowResponseDto;
import com.showshaala.show_shaala.payload.responseDto.ShowSeatResponse;
import com.showshaala.show_shaala.payload.responseDto.TheaterResponseDto;
import com.showshaala.show_shaala.providers.BookingStatus;
import com.showshaala.show_shaala.providers.PaymentStatus;
import com.showshaala.show_shaala.providers.RefundStatus;
import com.showshaala.show_shaala.repositories.PaymentRepo;
import com.showshaala.show_shaala.repositories.RefundRepo;
import com.showshaala.show_shaala.repositories.ShowRepo;
import com.showshaala.show_shaala.repositories.ShowSeatsRepo;
import com.showshaala.show_shaala.repositories.TicketRepo;
import com.showshaala.show_shaala.repositories.UserRepo;
import com.showshaala.show_shaala.services.PaymentService;
import com.showshaala.show_shaala.services.PdfGenerator;
import com.showshaala.show_shaala.services.TicketService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
  @Autowired
  private RefundRepo refundRepo;
  @Autowired
  private TicketService ticketService;

  @Autowired
  private PdfGenerator pdfGenerator;

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Override
  public ServiceResponse<BookingResponseDto> pay(Long ticketId, double amount,
      Principal principal) {
    Optional<Ticket> ticket = ticketRepo.findById(ticketId);
    if (ticket.isEmpty()) {
      return new ServiceResponse<>(false, null, "no ticket is present with the specified ticketId");
    }

    if (!Objects.equals(ticket.get().getUser().getEmail(), principal.getName())) {
      return new ServiceResponse<>(false, null,
          "no ticket registered with this id against your user id..");
    }

    if (ticket.get().getSeatList().get(0).getStatus() == BookingStatus.FREE) {
      return new ServiceResponse<>(false, null, "ticket expired.. try booking again");
    }

    Payment payment;
    try {
      payment = paymentRepo.findByTicketId(ticketId);
    } catch (Exception e) {
      return new ServiceResponse<>(false, null, e.getMessage());
    }

//    List<ShowSeats> seats= ticket.get().getSeatList();
//    ShowSeats seat = seats.get(0);

//    if (status == PaymentStatus.FAIL) {
//      payment.setStatus(PaymentStatus.FAIL);
//      paymentRepo.save(payment);
//      return new ServiceResponse<>(false, null, "Please pay.. to confirm your booking");
//    }

    if (paymentRepo.findByTicketIdAndPaymentStatusPaid(ticketId)!=null) {
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
      return new ServiceResponse<>(false, bookingResponseDto,
          "you have already paid. Here's your ticket details..");
    }

    if (ticket.get().getAmount() == amount) {

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

      for (ShowSeats seat : ticket.get().getSeatList()) {
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
      payment.setPaidAmt(ticket.get().getAmount());
      paymentRepo.save(payment);
//      ServiceResponse<InvoiceDto> invoiceResponse = pdfGenerator.generateInvoice(ticketId);

//      if (invoiceResponse.getSuccess()) {
//        byte[] pdfBytes = invoiceResponse.getData().getPdfBytes();
//      }
      return new ServiceResponse<>(true, bookingResponseDto,
          "your ticket is confirmed...ENJOY YOUR MOVIE");

    }
    payment.setPaidAmt(amount);
    paymentRepo.save(payment);

    return new ServiceResponse<>(false, null,
        "you have not paid the initial amount to be paid... please try booking again if money got deducted it will be refund in 3 business days..");
  }

  @Override
  public ServiceResponse<?> cancelBooking(Long id, Principal principal) {

    Optional<Ticket> ticket = ticketRepo.findById(id);
    if (ticket.isEmpty()) {
      return new ServiceResponse<>(false, null, "cannot find any ticket with the specified id");
    }

    if (ticket.get().isCancelled()) {
      return new ServiceResponse<>(false, null,
          "you cancelled this ticked and refund has been initiated..");
    }


    if (!Objects.equals(ticket.get().getUser().getEmail(), principal.getName())) {
      return new ServiceResponse<>(false, null,
          "you do not have any tickets associated with u with the specified id..");
    }
    Ticket t = ticket.get();
    t.setCancelled(true);
    for (ShowSeats seat : t.getSeatList()) {
//        seat.setAvailable();
      seat.setStatus(BookingStatus.FREE);
      showSeatsRepo.save(seat);
    }
    ticketRepo.save(t);
    paymentRepo.findByTicketIdAndPaymentStatusPaid(t.getTicketId());

//      List<ShowSeats> showSeats = t.getSeatList();


    // Process the refund after cancellation charges
//      User user = t.getUser();
//      double refundAmount = t.getAmount() * 0.9;
    return processRefund(t);


  }

  @Override
  public ServiceResponse<?> processRefund(Ticket t) {

    User user = t.getUser();
    double refundAmount = t.getAmount() * 0.9;
//    t.getPayment().setPaidAmt(t.getAmount() - refundAmount);
    Payment pay = paymentRepo.findByTicketIdAndPaymentStatusPaid(t.getTicketId());
    pay.setPaidAmt(t.getAmount() - refundAmount);
    paymentRepo.save(pay);
    Refund refund = Refund.builder()
        .payment(pay)
        .refundAt(new Date())
        .status(RefundStatus.PAID)
        .build();
    refundRepo.save(refund);
    return new ServiceResponse<>(true, null,
        "we have initiated the refund of amount: " + refundAmount
            + " .... after the cancellation charges... "+"with refund id:"+ refund.getId());
  }

}
