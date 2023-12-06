package com.salsel.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.salsel.repository.AwbRepository;
import com.salsel.service.CodeGenerationService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {
    private final AwbRepository awbRepository;
    public CodeGenerationServiceImpl(AwbRepository awbRepository) {
        this.awbRepository = awbRepository;
    }
    @Override
    public Boolean generateBarcodeVertical(String data, Long awbId, OutputStream outputStream) {
        try {
            // Step 1: Get the path to the resources/static/ directory
            String staticDirectory = "src/main/resources/static/images/code";
            Path staticPath = Paths.get(staticDirectory);

            // Step 2: Create a Path object for the barcode image file
            String filename = "vertical_barcode_" + awbId + ".png";
            Path barcodeFilePath = staticPath.resolve(filename);

            // Step 3: Check if the file already exists. If it does, delete it to override it.
            if (Files.exists(barcodeFilePath)) {
                Files.delete(barcodeFilePath);
            }

            // Step 4: Create the file and write the barcode image to it
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 200, 80);
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Step 6: Rotate the image vertically
            BufferedImage rotatedBarcodeImage = rotateImage(barcodeImage);

            // Step 7: Save the modified image to a file
            try (OutputStream fileOutputStream = Files.newOutputStream(barcodeFilePath.toFile().toPath())) {
                ImageIO.write(rotatedBarcodeImage, "PNG", fileOutputStream);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean generateBarcode(String data, Long awbId, OutputStream outputStream) {
        try {
            // Step 1: Get the path to the resources/static/ directory
            String staticDirectory = "src/main/resources/static/images/code";
            Path staticPath = Paths.get(staticDirectory);

            // Step 2: Create a Path object for the barcode image file
            String filename = "barcode_" + awbId + ".png";
            Path barcodeFilePath = staticPath.resolve(filename);

            // Step 3: Check if the file already exists. If it does, delete it to override it.
            if (Files.exists(barcodeFilePath)) {
                Files.delete(barcodeFilePath);
            }

            // Step 4: Create the file and write the barcode image to it
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 200, 80);

            try (OutputStream fileOutputStream = Files.newOutputStream(barcodeFilePath)) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", fileOutputStream);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {

        }
    }

    @Override
    public Boolean generateQRCode(String data, Long awbId, OutputStream outputStream) {
        try {
            String staticDirectory = "src/main/resources/static/images/code";
            Path staticPath = Paths.get(staticDirectory);

            String filename = "qrcode_" + awbId + ".png";
            Path qrcodeFilePath = staticPath.resolve(filename);

            if (Files.exists(qrcodeFilePath)) {
                Files.delete(qrcodeFilePath);
            }

            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
            try (OutputStream fileOutputStream = Files.newOutputStream(qrcodeFilePath)) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", fileOutputStream);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
}
