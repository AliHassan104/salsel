package com.salsel.service;

import java.util.Map;

public interface BucketService {
    String save(byte[] pdf, String folderName, String fileName, String folderType);
    byte[] downloadFile(String folderName, String fileName);
    void deleteFile(String fileName);
    void deleteFolder(String folderName);
//    List<String> getAllFiles();
    Map<String, String> getAllFilesWithUrls();
}
