package com.someexp.parking_info.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value="/")
    public String index(){
        return "redirect:home";
    }

    // 主页页面
    // get: listNearbyInfos
    // post: logout
    @GetMapping(value="/home")
    public String home(){
        return "home";
    }

    // 注册页面
    // post: register
    @GetMapping(value="/register")
    public String register(){
        return "register";
    }

    // 登录页面
    // post: login
    @GetMapping(value="/login")
    public String login(){
        return "login";
    }

    // 未授权页面
    @GetMapping(value="/noAuth")
    public String noAuth(){
        return "error/noAuth";
    }

    // 提交车停车场信息页面
    // post: addInfo
    @GetMapping(value="/add_info")
    public String addInfo(){
        return "fore/addInfo";
    }

    // 提交停车场详情图片页面
    // post: addInfoImage
    @GetMapping(value="/add_info_image")
    public String addInfoImage(){
        return "fore/addInfoImage";
    }

    // 测试页面  查看停车场所有图片
    // get: listInfoImages
    @GetMapping(value="/list_info_images")
    public String listInfoImages(){
        return "fore/listInfoImages";
    }

    // 我提交的停车场信息页面
    // get: mySubmitInfos
    @GetMapping(value="/my_submit_infos")
    public String mySubmitInfos(){
        return "fore/mySubmitInfos";
    }
    @GetMapping(value="/add_review")
    public String addReview(){
        return "fore/addReview";
    }
    @GetMapping(value="/info_detail")
    public String infoDetail(){
        return "fore/infoDetail";
    }
    @GetMapping(value="/add_temp")
    public String addTemp(){
        return "fore/addTemp";
    }


    @GetMapping(value="/admin")
    public String adminHome(){
        return "admin/home";
    }


}
