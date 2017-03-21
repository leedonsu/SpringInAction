# 3. 고급 와이어링
기초적인 빈 와이이어링 기술보다 향상된 고급 와이어링 기술을 다룬다.  
(번역된 한글이 좀 애매합니다;)
* 스프링 프로파일
* 조건 빈 선언
* 오토 와이어링과 애매성
* 빈 범위
* 스프링 표현 언어  

## 3.1 환경과 프로파일
> 개발/QA/서비스 환경에 따라 변경될 수 있는 일반적인 문제를 해결하기 위한 내용입니다.  
  여기서는 데이터베이스를 예로 들고 있습니다.  
  
 
개발 환경에서는 로컬에 있는 데이터베이스를 사용하도록 설정한다.


    @Bean(destroyMethod = "shutdown")
    public DataSource embeddedDataSource() {                // javax.sql.DataSource 빈을 사용
        return new EmbeddedDatabaseBuilder()                
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")          // 스키마
                .addScript("classpath:test-data.sql")       // 테스트 데이터
                .build();
    }    
    // H2는 Database
    
실서비스 환경에서는 따로 구성된 데이터베이스를 사용한다.

    @Bean
    public DataSource jndiDataSource() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/myDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }
    // JNDI를 이용하여 DataSource 빈 구성 
    
QA 테스트 환경에서는 원격지에 있는 데이터베이스를 사용할 수 있다.

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:tcp://dbserver/~/test");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        dataSource.setInitialSize(20);
        dataSource.setMaxActive(30);
        return dataSource;
    }
    // QA환경에선 외부에 설치된 DB를 구성하여 DB풀을 구성
    
환경에 따라 데이터베이스 접근 방법이 모두 다르다.  
환경에 따라 적절한 빈을 선택하도록 하는 방법이 프로파일을 설정하는 방법이다.  

다른 방법이 있다면 클래스나 xml파일을 분리하고, 컴파일시에 결정하여 빌드하는 방법이 있지만  
각 환경에 따라 빌드 바이너리가 달라 실수를 범할수 있다.


### 3.1.1 빈 프로파일 설정하기
프로파일 설정을 통해 빌드시 결정을 하는 것이 아니라 스프링은 런타임시에 결정을 기다린다.  
그래서 동일 바이너리(예를 들면 war파일)로 각각의 환경을 구동할 수 있게 된다.

**빈이 속한 프로파일을 지정하려면 @profile 애너테이션으로 한다.**  
**클래스 단위로만 지정이 가능하다.**  
**해당 프로파일이 활성화 되지 않으면 @Bean 애너테이션은 무시된다.(빈은 생성되지 않는다.)**

