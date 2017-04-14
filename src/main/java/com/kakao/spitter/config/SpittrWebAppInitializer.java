package com.kakao.spitter.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by palrang on 2017. 4. 13..
 */
public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
//        return new Class<?>[] {WebConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() { // 설정 클래스를 명시
        return new Class<?>[0];
//        return new Class<?>[] {RootConfig.class};
    }

    @Override
    protected String[] getServletMappings() {   // DispatcherServlet 을 /에 매핑
        return new String[] {"/"};
    }


    /*
       note : 스프링은 SpingServletContainerInitializer 의 구현을 제공하고 이것은 순차적으로 WebApplicationInitailizer의 구현 클래스를 찾아 설정을 위임한다.
     */
}
