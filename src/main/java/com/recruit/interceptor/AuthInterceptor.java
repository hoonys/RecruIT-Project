package com.recruit.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.recruit.controller.AdminController;
import com.recruit.domain.UserVO;
import com.recruit.service.UserService;


//Header로 적용시 제대로 적용되지 않음
public class AuthInterceptor extends HandlerInterceptorAdapter {

  private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
 
  @Inject
  private UserService service;
  
  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    
    HttpSession session = request.getSession();   

    if(session.getAttribute("login") == null){
    
      logger.info("current user is not logined");
      
      saveDest(request);
      
      Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
      
      if(loginCookie != null) { 
        System.out.println("로그인 쿠키 확인 : "+loginCookie);
    	
        UserVO UserVO = service.checkLoginBefore(loginCookie.getValue());
        
        logger.info("USERVO: " + UserVO);
        
        if(UserVO != null){
          session.setAttribute("login", UserVO);
          return true;
        }
        
      }
      
      response.sendRedirect("/");
      return false;
    }
    
    return true;
  } 

  private void saveDest(HttpServletRequest req) {

    String uri = req.getRequestURI();

    String query = req.getQueryString();

    if (query == null || query.equals("null")) {
      query = "";
    } else {
      query = "?" + query;
    }

    if (req.getMethod().equals("GET")) {
      logger.info("dest: " + (uri + query));
      req.getSession().setAttribute("dest", uri + query);
    }
  }

}
