package com.salsel.service;

import java.util.List;
import java.util.Map;

public interface BucketService {
    String save(byte[] pdf, String folderName, String fileName, String folderType);
    List<String> saveMultipleFiles(Map<String, byte[]> files, String folderName, String folderType);
    byte[] downloadFile(String folderName, String fileName, String folderType);
    void deleteFile(String fileName);
    void deleteFileAtPath(String folderKey, String fileName);
    void deleteFilesStartingWith(String folderKey, String prefix);
    void deleteFolder(String folderName);
//    List<String> getAllFiles();
    Map<String, String> getAllFilesWithUrls();
}
