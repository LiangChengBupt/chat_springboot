package com.chat.dao;

import com.chat.entity.Group;
import com.chat.entity.GroupMsgContent;
import com.chat.entity.User;

import java.util.List;

public interface GroupDao {
    /**
     * 查找全部群聊
     *
     * @return 实例对象
     */
    List<Group> getAllGroup();

    /**
     * groupname查找群聊
     *
     * @return 实例对象
     */
    Group getGroupByName(String groupName);

    /**
     * 查找用户所在的群聊
     *
     * @param userid
     * @return 实例对象
     */
    List<Group> getUserGroup(Integer userid);

    /**
     * 查找群聊全部消息
     *
     * @param groupid
     * @return 实例对象
     */
    List<GroupMsgContent> getGroupMsg(Integer groupid);

    /**
     * 新建群聊
     *
     * @param group
     * @return int
     */
    int insertGroup(Group group);

    /**
     * 拉人进群群聊
     *
     * @param groupid
     * @param userid
     * @return int
     */
    int invite(Integer groupid,Integer userid);

    /**
     * 退出群聊
     *
     * @param groupid
     * @param userid
     * @return int
     */
    int quit(Integer groupid,Integer userid);

    /**
     * 添加群聊信息群聊（添加对应关系而非信息本身）
     *
     * @param groupid
     * @param msgid
     * @return int
     */
    int addMsg(Integer groupid,Integer msgid);

    /**
     * 删除群聊信息群聊（添加对应关系而非信息本身）
     *
     * @param groupid
     * @param msgid
     * @return int
     */
    int deleteMsg(Integer groupid,Integer msgid);

    /**
     * 删除群聊
     *
     * @param groupid
     * @return int
     */
    int deleteGroup(Integer groupid);

    /**
     * 删除群聊用户关联
     *
     * @param groupid
     * @return int
     */
    int deleteGroupUser(Integer groupid);

    /**
     * 删除群聊消息关联
     *
     * @param groupid
     * @return int
     */
    int deleteGroupMsg(Integer groupid);

}
