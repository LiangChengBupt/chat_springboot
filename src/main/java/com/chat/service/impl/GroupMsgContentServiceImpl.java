package com.chat.service.impl;

import com.chat.dao.GroupDao;
import com.chat.dao.GroupMsgContentDao;
import com.chat.entity.GroupMsgContent;
import com.chat.entity.RespPageBean;
import com.chat.service.GroupMsgContentService;
import com.chat.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (GroupMsgContent)表服务实现类
 *
 * @author gzx
 */
@Service("groupMsgContentService")
public class GroupMsgContentServiceImpl implements GroupMsgContentService {
    @Resource
    private GroupMsgContentDao groupMsgContentDao;
    @Resource
    private GroupDao groupDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public GroupMsgContent queryById(Integer id) {
        return this.groupMsgContentDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<GroupMsgContent> queryAllByLimit(Integer offset, Integer limit) {
        return this.groupMsgContentDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param groupMsgContent 实例对象
     * @return 实例对象
     */
    @Override
    public GroupMsgContent insert(GroupMsgContent groupMsgContent,Integer groupid) {
        this.groupMsgContentDao.insert(groupMsgContent);
        this.groupDao.addMsg(groupid,groupMsgContent.getId());
        return groupMsgContent;
    }

    /**
     * 修改数据
     *
     * @param groupMsgContent 实例对象
     * @return 实例对象
     */
    @Override
    public GroupMsgContent update(GroupMsgContent groupMsgContent,Integer groupid) {
        this.groupMsgContentDao.update(groupMsgContent);
        this.groupDao.deleteMsg(groupid,groupMsgContent.getId());
        this.groupDao.addMsg(groupid,groupMsgContent.getId());
        return this.queryById(groupMsgContent.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id,Integer groupid) {
        this.groupDao.deleteMsg(groupid,id);
        return this.groupMsgContentDao.deleteById(id) > 0;
    }

    @Override
    public RespPageBean getAllGroupMsgContentByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope) {
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<GroupMsgContent> allGroupMsgContentByPage = groupMsgContentDao.getAllGroupMsgContentByPage(page, size, nickname, type, dateScope);
        Long total=groupMsgContentDao.getTotal(nickname, type, dateScope);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(allGroupMsgContentByPage);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    @Override
    public Integer deleteGroupMsgContentByIds(Integer[] ids) {
        return groupMsgContentDao.deleteGroupMsgContentByIds(ids);
    }
}
