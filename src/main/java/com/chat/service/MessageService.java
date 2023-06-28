package com.chat.service;

import com.chat.entity.GroupMsgContent;
import com.chat.entity.Message;
import com.chat.entity.RespPageBean;

import java.util.Date;
import java.util.List;

/**
 * (MessageService)表服务接口
 *
 * @author jzq
 * @since 2023/6/24
 */
public interface MessageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Message queryById(Integer id);

    /**
     * 通过toId查询数据
     *
     * @param fromId
     * @param toId
     * @return 实例对象
     */
    List<Message> queryByToId(Integer fromId, Integer toId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Message> queryAllByLimit(Integer offset, Integer limit);

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message insert(Message message);

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message update(Message message);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    RespPageBean getAllMessageByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope);

    Integer deleteMessageByIds(Integer[] ids);
}
