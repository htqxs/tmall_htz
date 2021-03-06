package com.htz.tmall.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class BaseForeServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = (String) request.getAttribute("method");
        try {
            Method method = getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            String returnValue = (String) method.invoke(this, request, response);
            if(returnValue != null) {
                if (returnValue.startsWith("@")) {
                    //重定向
                    response.sendRedirect(request.getContextPath() + "/" + returnValue.substring(1));
                } else if (returnValue.startsWith("%")) {
                    response.getWriter().write(returnValue.substring(1));
                } else {
            request.getRequestDispatcher("/" + returnValue).forward(request, response);
        }
    }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/errorFore.jsp");
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象序列化为json数据并写回到客户端
     * @param obj
     * @param response
     */
    public void writeValue(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), obj);
    }
}
