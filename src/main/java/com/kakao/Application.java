package com.kakao;

import net.daum.tenth2.Tenth2Authentication;
import net.daum.tenth2.Tenth2Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        // 신청한 텐스2 서비스id와 r/w키 입력
        Tenth2Authentication.addAccessKey("test", "r_tenth2test", "w_tenth2test");

        // 테스트용 도메인 사용
        Tenth2Connector.setHost(new Tenth2Connector.HostInfo("sa.beta.tset.daumcdn.net"));

        SpringApplication.run(Application.class, args);
    }
}
