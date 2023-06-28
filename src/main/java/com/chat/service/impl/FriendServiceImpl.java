package com.chat.service.impl;

import com.chat.dao.FriendDao;
import com.chat.entity.User;
import com.chat.service.FriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("FriendService")
public class FriendServiceImpl implements FriendService {
    @Resource
    private FriendDao friendDao;
    /**
     * 通过userID查询其好友
     *
     * @param userid 主键
     * @return 实例对象
     */
    @Override
    public List<User> getFriendById(Integer userid) {
        return this.friendDao.getFriendById(userid);
    }

    /**
     * 新增朋友数据
     *
     * @param userid1
     * @param userid2
     * @return 影响行数
     */
    @Override
    public int insert(Integer userid1, Integer userid2) {
        return this.friendDao.insertFriend1(userid1, userid2)+this.friendDao.insertFriend2(userid1, userid2);
    }

    /**
     * userID查询向谁发过请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    @Override
    public List<User> getSendTo(Integer userid) {
        return this.friendDao.getSendTo(userid);
    }

    /**
     * userID查询收到了谁的好友请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    @Override
    public List<User> getReceiveFrom(Integer userid) {
        return this.friendDao.getReceiveFrom(userid);
    }

    /**
     * 添加请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @Override
    public int addRequest(Integer fromId, Integer toId) {
        return this.friendDao.insertRequest(fromId,toId);
    }

    /**
     * 通过请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @Override
    public int agreeRequest(Integer fromId, Integer toId) {
        if(this.friendDao.deleteById(fromId,toId)>0){
            return this.friendDao.insertFriend1(fromId,toId)+this.friendDao.insertFriend2(fromId,toId);
        }
        return 0;
    }

    /**
     * 拒绝请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @Override
    public int disagreeRequest(Integer fromId, Integer toId) {
        return this.friendDao.deleteById(fromId,toId);
    }
}
