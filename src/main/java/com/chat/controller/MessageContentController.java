package com.chat.controller;

import com.chat.entity.Message;
import com.chat.entity.RespBean;
import com.chat.entity.RespPageBean;
import com.chat.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (MessageContent)表控制层
 *
 * @author jzq
 */
@RestController
@RequestMapping("/MessageContent")
public class MessageContentController {
    /**
     * 服务对象
     */
    @Resource
    private MessageService messageService;

    @GetMapping("/")
    private List<Message> getAllMessageContent(){
        return messageService.queryAllByLimit(null,null);
    }

    @GetMapping("/getbytoid")
    private List<Message> getByToId(Integer fromId,Integer toId){
        return messageService.queryByToId(fromId,toId);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Message selectOne(Integer id) {
        return this.messageService.queryById(id);
    }

    /**
     * 分页返回数据
     * @author jzq
     * @param page 页数
     * @param size 单页大小
     * @param nickname 发送者昵称
     * @param type 消息类型
     * @param dateScope 发送时间范围
     * @return
     */
    @GetMapping("/page")
    public RespPageBean getAllMessageContentByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size",defaultValue = "10") Integer size,
                                                    String nickname, Integer type,
                                                    Date[] dateScope){
        return messageService.getAllMessageByPage(page,size,nickname,type,dateScope);
    }

    /**
     * 根据id删除单条记录
     * @author jzq
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespBean deleteMessageContentById(@PathVariable Integer id){
        if (messageService.deleteById(id)){
            return RespBean.ok("删除成功！");
        }else{
            return RespBean.error("删除失败！");
        }
    }
    @DeleteMapping("/")
    public RespBean deleteMessageContentByIds(Integer[] ids){
        if (messageService.deleteMessageByIds(ids)==ids.length){
            return RespBean.ok("删除成功！");
        }else {
            return RespBean.error("删除失败！");
        }
    }
}
