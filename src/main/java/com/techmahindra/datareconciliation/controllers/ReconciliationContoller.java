package com.techmahindra.datareconciliation.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techmahindra.datareconciliation.exceptions.ApplicationException;
import com.techmahindra.datareconciliation.service.FileChecker;

@RestController("/")
public class ReconciliationContoller {
	
	@Autowired
	FileChecker fileChecker;
	
	@Value("${fileupload.folder}")
	private String UPLOADED_FOLDER;

	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String testHome() {
		return "Home";
	}
	
	
	@PostMapping(value="/filematcher", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> fileMatcher(
            @RequestParam("files") MultipartFile[] uploadfiles) {
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName) || uploadedFileName.split(",").length!=2) {
            return new ResponseEntity("Upload Two txt Files!",HttpStatus.BAD_REQUEST);
        }
        try {
        	return new ResponseEntity(saveUploadedFiles(Arrays.asList(uploadfiles)),HttpStatus.OK);
        } catch (IOException |ApplicationException e) {
        	return new ResponseEntity("Upload Proper txt Files!",HttpStatus.BAD_REQUEST);
		}
    }

	private Object saveUploadedFiles(List<MultipartFile> files) throws IOException, ApplicationException {
		Path[] fileList = new Path[2]; int i = 0;
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();
            if(!name.endsWith(".txt"))
            	return "Upload Proper txt Files!";
            Path path = Paths.get(UPLOADED_FOLDER + name);
            Files.write(path, bytes);
            fileList[i++]=path;
        }
        return fileChecker.matchFile(fileList[0], fileList[1]);
        
    }
	
}
