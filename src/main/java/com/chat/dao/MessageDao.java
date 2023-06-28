package com.chat.dao;

import com.chat.entity.GroupMsgContent;
import com.chat.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * (MessageContent)表数据库访问层
 *
 * @author jzq
 */
public interface MessageDao {

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
    List<Message> queryByToId(Integer fromId,Integer toId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Message> queryAllByLimit(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param groupMsgContent 实例对象
     * @return 对象列表
     */
    List<Message> queryAll(GroupMsgContent groupMsgContent);

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 影响行数
     */
    int insert(Message message);

    /**
     * 修改数据
     *
     * @param groupMsgContent 实例对象
     * @return 影响行数
     */
    int update(Message groupMsgContent);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    List<Message> getAllMessageByPage(@Param("page") Integer page,
                                                      @Param("size") Integer size,
                                                      @Param("nickname") String nickname,
                                                      @Param("type") Integer type,
                                                      @Param("dateScope") Date[] dateScope);

    Long getTotal(@Param("nickname") String nickname,
                  @Param("type") Integer type,
                  @Param("dateScope") Date[] dateScope);

    Integer deleteMessageByIds(@Param("ids") Integer[] ids);
}
