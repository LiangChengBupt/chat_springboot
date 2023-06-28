package com.chat.utils;


import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

//import static java.util.prefs.WindowsPreferences.logger;

@SpringBootTest
public class FastDFSTest {

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Test
    public void testofdfs() throws IOException, MyException {
        //客户端配置文件
//        String conf_filename = "E:\\Workspace\\code\\java\\subtlechat-mini\\subtlechat-mini\\src\\main\\resources\\fastdfs-client.properties";
        //本地文件，要上传的文件
        String local_filename = "E:\\desk\\1.jpg";

        for (int i = 0; i < 1; i++) {

            try {
                ClientGlobal.init("fastdfs-client.properties");

                TrackerClient tracker = new TrackerClient();
                TrackerServer trackerServer = tracker.getConnection();
                StorageServer storageServer = null;

                StorageClient storageClient = new StorageClient(trackerServer,
                        storageServer);
                NameValuePair nvp[] = new NameValuePair[]{
                        new NameValuePair("item_id", "100010"),
                        new NameValuePair("width", "80"),
                        new NameValuePair("height", "90")
                };
                String fileIds[] = storageClient.upload_file(local_filename, null,
                        nvp);

                System.out.println(fileIds.length);
                System.out.println("组名：" + fileIds[0]);
                System.out.println("路径: " + fileIds[1]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Test
    public void getToken() throws Exception {
        int ts = (int) Instant.now().getEpochSecond();
        String token = ProtoCommon.getToken("group1/M00/00/00/ZSqov2SXMYmAGY3EAAABXU4koC8375.png", ts, "FastDFS1234567890");
        StringBuilder sb = new StringBuilder();
        sb.append("?token=").append(token);
        sb.append("&ts=").append(ts);
        System.out.println(sb.toString());
    }

    @Test
    public void testDownload() {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = client.download_file1("group1/M00/00/00/ZSqov2SXMYmAGY3EAAABXU4koC8375.png");
            FileOutputStream fos = new FileOutputStream(new File("E:\\Workspace\\code\\java\\subtlechat-mini\\subtlechat-mini\\src\\test\\1.png"));
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            NameValuePair nvp[] = null;
            //上传到文件系统
            String fileId = client.upload_file1("E:\\Workspace\\code\\java\\subtlechat-mini\\subtlechat-mini\\src\\test\\yanzu_sample.png", "png",
                    nvp);
            System.out.println(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
