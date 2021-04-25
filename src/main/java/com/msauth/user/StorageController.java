package com.msauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class StorageController {

	@Autowired
	private StorageService service;
	
	@PostMapping("/file/pic")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
        return service.upload(multipartFile);
    }
	
	@PostMapping("/file/pic/{fileName}")
    public Object download(@PathVariable String fileName) throws Exception {
        return service.download(fileName);
    }
    
    @PostMapping("/file/pic/delete/{fileName}")
    public Object delete(@PathVariable String fileName) throws Exception {
        return service.delete(fileName);
    }
}
