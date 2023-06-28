package com.chat.entity;

import java.util.Date;

/**
 * 单聊的消息实体
 * @author jzq
 */
public class Message {
  private Integer Id;
  private Integer fromId;
  private Integer toId;
  private String from;
  private String to;
  private String content;
  private Date createTime;
  private String fromNickname;
  private String fromUserProfile;
  private Integer messageTypeId;

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public Integer getFromId() {
    return fromId;
  }

  public void setFromId(Integer fromId) {
    this.fromId = fromId;
  }

  public Integer getToId() {
    return toId;
  }

  public void setToId(Integer toId) {
    this.toId = toId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getFromNickname() {
    return fromNickname;
  }

  public void setFromNickname(String fromNickname) {
    this.fromNickname = fromNickname;
  }

  public String getFromUserProfile() {
    return fromUserProfile;
  }

  public void setFromUserProfile(String fromUserProfile) {
    this.fromUserProfile = fromUserProfile;
  }

  public Integer getMessageTypeId() {
    return messageTypeId;
  }

  public void setMessageTypeId(Integer messageTypeId) {
    this.messageTypeId = messageTypeId;
  }
}
