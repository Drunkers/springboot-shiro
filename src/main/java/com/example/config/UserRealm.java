package com.example.config;

import ch.qos.logback.core.db.dialect.SybaseSqlAnywhereDialect;
import com.example.entity.User;
import com.example.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的realm 需要extends AuthorizingRealm 然后重写其方法
public class UserRealm extends AuthorizingRealm {
    //从数据库取数据，下面测试用户名及密码可删除
    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        //授权（但没经过判断，每个用户都可以去add，so如下获取当前用户信息，拿到其权限
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("add");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前用户信息,(
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal(); //本应该是个object对象，强转为User
        //设置当前用户权限，从数据库获取
        info.addStringPermission(currentUser.getPerms());
        return info;
//        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证");

        //测试用户名 密码
//        String name = "root";
//        String password = "123456";
//        UsernamePasswordToken userToken =   (UsernamePasswordToken) token;
//        if(!userToken.getUsername().equals(name)){
//            return null; //抛出UnknownAccountException 异常 用户名错误
//        }

        UsernamePasswordToken userToken =   (UsernamePasswordToken) token;
        //开始连接真实的数据库
        User user = userService.selectUser(userToken.getUsername());
        if(user == null){
            return null; //抛出UnknownAccountException 异常 用户名错误
        }
        //存取登录用户session
        Subject CurrentSubject = SecurityUtils.getSubject();
        Session session = CurrentSubject.getSession();
        session.setAttribute("loginUser",user);


        //密码认证 shiro自己做
//        return new SimpleAuthenticationInfo("",password,"");
//        return new SimpleAuthenticationInfo("",user.getPass(),"");
        return new SimpleAuthenticationInfo(user,user.getPass(),"");//这里传user对象供授权拿到用户信息

    }
}
