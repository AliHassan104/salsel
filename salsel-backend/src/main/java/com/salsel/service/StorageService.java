package com.salsel.service;

public interface StorageService {
    String uploadFile(byte[] fileData, String fileName);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);
}
