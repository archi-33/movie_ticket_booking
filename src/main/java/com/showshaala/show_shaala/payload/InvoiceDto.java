package com.showshaala.show_shaala.payload;

public class InvoiceDto {
    private byte[] pdfBytes;

    public InvoiceDto() {
    }

    public InvoiceDto(byte[] pdfBytes) {
      this.pdfBytes = pdfBytes;
    }

    public byte[] getPdfBytes() {
      return pdfBytes;
    }

    public void setPdfBytes(byte[] pdfBytes) {
      this.pdfBytes = pdfBytes;
    }



}