[XML로 프로파일 설정하기](https://github.com/leedonsu/SpringInAction/blob/chapter3/paul/src/main/java/resource/datasource-config.xml)  
여러 개의 xml파일로 빈 설정이 나눠져있어도 프로파일 설정 효과는 동일하다.  
bean id가 동일한 상태여도 프로파일에 따라 1개의 빈만 생성된다.  


### 3.1.2 프로파일 활성화하기

프로퍼티를 활성화 시킬수 있는 방법

* DispatcherServlet에 초기화 된 파라미터
* 웹 애플리케이션의 컨텍스트 파라미터
* JNDI 엔트리
* 환경 변수
* JVM 시스템 프로퍼티
* 통합 테스트 클래스에서 @ActiveProfiles 애너테이션 사용

[DispatcherServlet에 초기화된 파라미터](https://github.com/leedonsu/SpringInAction/blob/chapter3/paul/src/main/java/resource/web.xml)
이렇게 지정하면 개발에 참여한 개발자들이 추가 설정 없이 개발환경(dev)으로 적용해서 사용 할 수 있다.

(spring.profile.active와 spring.profile.defalut에 대해 차이를 잘 모르겠음)   
(active는 무조건 bean을 생성하고 default는 조건부로 빈을 생성한다는건지..)
  
JVM 시스템 프로퍼티:  

    -Dspring.profiles.active=dev

스프링은 테스트를 실행할 때 활성화될 필요가 있는 프로파일을 지정할 수 있도록 @ActiveProfiles 애너테이션을 제공한다.

    @RunWith(SpringJUnit4ClassRunner.class)  
    @ContextConfigureation(classes={PersistenceTestconfig.class}
    @ActiveProfiles("dev")      // dev 프로파일로 활성화 시킴
    public class PersistenceTest {
        ...
    }

## 3.2 조건부 빈
특정 환경변수가 설정되어있을 때 빈을 생성할때 조건부 빈을 사용할 수 있다.
@Conditional 애너테이션을 사용한면 된다. 조건이 참이 되면 빈이 생성된다.

    @Bean
    @Conditional(MagicExistsCondition.class)
    public MagicBean magicBean() {
        return new MagicBean();
    }

@Conditional은 Condition 인터페이스를 사용한다.(책이 당연한 소릴...)  
@Condition 애너테이션을 사용한 경우 조건에 들어가는 클래스 등과 맞는 경우 true를 리턴하고, 빈을 생성한다.

    public interface Condition {
        boolean matches(ConditionContext var1, AnnotatedTypeMetadata var2);
    }
   

환경변수에 magic이 있는지 체크 해서 있는 경우 빈을 생성하도록 하는 예제  
[조건부 빈 예제](https://github.com/leedonsu/SpringInAction/blob/chapter3/paul/src/main/java/com/kakao/module/chapter3/MagicExistsCondition.java)


## 3.3 오토와이어링의 모호성
오토와이어링 당시에 정확히 하나의 빈과 일치하여 생성되어야 하지만 일치하는 빈이 여러 개인 경우 오토와이어링이 실패한다.

    @Autowired
    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }  
      
    @Component
    public class Cake implements Dessert { ... }  
      
    @Component
    public class Cookies implements Dessert { ... }  
      
    @Component  
    public class IceCream implements Dessert { ... }  
    
@Component 애너테이션으로 컴포넌트 스캐닝 중 찾을수 있고, 빈으로 생성할 수 있다.  
setDessert에서 명확한 Dessert를 찾을수 없어 스프링은 예외를 발생시킨다.


    nested exception is
      org.springframework.beans.factory.NoUniqueBeanDefinitionException:
    No qualifying bean of type [com.desserteater.Dessert] is defined:
      expected single matcing bean but found 3: cake,cookies,iceCream
    

### 3.3.1 기본 빈 지정
하나의 디저트만 고르도록 하려면 어떻게 할까?  
빈 선언시 기본 빈 설정을 하면 된다. @Primary 

    @Component  
    @Primary
    public class IceCream implements Dessert { ... }  

또는 자바 설정에서 명시적으로 빈을 선언한다.

    @Bean
    @Primary
    public Dessert iceCream() {
        reuturn new IceCream();
    }
    
또는 XML 설정에서

    <bean id="iceCream"
          class="com.desserteater.IceCream"
          primary="true" />
          
          
아이스크림 말고 케잌에도 @Primary 애너테이션을 적용하면 오토와이어링에 실패하게 된다.

### 3.3.2 오토와이어링 빈의 자격
@Primary가 여러개 있으면 명확하게 어떤 빈을 생성할 것인가 스프링은 고민에 빠지게 된다.
@Qualifier 애너테이션은 빈의 아이디를 넣어 해당 클래스가 인스턴스로 생성될 경우 참조하도록 된다.
iceCream 아이디를 가지는 빈을 참조하며, 메소드의 디펜던시는 IceCream 클래스에 생긴다. 

    @Autowired
    @Qualifier("iceCream")
    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }
    

맞춤형 수식자 만들기
IceCream 클래스를 cold라는 수식자로 alias하는 기능이다.

    @Component  
    @Qulifier("cold")
    public class IceCream implements Dessert { ... }  
    
    @Autowired
    @Qualifier("cold")
    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }
    
    @Bean
    @Qualifier("cold")
    public Dessert iceCream() {
        reuturn new IceCream();
    }


## 3.4 빈 범위
기본적으로 스프링에서 생성되는 빈은 싱글톤이다.

* 싱글톤 : 전체 애플리케이션을 위해 생성되는 인스턴스 하나
* 프로토타입 : 빈이 주입될 때마다 생성되거나 스프링 애플리케이션 컨텍스트에서 얻는 인스턴스 하나
* 세션 : 웹 애플리케이션에서 각 세션용으로 생성되는 인스턴스 하나
* 요청 : 웹 애플리케이션에서 각 요청용으로 생성되는 인스턴스 하나

빈의 범위를 지정하기 위해 @Scope 애너테이션을 사용한다.

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public class Notepad { ... } 
    
기본은 싱글톤이고, 위의 예제는 프로토타입으로 범위를 지정한다.
@Scope("prototype")도 가능하지만 상수를 사용하여 안전하게 지정할 수 있다.


자바설정과 xml설정
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Notepad notepad() {
        return new Notepad();
    }
    
    <bean id="notepad"
          class="com.myapp.Notepad"
          scope="prototype" />
    

### 3.4.1 요청과 세션 범위 작업하기
특정 요청을 하거나 세션 범위내에서 공유하는 bean을 생성할 수 있다.

온라인 쇼핑몰 장바구니로 예를 들어 세션범위 지정하기

    @Component
    @Scope(value=WebApplicationContext.SCOPE_SESSION,
           proxyMode=ScopedProxyMode.INTERFACES
          )
    public SoppingCart cart() { ... } 

proxyMode는 범위프록시를 지정한다.
(CGLIB에 대한 부분까지 언급하는데 아직 이해를 못해서 설명은 패스)

                                          ,--------- 인터페이스 | 세션요청 범위 빈
               주입                       /
    싱글톤 빈 <-------- 인터페이스 | 범위프록시 ------------ 인터페이스 | 세션요청 범위 빈
                                        \
                                         `---------- 인터페이스 | 세션요청 범위 빈
                                         


### 3.4.2 XML로 범위 프록시 선언하기
프록시 모드로 사용하려면 AOP 네임스페이스를 사용해야한다. (aop 관련된 내용은 4장에 언급된다.)

    <bean id="cart"
          class="com.myapp.ShoppingCart"
          scope="sesssion">
          <aop:scoped-proxy />
    </bean>



## 3.5 런타임 값 주입
2장에서 보았던 예제를 가지고 빈 와이어링의 빈 프로퍼티 혹은 생성자에 값을 연결하는 예제를 본다.
 
    @Bean
    public CompactDisc sgtPeppers() {
        return new BlankDisc(
            "Sgt. Pepper's Lonely Hearts Club Band",    // 제목
            "The Beatles");                             // 아티스트
    }

제목과 아티스트가 하드코딩 되어있다.
제목과 아티스트 값이 런타임에 결정될수도 있다.

* 프로퍼티 플레이스홀더(Property placeholders)
* 스프링 표현 언어(SpEL, Spring Expression Language)


### 3.5.1 외부 값 주입
외부값을 간단하게 처리하는 방법은 프로퍼티 소스 선언과 프로퍼티 검색이다.

    package com.soundsystem;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.PropertySource;
    import org.springframework.core.env.Environment;
    
    @Configuration
    @PropertySource("classpath:/com/soundsystem/app.properties")   // 프로퍼티 소스 선언
    public class EnvironmentConfig {
    
      @Autowired
      Environment env;
      
      @Bean
      public BlankDisc blankDisc() {
        return new BlankDisc(
            env.getProperty("disc.title"),                          // 프로퍼티 값 얻기
            env.getProperty("disc.artist"));
      }
    }       
    

@PropertySource는 프로퍼티 소스를 읽도록 하는 애너테이션이다.
    
    disc.title=Sgt. Peppers Lonely Hearts Club Band
    disc.artist=The Beatles


프로퍼티가 없는 경우 에러를 내고 싶으면 아래와 같이 정의하면 된다.
프로퍼티가 정의되지 않으면 IllegalStateException이 발생된다.

    env.getRequiredProperty("disc.artist""));


기본값을 넣기 위해선 아래와 같이 할수 있다.

    @Bean
    public BlankDisc blankDisc() {
      return new BlankDisc(
          env.getProperty("disc.title",  "Rattle and Hum"), 
          env.getProperty("disc.artist", "U2"));
    }

    
**프로퍼티 플레이스홀더 처리하기**
스프링은 프로퍼티 파일로 프로퍼티를 내보내는 옵션을 지원하며 플레이스홀더 값을 사용하여 스프링 빈으로 플러그인 한다.

    <bean id="sgtPeppers"
          class="soundsystem.BlankDisc"
          c:_title="${disc.title}"
          c:_artist="${disc.artist}" />
  
플레이스홀더값은 ${...} 로 쌓여진 프로퍼티명이다.  
title의 생성자 인수에 매핑되는 프로퍼티 명은 disc.title이며 disc.title 값이 들어가게 된다.
이처럼 XML에서 하드코딩 된 값을 사용하지 않는다. 외부에 정의된 값으로 값이 정해진다.
   
코드레벨에서 정의할 때는 @Value 애너테이션을 사용하여 생성자에 사용할 수 있다.
  
      public BlankDisc(
                   @Value("${disc.title}" String title,
                   @Value("${disc.artist}" String artist) {
          this.title = title;
          this.artist = artist;
      }

          
### 3.5.2 스프링 표현식 와이어링
런타임에 실행되는 표현식을 이용하여 빈의 프로퍼티나 생성자 인자에 값을 할당하는 스프링표현 언어가 있다.
스프링 표현언어(SpEL, Spring Expression Language)
SpEL을 이용하면 힘들거나 불가능했던 빈 와이어링 기능을 사용할 수 있다.

* ID로 빈을 참조하는 기능
* 메소드 호출과 객체의 프로퍼티 액세스
* 값에서의 수학적인 동작, 관계와 관련된 동작, 논리연산 동작
* 정규표현식 매칭
* 컬렉션 처리

SpEL은 종속객체 주입보다는 다른 용도로 많이 사용된다.
보안을 정의하거나, MVC 애플리케이션 뷰 템플릿의 모델 데이터를 참조하기 위해 사용된다.

SpEL은 #{...}로 사용할 수 있다.

    #{1}    // 숫자 상수
    #{T(System).currentTimeMillis()}    // 현재시간을 밀리초 단위로 나타냄

T(System)은 java.lang.System 타입을 얘기한다.

    #{sgtPeppers.artist} // sgtPeppers ID를 가진 빈의 artist 값을 사용
    #{systemProperties['disc.title']}   // 시스템 프로퍼티 참조
    
    
프로퍼티 플레이스홀더를 사용하지 않고 SpEL 표현식을 사용한다.
    public BlankDisc(
                   @Value("#{systemProperties['disc.title']}" String title,
                   @Value("#{systemProperties['disc.artist']}" String artist) {
          this.title = title;
          this.artist = artist;
      }


상수값 표시

    #{3.14159}  // 부동소수점
    #{9.87E4}   // 지수
    #{'HELLO'}  // 문자열
    #{false}    // boolean

빈, 프로퍼티, 메소드 참조

    #{sgtPeppers}
    #{sgtPeppers.artist}
    #{artistSelector.selectArtist()}    // artistSelector 빈 아이디를 가지는 인스턴스에 selectArtist()를 호출한다 
    
    // 리턴된 문자열을 대문자로 바꾸는 SpEL
    #{artistSelector.selectArtist().toUpperCase()}  
    
    // NULL을 리턴하는 경우 NullPointerException이 발생하기 때문에
    // 타입세이프 연산자 ? 를 사용하여 보호
    #{artistSelector.selectArtist()?.toUpperCase()}  

표현식에서 타입 사용하기

    T(java.lang.Math)   해당 클래스 객체를 나타낸다.
    T(java.lang.Math).PI
    T(java.lang.Math).random()

SpEL 연산자

    산술 :  +,-,*,/,%,^
    비교 : <, lt, >, gt, ==, eq, <=, le, >=, ge
    논리 : and, or, not, |
    조건 : ?: (ternary, 삼항조건연산), ?: (Elvis)  << 이 엘비스 머리같은 조건은 뭘까요?
    정규표현식 : matches

연산자 예제

    #{2 * T(java.lang.Math).PI * circle.radius)     // 원의 둘레
    #{T(java.lang.Math).PI * circle.radius ^ 2)     // 원의 넓이 
    #{disc.title + ' by ' + disc.artist}            // 문자열 합치기
    #{counter.total == 100}                         // 동일 비교연산
    #{counter.total eq 100}                         // 동일 비교연산
    #{scoreboard.score > 1000 ? "Winner!" : "Loser"}    // 삼항비교연산
    #{disc.title ?: 'Rattle and Hum'}                   // NULL인 경우 기본값을 할당한다
    // 이와 같은 표현식을 엘비스 연산자라 한다.

정규 표현식 사용하기

    텍스트가 특정 패턴과 일치하는지 여부를 확인하기 위해 사용하면 유용할 수 있다.
    #{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}
    
    
컬렉션 사용하기

    #{jukebox.songs[4].title}        // ID가 jukebox인 songs컬렉션 프로퍼티의 다섯번째 title을 사용                          
    #{jukebox.songs[T(java.lang.Math].random() * jukebox.songs.size()].title}
    jukebox의 songs를 무작위로 선택하여 tistle을 가지고 옴
     
    #{'This is a test'[3]}          // 해당 문자열에서 네번째 문자 참조
    #{jukebox.songs.?[artist eq 'Aerosmith']}
    // []내에 다른 표현식을 포함함
    // 셀렉션 연산자  
      
    #{jukebox.songs.^[artist eq 'Aerosmith']}
    // .^[] .$[] 첫번째와 마지막에 일치하는 항목 조회  
      
    #{jukebox.songs.![title]}
    // 컬렉션 요소로부터 프로퍼티를 새로운 컬렉션으로 모으기 위한 프로젝션 연산자
    // song의 컬렉션이 아니더라도 모든 타이틀을 프로젝션한다.
      
    #{jukebox.songs.?[artist eq 'Aerosmith'].![title]}
    // Aerosmith의 모든 노래 리스트를 얻기 위해 사용
    












