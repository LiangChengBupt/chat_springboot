package com.chat.service;

import com.chat.entity.User;

import java.util.List;

public interface FriendService {
    /**
     * 通过userID查询其好友
     *
     * @param userid 主键
     * @return 实例对象
     */
    List<User> getFriendById(Integer userid);

    /**
     * 新增朋友数据
     *
     * @param userid1
     * @param userid2
     * @return 影响行数
     */
    int insert(Integer userid1,Integer userid2);

    /**
     * userID查询向谁发过请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    List<User> getSendTo(Integer userid);

    /**
     * userID查询收到了谁的好友请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    List<User> getReceiveFrom(Integer userid);

    /**
     * 添加请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    int addRequest(Integer fromId,Integer toId);

    int addRequest(String fromName,String toName);

    /**
     * 通过请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    int agreeRequest(Integer fromId,Integer toId);

    /**
     * 拒绝请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    int disagreeRequest(Integer fromId,Integer toId);
}
