package com.chat.controller;

import com.chat.entity.*;
import com.chat.service.FriendService;
import com.chat.service.GroupService;
import com.chat.service.UserService;
import com.chat.service.impl.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author gzx
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private FaceRecognitionService faceRecognitionService;


    @GetMapping("/getUserByName")
    public User getUserByName(String userName){
        return userService.getByName(userName);
    }

    /**
    获得一个用户的所有好友
     */
    @GetMapping("/friends")
    public List<User> getUsersWithoutCurrentUser(){
        return userService.getUsersWithoutCurrentUser();
    }

    /**
     * 注册操作
     */
    @PostMapping("/register")
    public RespBean addUser(@RequestBody User user){
        if (userService.insert(user)==1){
            return RespBean.ok("注册成功！");
        }else{
            return RespBean.error("注册失败！");
        }
    }

    @PostMapping("/personImg/upload")
    public RespBean addUser(String img, String personName, String personId){
        if(faceRecognitionService.uploadImg(img, personName, personId)){
            return RespBean.ok("人脸图像上传成功！");
        }
        else
            return RespBean.error("人脸图像上传失败！");
    }

    @DeleteMapping("/personImg/{personId}")
    public RespBean deleteUser(@PathVariable String personId){
        if (faceRecognitionService.deletePerson(personId)){
            return RespBean.ok("删除人员成功！");
        }
        else{
            return RespBean.error("删除人员失败！");
        }
    }

    /**
     * 注册操作，检查用户名是否已被注册
     * @param username
     * @return
     */
    @GetMapping("/checkUsername")
    public Integer checkUsername(@RequestParam("username")String username){
        return userService.checkUsername(username);
    }

    /**
     * 注册操作，检查昵称是否已被注册
     * @param nickname
     * @return
     */
    @GetMapping("/checkNickname")
    public Integer checkNickname(@RequestParam("nickname") String nickname){
        return userService.checkNickname(nickname);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public User selectOne(Integer id) {
        return this.userService.queryById(id);
    }

    /**
     * @author gzx
     * @param page  页数，对应数据库查询的起始行数
     * @param size  数据量，对应数据库查询的偏移量
     * @param keyword 关键词，用于搜索
     * @param isLocked  是否锁定，用于搜索
     * @return
     */
    @GetMapping("/")
    public RespPageBean getAllUserByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                                         String keyword, Integer isLocked){
        return userService.getAllUserByPage(page,size,keyword,isLocked);
    }

    /**
     * 更新用户的锁定状态
     * @author gzx
     * @param id
     * @param isLocked
     * @return
     */
    @PutMapping("/")
    public RespBean changeLockedStatus(@RequestParam("id") Integer id,@RequestParam("isLocked") Boolean isLocked){
      if (userService.changeLockedStatus(id,isLocked)==1){
          return RespBean.ok("更新成功！");
      }else {
          return RespBean.error("更新失败！");
      }
    }

    /**
     * 删除单一用户
     * @author gzx
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespBean deleteUser(@PathVariable Integer id){
        if (userService.deleteById(id)){
            return RespBean.ok("删除成功！");
        }
        else{
            return RespBean.error("删除失败！");
        }
    }

    /**
     * 批量删除用户
     * @author gzx
     * @param ids
     * @return
     */
    @DeleteMapping("/")
    public RespBean deleteUserByIds(Integer[] ids){
        if (userService.deleteByIds(ids)==ids.length){
            return RespBean.ok("删除成功！");
        }else{
            return RespBean.error("删除失败！");
        }
    }

    @Resource
    private FriendService friendService;

    /**
     * 通过userID查询其好友
     *
     * @param userid 主键
     * @return 实例对象
     */
    @GetMapping("/getFriends")
    public List<User> getFriends(Integer userid){
        return this.friendService.getFriendById(userid);
    }

    /**
     * userID查询向谁发过请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    @GetMapping("/getFriendTo")
    public List<User> getFriendSendTo(Integer userid){
        return this.friendService.getSendTo(userid);
    }

    /**
     * userID查询收到了谁的好友请求
     *
     * @param userid 主键
     * @return 实例对象
     */
    @GetMapping("/getFriendFrom")
    public List<User> getFriendReciveFrom(Integer userid){
        return this.friendService.getReceiveFrom(userid);
    }

    /**
     * 发送好友请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @GetMapping("/addFriendRequestById")
    public int addFriendRequest(Integer fromId,Integer toId){
        int i = this.friendService.addRequest(fromId,toId);
        if(i>0){
            simpMessagingTemplate.convertAndSendToUser(userService.queryById(toId).getUsername(),"/queue/chat","refresh");
        }
        return i;
    }

    @GetMapping("/addFriendRequest")
    public int addFriendRequest_name(String fromName,String toName){
        int i = this.friendService.addRequest(fromName,toName);
        if(i>0){
            simpMessagingTemplate.convertAndSendToUser(toName,"/queue/chat","refresh");
        }
        return i;
    }

    /**
     * 通过请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @GetMapping("/agreeRequest")
    public int agreeRequest(Integer fromId,Integer toId){
        int i = this.friendService.agreeRequest(fromId,toId);
        if(i>0){
            simpMessagingTemplate.convertAndSendToUser(userService.queryById(fromId).getUsername(),"/queue/chat","refresh");
        }
        return i;
    }

    /**
     * 拒绝请求
     *
     * @param fromId
     * @param toId
     * @return 影响行数
     */
    @GetMapping("/disagreeRequest")
    public int disagreeRequest(Integer fromId,Integer toId){
        int i = this.friendService.disagreeRequest(fromId,toId);
        if(i>0){
            simpMessagingTemplate.convertAndSendToUser(userService.queryById(fromId).getUsername(),"/queue/chat","refresh");
        }
        return i;
    }

    @Autowired
    private GroupService groupService;

    /**
     * 查找全部群聊
     *
     * @return 实例对象
     */
    @GetMapping("/getAllGroup")
    public List<Group> getAllGroup() {
        return groupService.getAllGroup();
    }

    /**
     * 查找用户所在的群聊
     *
     * @param userid
     * @return 实例对象
     */
    @GetMapping("/getUserGroup")
    public List<Group> getUserGroup(@RequestParam Integer userid) {
        return groupService.getUserGroup(userid);
    }

    /**
     * 查找群聊全部消息
     *
     * @param groupid
     * @return 实例对象
     */
    @GetMapping("/getGroupMsg")
    public List<GroupMsgContent> getGroupMsg(@RequestParam Integer groupid) {
        return groupService.getGroupMsg(groupid);
    }

    @GetMapping("/getGroupByName")
    public Group getGroupByName(String groupName){
        return groupService.getGroupByName(groupName);
    }

    /**
     * 新建群聊
     *
     * @param group
     * @return int
     */
    @PostMapping("/newGroup")
    public int insertGroup(@RequestBody Group group, @RequestParam Integer userid) {
        return groupService.insertGroup(group, userid);
    }

    /**
     * 邀请进群
     *
     * @param groupid
     * @param userid
     * @return int
     */
    @GetMapping("/invite")
    public int invite(@RequestParam Integer groupid, @RequestParam Integer userid) {
        int i = this.groupService.invite(groupid,userid);
        if(i>0){
            simpMessagingTemplate.convertAndSendToUser(userService.queryById(userid).getUsername(),"/queue/chat","refresh");
        }
        return i;
    }

    /**
     * 退出群聊
     *
     * @param groupid
     * @param userid
     * @return int
     */
    @GetMapping("/quit")
    public int quit(@RequestParam Integer groupid, @RequestParam Integer userid) {
        return groupService.quit(groupid, userid);
    }

    /**
     * 删除群聊信息群聊（删除对应关系而非信息本身）
     *
     * @param groupid
     * @param msgid
     * @return int
     */
    @GetMapping("/addMsg")//一般不用，报错关系在保存群消息时做
    public int addMsg(@RequestParam Integer groupid, @RequestParam Integer msgid) {
        return groupService.addMsg(groupid, msgid);
    }

    /**
     * 删除群聊
     *
     * @param groupid
     * @return int
     */
    @DeleteMapping("/delete")
    public int deleteGroup(@RequestParam Integer groupid) {
        return groupService.deleteGroup(groupid);
    }
}
