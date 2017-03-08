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
  
 
개발 환경에서는 로컬에 있는 데이터베이스를 사용한다.


    @Bean(destroyMethod = "shutdown")
    public DataSource embeddedDataSource() {                // javax.sql.DataSource 빈을 사용
        return new EmbeddedDatabaseBuilder()                
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")          // 스키마
                .addScript("classpath:test-data.sql")       // 테스트 데이터
                .build();
    }    
    
    
실서비스 환경에서는 따로 구성된 데이터베이스를 사용한다.

    @Bean
    public DataSource jndiDataSource() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/myDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }
    
    
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

[DispatcherServlet에 초기화된 파라미터](https://github.com/leedonsu/SpringInAction/blob/chapter3/paul/web/WEB-INF/web.xml)
이렇게 지정하면 개발에 참여한 개발자들이 추가 설정 없이 개발환경(dev)으로 적용해서 사용 할 수 있다.

(spring.profile.active와 spring.profile.defalut에 대해 차이를 잘 모르겠음)   
(active는 무조건 bean을 생성하고 default는 조건부로 빈을 생성한다는건지..)

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
    public MagicBean magicBead() {
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

IceCream 클래스명을 cold라는 수식자로 alias거는 기능이다.

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
### 3.4.1 요청과 세션 범위 작업하기
### 3.4.2 XML로 범위 프록시 선언하기

## 3.5 런타임 값 주입
### 3.5.1 외부 값 주입
### 3.5.2 스프링 표현식 와이어링


