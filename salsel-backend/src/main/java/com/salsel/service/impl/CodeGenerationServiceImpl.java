package com.salsel.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.salsel.service.BucketService;
import com.salsel.service.CodeGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {
    private final BucketService bucketService;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);


    public CodeGenerationServiceImpl(BucketService bucketService) {
        this.bucketService = bucketService;
    }
//
//    @Override
//    public Boolean generateBarcodeVertical(String data, Long awbId) {
//        try {
//            String filename = "vertical_barcode_" + awbId + ".png";
//
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 200, 80);
//            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//            BufferedImage rotatedBarcodeImage = rotateImage(barcodeImage);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//            try {
//                ImageIO.write(rotatedBarcodeImage, "PNG", outputStream);
//                outputStream.toByteArray();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            bucketService.save(outputStream.toByteArray(),filename);
//            logger.info("Vertical Barcode uploaded on S3");
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public String generateBarcodeVertical(String data, Long awbId) {
        try {
            String filename = "vertical_barcode_" + awbId + ".png";

            // Generate barcode image
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 200, 80);
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Rotate barcode image
            BufferedImage rotatedBarcodeImage = rotateImage(barcodeImage);

            // Convert rotated barcode image to byte array
            byte[] imageBytes = convertImageToByteArray(rotatedBarcodeImage);

            // Save to S3 bucket
            String url = bucketService.save(imageBytes, filename);
            logger.info("Vertical Barcode uploaded on S3");

            return url;
        } catch (Exception e) {
            logger.error("Error generating or uploading vertical barcode", e);
        }
        return null;
    }

    @Override
    public String generateBarcode(String data, Long awbId) {
        try {
            String filename = "barcode_" + awbId + ".png";

            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 200, 80);
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            byte[] imageBytes = convertImageToByteArray(barcodeImage);

            // Save to S3 bucket
            String url = bucketService.save(imageBytes, filename);
            logger.info("Barcode uploaded on S3");
            return url;

        } catch (Exception e) {
            logger.error("Error generating or uploading barcode", e);
        }
        return null;
    }

    @Override
    public String generateQRCode(String data, Long awbId) {
        try {
            String filename = "qrcode_" + awbId + ".png";

            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage qrcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            byte[] imageBytes = convertImageToByteArray(qrcodeImage);

            // Save to S3 bucket
            String url = bucketService.save(imageBytes, filename);
            logger.info("Qrcode uploaded on S3");

            return url;

        } catch (Exception e) {
            logger.error("Error generating or uploading qrcode", e);
        }
        return null;
    }

    private BufferedImage rotateImage(BufferedImage inputImage) {
        double radians = Math.toRadians(90);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(inputImage.getWidth() * cos + inputImage.getHeight() * sin);
        int newHeight = (int) Math.round(inputImage.getWidth() * sin + inputImage.getHeight() * cos);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, inputImage.getType());
        Graphics2D g = rotatedImage.createGraphics();
        g.translate((newWidth - inputImage.getWidth()) / 2, (newHeight - inputImage.getHeight()) / 2);
        g.rotate(radians, inputImage.getWidth() / 2.0, inputImage.getHeight() / 2.0);
        g.drawRenderedImage(inputImage, null);
        g.dispose();

        return rotatedImage;
    }

    private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", outputStream);
            return outputStream.toByteArray();
        }
    }
}
