package com.chat.controller;

import com.chat.entity.GroupMsgContent;
import com.chat.entity.Message;
import com.chat.entity.User;
import com.chat.service.GroupMsgContentService;
import com.chat.service.GroupService;
import com.chat.service.MessageService;
import com.chat.utils.TuLingUtil;
import com.github.binarywang.java.emoji.EmojiConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jzq
 * @date 2023/6/24
 */
@Controller
public class WsController {
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  MessageService messageService;

  @Autowired
  GroupService groupService;

  /**
   * 单聊的消息的接受与转发
   * @param authentication
   * @param message
   */
  @MessageMapping("/ws/chat")
  public void handleMessage(Authentication authentication, Message message){
    User user= ((User) authentication.getPrincipal());
    //处理emoji内容,转换成unicode编码
    message.setContent(emojiConverter.toHtml(message.getContent()));
    //保证来源正确性，从Security中获取用户信息
    message.setFromId(message.getFromId());
    message.setFromNickname(message.getFromNickname());
    message.setFromUserProfile(message.getFromUserProfile());
    message.setFrom(message.getFrom());
    message.setCreateTime(new Date());
    System.out.println(message.getTo());
    //保存该条群聊消息记录到数据库中
    messageService.insert(message);
    //转发该条数据
    simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/chat/"+message.getToId(),message);
  }

  @Autowired
  GroupMsgContentService groupMsgContentService;

  EmojiConverter emojiConverter = EmojiConverter.getInstance();

  SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  /**
   * 群聊的消息接受与转发
   * @param authentication
   * @param groupMsgContent
   */
  @MessageMapping("/ws/groupChat")
  public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent, Integer groupId){
    User currentUser= (User) authentication.getPrincipal();
    //处理emoji内容,转换成unicode编码
    groupMsgContent.setContent(emojiConverter.toHtml(groupMsgContent.getContent()));
    //保证来源正确性，从Security中获取用户信息
    groupMsgContent.setFromId(currentUser.getId());
    groupMsgContent.setFromName(currentUser.getNickname());
    groupMsgContent.setFromProfile(currentUser.getUserProfile());
    groupMsgContent.setCreateTime(new Date());
    //保存该条群聊消息记录到数据库中
    groupMsgContentService.insert(groupMsgContent,currentUser.getId());
    groupService.addMsg(groupId,groupMsgContent.getId());
    //转发该条数据
    simpMessagingTemplate.convertAndSend("/topic/greetings",groupMsgContent);
  }

  /**
   * 接受前端发来的消息，获得图灵机器人回复并转发回给发送者
   * @param authentication
   * @param message
   * @throws IOException
   */
  @MessageMapping("/ws/robotChat")
  public void handleRobotChatMessage(Authentication authentication, Message message) throws IOException {
    User user = ((User) authentication.getPrincipal());
    //接收到的消息
    message.setFrom(user.getUsername());
    message.setCreateTime(new Date());
    message.setFromNickname(user.getNickname());
    message.setFromUserProfile(user.getUserProfile());
    //发送消息内容给机器人，获得回复
    String result = TuLingUtil.replyMessage(message.getContent());
    //构建返回消息JSON字符串
    Message resultMessage = new Message();
    resultMessage.setFrom("瓦力");
    resultMessage.setCreateTime(new Date());
    resultMessage.setFromNickname("瓦力");
    resultMessage.setContent(result);
    //回送机器人回复的消息给发送者
    simpMessagingTemplate.convertAndSendToUser(message.getFrom(),"/queue/robot",resultMessage);
  }
}
