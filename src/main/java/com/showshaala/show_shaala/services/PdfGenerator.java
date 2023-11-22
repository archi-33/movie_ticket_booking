package com.showshaala.show_shaala.services;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.showshaala.show_shaala.entities.Ticket;
import com.showshaala.show_shaala.payload.InvoiceDto;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.repositories.PaymentRepo;
import com.showshaala.show_shaala.repositories.TicketRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PdfGenerator {
  @Autowired
  private TicketRepo ticketRepo;

  @Autowired
  private PaymentRepo paymentRepo;

  @Autowired
  private JavaMailSender mailSender;


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ServiceResponse<InvoiceDto> generateInvoice(Long bookingId) {
    try {
      Ticket booking = ticketRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking with this id not found"));
//      if (!booking.getStatus().toString().equalsIgnoreCase("confirmed")) {
//        return new ServiceResponse<>(false, null, "Please complete the payment");
//      }
      if(paymentRepo.findByTicketIdAndPaymentStatusPaid(bookingId)==null){
        return new ServiceResponse<>(false, null, "Please complete the payment");
      }
      byte[] pdfBytes = createPdfInvoice(booking);
      sendInvoiceByEmail(booking.getUser().getEmail(), pdfBytes);
      InvoiceDto invoiceDto = new InvoiceDto();
      invoiceDto.setPdfBytes(pdfBytes);
      return new ServiceResponse<>(true, invoiceDto, "Your Invoice generated successfully");
    } catch (Exception ex) {
      return new ServiceResponse<>(false, null, ex.getMessage());
    }
  }

  private void sendInvoiceByEmail(String email, byte[] pdfBytes) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setSubject("Invoice for Your Booking");
      helper.setText("Please find attached the invoice for your recent Booking.");
      helper.addAttachment("invoice.pdf", new ByteArrayResource(pdfBytes));
      mailSender.send(message);
    } catch (MessagingException ex) {
      log.error("Error sending invoice by email: " + ex.getMessage(), ex);
    }
  }

  private byte[] createPdfInvoice(Ticket booking) throws DocumentException, IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Document document = new Document();
    PdfWriter.getInstance(document, byteArrayOutputStream);
    document.open();
    document.add(new Paragraph("Booking ID #" + booking.getTicketId()));
    document.add(new Paragraph("Invoice for Booking :" + booking.getInvoice()));
    document.add(new Paragraph("Time :" + booking.getBookedAt()));
    document.add(new Paragraph("Time :" + booking.getShow().getScreen().getTheater().getName()));
    document.add(new Paragraph("City :" + booking.getShow().getScreen().getTheater().getCity()));
    document.add(new Paragraph("Show :" + booking.getShow()));
    document.add(new Paragraph("Booking Time :" + booking.getScreenName()));
    document.add(new Paragraph("Seat List :" + booking.getSeatList()));
    document.add(new Paragraph("Total Amount paid: " + booking.getAmount()));
    document.close();
    return byteArrayOutputStream.toByteArray();
  }


}
