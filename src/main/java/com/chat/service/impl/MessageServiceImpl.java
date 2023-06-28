package com.chat.service.impl;

import com.chat.dao.MessageDao;
import com.chat.entity.Message;
import com.chat.entity.RespPageBean;
import com.chat.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (MessageContent)表服务实现类
 *
 * @author jzq
 */
@Service("MessageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageDao messageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Message queryById(Integer id) {
        return this.messageDao.queryById(id);
    }

    /**
     * 通过toId查询数据
     *
     * @param fromId
     * @param toId
     * @return 实例对象
     */
    @Override
    public List<Message> queryByToId(Integer fromId, Integer toId) {
        return this.messageDao.queryByToId(fromId,toId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Message> queryAllByLimit(Integer offset, Integer limit) {
        return this.messageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message insert(Message message) {
        this.messageDao.insert(message);
        return message;
    }

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message update(Message message) {
        this.messageDao.update(message);
        return this.queryById(message.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.messageDao.deleteById(id) > 0;
    }

    @Override
    public RespPageBean getAllMessageByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope) {
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<Message> allMessageByPage = messageDao.getAllMessageByPage(page, size, nickname, type, dateScope);
        Long total=messageDao.getTotal(nickname, type, dateScope);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(allMessageByPage);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    @Override
    public Integer deleteMessageByIds(Integer[] ids) {
        return messageDao.deleteMessageByIds(ids);
    }
}
