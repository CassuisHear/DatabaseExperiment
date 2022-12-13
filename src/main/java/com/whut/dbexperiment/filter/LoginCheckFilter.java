package com.whut.dbexperiment.filter;

import com.alibaba.fastjson.JSON;
import com.whut.dbexperiment.common.BaseContext;
import com.whut.dbexperiment.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录，
 * 当用户还没有登录时，不能够直接在地址栏中输入请求路径来获取数据
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求: {}", requestURI);

        //2、判断本次请求是否需要处理
        //如果是以下请求就就不用进行处理
        String[] urlsNotProcess = new String[]{
                "/user/login",
                "/user/logout",
                "/static/**"
        };

        //3、如果不需要处理，则直接放行
        if (urlMatch(urlsNotProcess, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，id 为: {}", request.getSession().getAttribute("user"));

            //在这里设置当前用户的 id 值，令这个 id 值和当前线程绑定
            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //5、如果未登录则返回未登录结果，通过输出流的方式向客户端页面响应数据
        log.info("用户未登录...");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查此次请求是否不需要处理
     *
     * @param urlsNotProcess 不需要处理的请求集合
     * @param requestURI     待检查的请求
     * @return 返回true表示 requestURI 确实不需要处理，否则需要处理
     */
    private boolean urlMatch(String[] urlsNotProcess, String requestURI) {
        for (String urlNotProcess : urlsNotProcess) {
            if (PATH_MATCHER.match(urlNotProcess, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
