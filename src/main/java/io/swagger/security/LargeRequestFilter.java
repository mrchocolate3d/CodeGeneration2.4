package io.swagger.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Log
@Component
@Order(1)
public class LargeRequestFilter implements Filter {

    private int maxSize = 1000;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        int size = request.getContentLength();

        if(size > maxSize){
            log.warning("Request too large");
             throw new ServletException("Rejected. Request too large");
        }else{
            chain.doFilter(request, response);
        }
    }
}
