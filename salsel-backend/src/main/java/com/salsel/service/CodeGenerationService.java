package com.salsel.service;

import java.io.OutputStream;

public interface CodeGenerationService {
    String generateBarcode(String data, Long awbId);
    String generateBarcodeVertical(String data, Long awbId);
   String generateQRCode(String data, Long awbId);
}
