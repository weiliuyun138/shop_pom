package com.qf.aop;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
public class LoginAop {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 环绕增强,判断当前的目标方法是否作用于登录环境
     */
//    @Around("execution(* **.*(..))")
    @Around("@annotation(IsLogin)")
    public Object isLogin(ProceedingJoinPoint joinPoint) {
        try {
            //判断是否登录?
            //1.获得请求中cookie中的login_token
                //1.1.获得request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
                //1.2.通过request获得cookie
            String loginToken = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login_token")) {
                    //登录的凭证
                    loginToken = cookie.getValue();
                    break;
                }
            }
//            System.out.println("获得cookie中的凭证: "+loginToken);

            //判断凭证是否为空
            User user = null;
            if (loginToken != null) {
            //说明有凭证, 但是不一定代表登录, 通过凭证查询redis
            //2.根据login_token查询redis
                user = (User) redisTemplate.opsForValue().get(loginToken);
            }
//            System.out.println("获得redis中的用户信息: " + user);

            //3.判断是否登录(判断得到的用户信息是否为空)
            //  未登录
            if (user == null) {
                //未登录
                //3.1如果未登录,判断IsLogin的mustLogin是否为true
                //3.1.1.获得@IsLogin
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                IsLogin isLogin = method.getAnnotation(IsLogin.class);
                //3.1.2.通过注解获得方法返回
                boolean mustLogin = isLogin.mustLogin();
                //3.1.3.判断mustLogin,选择是否强制跳转到登录页面
                if (mustLogin) {//强制跳转到登录页面
                    //获得当前请求路径
                    String returnUrl = request.getRequestURL().toString();
                    //获得当前路径的请求参数, ?后面的参数 --->get的请求方式
                    String params = request.getQueryString();
    //                request.getParameterMap(); //获得post请求体中的参数
                    returnUrl += "?" + params;
                    try {
                        returnUrl = URLEncoder.encode(returnUrl, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //跳转到登录页面
                    return "redirect:http://localhost:1119/sso/tologin?returnUrl="+returnUrl;
                }
            }
            //  已登录
            //4.将user对象设置到目标方法的形参中
                //4.1 获得目标方法的原来的参数列表
            Object[] args = joinPoint.getArgs();
//            System.out.println(Arrays.toString(args));
                //4.2 在茫茫多的实际参数中找到一个类型为user的参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && args[i].getClass() == User.class) {
                    args[i] = user;
                }
            }

//            System.out.println("获得当前新的参数列表: " + Arrays.toString(args));
            //执行目标方法

            Object result = joinPoint.proceed(args);
            return result;
//          //测试自定义注解是否成功
//            System.out.println("前---");
//            Object proceed = joinPoint.proceed();
//            System.out.println("后---");
//            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

}
