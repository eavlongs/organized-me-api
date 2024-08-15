package com.organized_me.api.util;


import com.organized_me.api.model.File;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileHandlingHelper {
	@Value("${file.upload.dir}")
	private String fileUploadDirValue;
	@Value("${frontend.project.public.dir}")
	private String frontEndProjectDirValue;
	
	private static String fileUploadDir;
	private static String frontEndProjectDir;
	
	@PostConstruct
	private void init() {
		fileUploadDir = this.fileUploadDirValue;
		frontEndProjectDir = this.frontEndProjectDirValue;
	}
	/**
	 * Takes MultipartFile as input, returns a path that can be saved to the database.
	 */
	public static String uploadFile(MultipartFile file) throws Exception {
		try {
			if (file.isEmpty()) {
				throw new Exception("File is empty");
			}
			
			// TODO limit file size
			UUID uuid = UUID.randomUUID();
			String fileToBeUploadedName = uuid + "." + Helper.getExtensionByStringHandling(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
			
			Path path = Path.of(frontEndProjectDir, fileUploadDir, fileToBeUploadedName);
			System.out.println(path);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, path,
						StandardCopyOption.REPLACE_EXISTING);
			}
			
			return Path.of(fileUploadDir, fileToBeUploadedName).toString().replace("\\", "/");
		} catch (IOException e) {
			throw new Exception("Failed to upload file");
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static void deleteFile(String relativePath) throws RuntimeException {
		// this is safe because we only store the files in one folder
		Path path = Path.of(frontEndProjectDir, relativePath);
		try {
			Files.delete(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Resource downloadFile(File file) throws MalformedURLException {
		Path path = Path.of(frontEndProjectDir, file.getFilePath());
		
		if (Files.exists(path)) {
			return new UrlResource(path.toUri());
		}
		
		return null;
	}
}
