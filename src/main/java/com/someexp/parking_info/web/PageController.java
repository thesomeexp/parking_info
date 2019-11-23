package com.someexp.parking_info.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value="/")
    public String index(){
        return "redirect:homePage";
    }

    // 主页页面
    // get: listNearbyInfos
    // post: logout
    @GetMapping(value="/homePage")
    public String home(){
        return "home";
    }

    // 注册页面
    // post: register
    @GetMapping(value="/registerPage")
    public String register(){
        return "register";
    }

    // 登录页面
    // post: login
    @GetMapping(value="/loginPage")
    public String login(){
        return "login";
    }

    // 未授权页面
    @GetMapping(value="/noAuthPage")
    public String noAuth(){
        return "error/noAuth";
    }

    // 提交车停车场信息页面
    // post: addInfo
    @GetMapping(value="/addInfoPage")
    public String addInfo(){
        return "fore/addInfo";
    }

    // 提交停车场详情图片页面
    // post: addInfoImage
    @GetMapping(value="/addInfoImagePage")
    public String addInfoImage(){
        return "fore/addInfoImage";
    }

    // 测试页面  查看停车场所有图片
    // get: listInfoImages
    @GetMapping(value="/listInfoImagesPage")
    public String listInfoImages(){
        return "fore/listInfoImages";
    }

    // 我提交的停车场信息页面
    // get: mySubmitInfos
    @GetMapping(value="/mySubmitInfosPage")
    public String mySubmitInfos(){
        return "fore/mySubmitInfos";
    }
    @GetMapping(value="/addReviewPage")
    public String addReview(){
        return "fore/addReview";
    }
    @GetMapping(value="/infoDetailPage")
    public String infoDetail(){
        return "fore/infoDetail";
    }
    @GetMapping(value="/addTempPage")
    public String addTemp(){
        return "fore/addTemp";
    }


    @GetMapping(value="/adminPage")
    public String adminHome(){
        return "admin/home";
    }
    @GetMapping(value = "/adminVerifiedInfoPage")
    public String adminVerifiedInfoPage() {
        return "admin/verifiedInfo";
    }
    @GetMapping(value = "/adminListDisableInfoPage")
    public String adminListDisableInfoPage() {
        return "admin/disableInfo";
    }

}
