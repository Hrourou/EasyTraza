/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
/**
 *
 * @author HAMZA
 */
@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/";
    private final String PROFILES_DIR = UPLOAD_DIR + "profiles/";
    private final String ALBARANS_DIR = UPLOAD_DIR + "albarans/";

    public FileStorageService() {
        createDirectoryIfNotExists(PROFILES_DIR);
        createDirectoryIfNotExists(ALBARANS_DIR);
    }

    private void createDirectoryIfNotExists(String dirPath) {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create directory: " + dirPath, e);
            }
        }
    }

    public String saveProfileImage(MultipartFile file) {
        return saveFile(file, PROFILES_DIR);
    }

    public String saveAlbaranImage(MultipartFile file) {
        return saveFile(file, ALBARANS_DIR);
    }

    private String saveFile(MultipartFile file, String targetDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Generate a unique filename to avoid overwriting
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            
            Path filePath = Paths.get(targetDir + uniqueFilename);
            Files.write(filePath, file.getBytes());

            // Return the public URL path
            return "/" + targetDir + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
