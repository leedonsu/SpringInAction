package com.kakao.module.chapter3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 *
 * Created by astone35 on 2017. 3. 8..
 * 89P 코드 3.4 빈의 조건 설정하기
 */

@Configuration
public class MagicConfig {

    @Bean
    @Conditional(MagicExistsCondition.class)
    public MagicBean magicBean() {
        return new MagicBean();
    }

}
