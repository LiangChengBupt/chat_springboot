package com.chat.dao;

import com.chat.entity.FriendRequest;
import com.chat.entity.User;

import java.util.List;

public interface FriendDao {
    /**
     * 通过userID查询其好友
     *
     * @param userid 主键
     * @return 实例对象
     */
    List<User> getFriendById(Integer userid);

    /**
     * 新增好友
     *
     * @param userid1
     * @param userid2
     * @return 影响行数
     */
    int insertFriend1(Integer userid1,Integer userid2);
    int insertFriend2(Integer userid1,Integer userid2);
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
     * 新增请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    int insertRequest(Integer fromId,Integer toId);

    /**
     * 删除请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    int deleteById(Integer fromId,Integer toId);

    FriendRequest getFriendQueByID(Integer fromId, Integer toId);
}
