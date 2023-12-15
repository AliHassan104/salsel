package com.salsel.service;

import java.util.Map;

public interface BucketService {
    String save(byte[] pdf, String fileName);
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
//    List<String> getAllFiles();
    Map<String, String> getAllFilesWithUrls();
}
