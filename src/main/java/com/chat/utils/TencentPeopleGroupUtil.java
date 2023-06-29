package com.chat.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;

/**
 * @author gzx
 */
public class TencentPeopleGroupUtil {
    Credential cred;
    public TencentPeopleGroupUtil(String secretId, String secretKey){
        cred = new Credential(secretId, secretKey);
    }

    // 创建人员库
    public void createGroup(){
        try{
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreateGroupRequest req = new CreateGroupRequest();
            req.setGroupName("test2");
            req.setGroupId("test2");

            String[] groupExDescriptions1 = {"id", "username", "nickname"};
            req.setGroupExDescriptions(groupExDescriptions1);

            // 返回的resp是一个CreateGroupResponse的实例，与请求对象对应
            CreateGroupResponse resp = client.CreateGroup(req);
//            System.out.println(CreateGroupResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    // 添加人员
    public boolean createPerson(String personImg, String personName, String personId){
        try{
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreatePersonRequest req = new CreatePersonRequest();
            // 选中人员库
            req.setGroupId("test2");
            // 人员名称，可以任意
            req.setPersonName(personName);
            // 人员id，在人员库中唯一
            req.setPersonId(personId);
            // 性别
            req.setGender(1L);

            // 人员描述，这里为id
            PersonExDescriptionInfo[] personExDescriptionInfos1 = new PersonExDescriptionInfo[1];
            PersonExDescriptionInfo personExDescriptionInfo1 = new PersonExDescriptionInfo();
            personExDescriptionInfo1.setPersonExDescriptionIndex(0L);
            personExDescriptionInfo1.setPersonExDescription(personId);
            personExDescriptionInfos1[0] = personExDescriptionInfo1;

            req.setPersonExDescriptionInfos(personExDescriptionInfos1);

            // 图片，应为b64格式
            req.setImage(personImg);
            // 识别是否唯一
            req.setUniquePersonControl(2L);
            // 照片质量控制，2表示一般质量
            req.setQualityControl(2L);
            // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
            CreatePersonResponse resp = client.CreatePerson(req);
            System.out.println(CreatePersonResponse.toJsonString(resp));
            return resp.getFaceId() != null;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    // 删除人员
    public boolean deletePerson(String personId){
        try{
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeletePersonRequest req = new DeletePersonRequest();
            req.setPersonId(personId);
            // 返回的resp是一个DeletePersonResponse的实例，与请求对象对应
            DeletePersonResponse resp = client.DeletePerson(req);
//            InvalidParameterValue.PersonIdNotExist
            // 输出json格式的字符串回包
            System.out.println(DeletePersonResponse.toJsonString(resp));
            return true;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    // 搜索人员，返回最相似的前三个人员
    public Candidate[] searchPerson(String personImg){
        try{
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SearchPersonsRequest req = new SearchPersonsRequest();
            String[] groupIds1 = {"test2"};
            req.setGroupIds(groupIds1);

            req.setImage(personImg);
            // 设置长度和宽度的最小尺寸为80px
            req.setMinFaceSize(80L);
            // 单张被识别的人脸返回的最相似人员数量
            req.setMaxPersonNum(3L);
            // 图片质量控制
            req.setQualityControl(0L);
            // 是否返回详细信息
            req.setNeedPersonInfo(0L);
            // 返回的resp是一个SearchPersonsResponse的实例，与请求对象对应
            SearchPersonsResponse resp = client.SearchPersons(req);
//            System.out.println(SearchPersonsResponse.toJsonString(resp));
            return resp.getResults()[0].getCandidates();
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    // 人员验证，用于搜索后判断是否真的是同一个人
    public boolean verifyPerson(String personImg, String personId){
        try{
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            VerifyPersonRequest req = new VerifyPersonRequest();
            req.setImage(personImg);
            req.setPersonId(personId);
            // 图片质量控制
            req.setQualityControl(0L);
            // 不需要旋转检测
            req.setNeedRotateDetection(0L);
            // 返回的resp是一个VerifyPersonResponse的实例，与请求对象对应
            VerifyPersonResponse resp = client.VerifyPerson(req);
//            System.out.println(VerifyPersonResponse.toJsonString(resp));
            // 返回结果：是否是同一个人
            return resp.getIsMatch();
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static void main(String[] args) {
        TencentPeopleGroupUtil tencentPeopleGroupUtil = new TencentPeopleGroupUtil("AKID0rWxBD6nGWcYGm7HFWdXFk5EAuRmxrjU", "gncJtIbmkZDfw20reHj3EGAuUvDKHAtR");
        // 删除人员
        tencentPeopleGroupUtil.deletePerson("gzx111");
        // 创建人员
//        String b64Img = CommonUtil.imageToB64(Paths.get(System.getProperty("user.dir") + "/src/main/resources/faces/chaowei.jpg"));
//        System.out.println(b64Img);
//        tencentPeopleGroupUtil.createPerson(b64Img, "gzx", "gzx");

//         搜索人脸
//        String b64Img = CommonUtil.imageToB64(Paths.get(System.getProperty("user.dir") + "/src/main/resources/faces/chaowei_sample1.jpeg"));
//        System.out.println(b64Img);
//        Candidate[] candidates = tencentPeopleGroupUtil.searchPerson(b64Img);
//        Candidate[] candidates = tencentPeopleGroupUtil.searchPerson("");
//        if(candidates != null){
//            for (Candidate candidate: candidates){
//                if(tencentPeopleGroupUtil.verifyPerson(b64Img, candidate.getPersonId())){
//                    System.out.println("人员验证成功！人员为：" + candidate.getPersonId());
//                    break;
//                }
//            }
//        }
    }
}
