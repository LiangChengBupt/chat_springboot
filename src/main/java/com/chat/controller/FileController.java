package com.chat.controller;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.chat.utils.FastDFSUtil;

import java.io.IOException;

/**
 * @author lc
 */
@RestController
public class FileController {

  @Value("${fastdfs.nginx.host}")
  String nginxHost;

  @PostMapping("/file")
  public String uploadFile(MultipartFile file) throws IOException, MyException {
    String fileId = FastDFSUtil.upload(file);
    return nginxHost + fileId;
  }

}