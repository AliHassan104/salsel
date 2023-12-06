package com.salsel.service;

import java.io.OutputStream;

public interface CodeGenerationService {
    Boolean generateBarcode(String data, Long awbId, OutputStream outputStream);
    Boolean generateBarcodeVertical(String data, Long awbId, OutputStream outputStream);
    Boolean generateQRCode(String data, Long awbId, OutputStream outputStream);
}
