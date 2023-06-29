package com.chat.service.impl;

import com.chat.dao.GroupDao;
import com.chat.entity.Group;
import com.chat.entity.GroupMsgContent;
import com.chat.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("GroupService")
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupDao groupDao;

    /**
     * 查找全部群聊
     *
     * @return 实例对象
     */
    @Override
    public List<Group> getAllGroup() {
        return this.groupDao.getAllGroup();
    }

    /**
     * 查找用户所在的群聊
     *
     * @param userid
     * @return 实例对象
     */
    @Override
    public List<Group> getUserGroup(Integer userid) {
        return this.groupDao.getUserGroup(userid);
    }

    /**
     * 查找群聊全部消息
     *
     * @param groupid
     * @return 实例对象
     */
    @Override
    public List<GroupMsgContent> getGroupMsg(Integer groupid) {
        return this.groupDao.getGroupMsg(groupid);
    }

    /**
     * 新建群聊
     *
     * @param group
     * @return int
     */
    @Override
    public int insertGroup(Group group, Integer userid) {
        this.groupDao.insertGroup(group);
        return this.groupDao.invite(group.getId(),userid);
    }

    /**
     * 拉人进群群聊
     *
     * @param groupid
     * @param userid
     * @return int
     */
    @Override
    public int invite(Integer groupid, Integer userid) {
        return this.groupDao.invite(groupid,userid);
    }

    /**
     * 退出群聊
     *
     * @param groupid
     * @param userid
     * @return int
     */
    @Override
    public int quit(Integer groupid, Integer userid) {
        return this.groupDao.quit(groupid,userid);
    }

    /**
     * 添加群聊信息群聊（添加对应关系而非信息本身）
     *
     * @param groupid
     * @param msgid
     * @return int
     */
    @Override
    public int addMsg(Integer groupid, Integer msgid) {
        return this.groupDao.addMsg(groupid,msgid);
    }

    /**
     * 添加群聊信息群聊（添加对应关系而非信息本身）
     *
     * @param groupName
     * @param msgid
     * @return int
     */
    @Override
    public int addMsg(String groupName, Integer msgid) {
        Group group = this.groupDao.getGroupByName(groupName);
        if(group == null){
            return 0;
        }else {
            return this.groupDao.addMsg(group.getId(),msgid);
        }
    }

    /**
     * 删除群聊信息群聊（删除对应关系而非信息本身）
     *
     * @param groupid
     * @param msgid
     * @return int
     */
    @Override
    public int deleteMsg(Integer groupid, Integer msgid) {
        return this.groupDao.deleteMsg(groupid,msgid);
    }

    /**
     * 删除群聊
     *
     * @param groupid
     * @return int
     */
    @Override
    public int deleteGroup(Integer groupid) {
        return this.groupDao.deleteGroup(groupid)+this.groupDao.deleteGroupUser(groupid)+this.groupDao.deleteGroupMsg(groupid);
    }
}
