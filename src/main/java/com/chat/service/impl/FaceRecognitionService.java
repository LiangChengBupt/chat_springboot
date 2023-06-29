package com.chat.service.impl;

import com.chat.dao.UserDao;
import com.chat.entity.User;
import com.chat.utils.TencentPeopleGroupUtil;
import com.tencentcloudapi.iai.v20200303.models.Candidate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("faceRecognitionService")
public class FaceRecognitionService {

    @Resource
    private UserDao userDao;

    private final TencentPeopleGroupUtil tencentPeopleGroupUtil;

    public FaceRecognitionService(){
        tencentPeopleGroupUtil = new TencentPeopleGroupUtil("AKID0rWxBD6nGWcYGm7HFWdXFk5EAuRmxrjU", "gncJtIbmkZDfw20reHj3EGAuUvDKHAtR");
    }

    /**
     * 人脸识别服务
     * @param personImgB64 用户人脸图像
     */
    public User recognize(String personImgB64){
        Candidate[] candidates = tencentPeopleGroupUtil.searchPerson(personImgB64);
        if(candidates != null){
            for (Candidate candidate: candidates){
                if(tencentPeopleGroupUtil.verifyPerson(personImgB64, candidate.getPersonId())){
                    System.out.println("人员验证成功！人员为：" + candidate.getPersonId());
                    return userDao.loadUserByUsername(candidate.getPersonId());
                }
            }
        }
        return null;
    }

    public boolean uploadImg(String personImgB64, String personName, String personId){
        return tencentPeopleGroupUtil.createPerson(personImgB64.replace(" ", "+"), personName, personId);
    }

    public boolean deletePerson(String personId){
        return tencentPeopleGroupUtil.deletePerson(personId);
    }
}
