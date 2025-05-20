package com.TheProfessorsPlate.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.http.Part;

public class ImageUtil {
    // Fix redundant path
    private static final String UPLOAD_BASE_PATH = "E:/WorkShop/New folder/TheProfessorsPlate/src/main/webapp/resources/usersImage";

    public String getImageNameFromPart(Part part) {
        if (part == null) {
            return null;
        }

        // Retrieve the content-disposition header from the part
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) {
            return null;
        }

        // Split the header by semicolons to isolate key-value pairs
        String[] items = contentDisp.split(";");

        // Initialize imageName variable to store the extracted file name
        String originalFileName = null;

        // Iterate through the items to find the filename
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                // Extract the file name from the header value
                originalFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                break;
            }
        }

        // If no file was uploaded, return null
        if (originalFileName == null || originalFileName.isEmpty()) {
            return null;
        }

        // Generate unique filename with UUID
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return "";
    }

    public boolean uploadImage(Part part, String rootPath, String saveFolder) {
        if (part == null || part.getSize() == 0) {
            return true; // No image to upload is not an error
        }

        String savePath = getSavePath(saveFolder);
        File fileSaveDir = new File(savePath);

        // Ensure the directory exists
        if (!fileSaveDir.exists()) {
            if (!fileSaveDir.mkdirs()) {
                return false; // Failed to create the directory
            }
        }

        try {
            // Get the image name
            String userImage = getImageNameFromPart(part);
            if (userImage == null) {
                return true; // No image to upload is not an error
            }

            // Create the file path
            String filePath = savePath + File.separator + userImage;
            
            // Write the file to the server
            part.write(filePath);
            return true; // Upload successful
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            return false; // Upload failed
        }
    }
    
    public String getSavePath(String saveFolder) {
        return UPLOAD_BASE_PATH + File.separator + saveFolder;
    }
}