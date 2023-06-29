package com.chat.utils;

import com.chat.entity.RobotReq;
import com.chat.entity.RobotResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * 智能回复机器人工具类
 * @author jzq
 * @date 2023/6/23
 */
public class TuLingUtil {

  private static ObjectMapper MAPPER=new ObjectMapper();

  /**
   * 发送消息，获得chatgpt回复消息
   * @param message
   * @return
   * @throws IOException
   */
  public static String replyMessage(String message) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    RobotReq robotReq = new RobotReq();
    robotReq.setModel("gpt-3.5-turbo");
    robotReq.setRole("user");
    robotReq.setContent(message);
    robotReq.setSafe_mode(false);
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("https://oa.api2d.net/v1/chat/completions");
    httpPost.setHeader("Content-type", "application/json");
    httpPost.setHeader("Authorization", "Bearer fk208811-TrNCBIqTP1Y1ZOxnW9v4uNitPNlDaxhC");
    httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(robotReq)));
    HttpResponse response = httpClient.execute(httpPost);
    HttpEntity entity = response.getEntity();
    String resp = EntityUtils.toString(entity);
    System.out.println(resp);
    RobotResp robotResp = objectMapper.readValue(resp,RobotResp.class);
    return robotResp.getChoices().get(0).getMessage().getContent();
  }

  public static void main(String[] args) throws IOException {
    //test
    System.out.println(replyMessage("hi"));
  }
}
