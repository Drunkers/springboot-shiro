package com.example.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class MyController {
    @RequestMapping({"/","/index"})
    public String ToIndex(Model model){
        model.addAttribute("msg","hello,shiro,dsad");
        return  "index";
    }

    @RequestMapping("/admin/add")
    public String add(){
        return "admin/add";
    }

    @RequestMapping("/admin/update")
    public String update(){
        return "admin/update";
    }
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(String name,String password,Model model){
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);
        try {
            subject.login(token);//执行了登录方法，没有异常就ok
            return  "index";
        }catch (UnknownAccountException e){//用户名不对
            model.addAttribute("msg","用户名错误");
            return "login";
        }catch (IncorrectCredentialsException e){//密码不对
            model.addAttribute("msg","密码错误");
            return "login";
        }

    }
    @RequestMapping("/nopers")
    @ResponseBody
    public String unauthorized(){
        return "未经授权页面";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session, Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg","安全退出！");
        return "login";
    }
}
