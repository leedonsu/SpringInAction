# 2. 빈 와이어링
DI 개념의 핵심은 애플리케이션 객체 간의 연간관계 형성 작업이며, 이를 보통 **와이어링**이라 한다.
  
## 2.1 스프링 설정 옵션 알아보기
스프링은 세 가지 기본적인 와이어링 메커니즘을 제공한다.  
  
* XML을 통한 명시적 설정
* 자바를 통한 명시적 설정
* 자동 와이어링
  
와이어링 설정 방법엔 정답이 없다. 그렇지만 할 수만 있다면 **자동 설정을 추천**한다. 명시적인 설정이 적을수록 좋다. 명시적인 빈 설정을 해야 할 때는 type-safe를 보장하고 더욱 강력한 **JavaConfig를 XML보다 선호**한다.  
  
## 2.2 자동으로 빈 와이어링하기
스프링은 두 가지 방법으로 오토와이어링을 수행한다.  
  
* 컴포넌트 스캐닝 - 애플리케이션 컨텍스트에서 생성되는 빈을 자동으로 발견
* 오토와이어링 - 자동으로 빈 의존성을 충족
  
위 두 개념을 설명하기 위해 몇 개의 빈을 만들어 보자.  
  
### 2.2.1 발견 가능한 빈 만들기
자바로 CD 개념을 만들어보자.  

**[public interface CompactDisc](https://github.com/leedonsu/SpringInAction/blob/chapter2/ellie/src/main/java/com/kakao/module/chapter2/CompactDisc.java)**  
CD 개념을 정의한다.  

**[public class SgtPeppers implements CompactDisc](https://github.com/leedonsu/SpringInAction/blob/chapter2/ellie/src/main/java/com/kakao/module/chapter2/SgtPeppers.java)**  
@Component로 어노테이트된 CompactDisc 구현.  
이 간단한 어노테이션은 클래스를 빈으로 만들어야 함을 스프링에 단서로 제공한다.  
하지만 컴포넌트 스캐닝은 기본적으로 켜 있지는 않다.

**[public class CDPlayerConfig](https://github.com/leedonsu/SpringInAction/blob/chapter2/ellie/src/main/java/com/kakao/module/chapter2/CDPlayerConfig.java)**  
@ComponentScan은 설정 클래스로서 동일한 클래스를 기본 스캐닝한다.  
스프링은 CDPlayerConfig가 포함된 패키지와 하위 패키지를 스캔하고, @Component로 애너테이트된 클래스를 찾는다.  
CompactDisc 클래스를 찾고, 자동으로 빈을 만든다.  
  
CDPlayerConfig 클래스는 자바로 스프링 와이어링 스펙을 정의한다.  
XML 설정을 통해서 컴포넌트 스캐닝을 활성화하려면 그때는 스프링의 컨텍스트 네임스페이스(context namespace)로부터 <context:component-scan> 요소를 사용한다.  
  
**[public class CDPlayerTest](https://github.com/leedonsu/SpringInAction/blob/chapter2/ellie/src/test/java/com.kakao/chapter2/CDPlayerTest.java)**  
컴포넌트 스캐닝에 의해 발견된 CompactDisc를 테스트한다.  
  
SpringJUnit4ClassRunner를 이용하면 테스트 시작 시 스프링 어플리케이션 컨텍스트가 자동으로 생성된다.   
@ContextConfiguration 애너테이션은 CDPlayerConfig 클래스를 통해서 설정을 로드한다.  
설정 클래스는 @ComponentScan을 가지므로 애플리케이션 컨텍스트는 CompactDisc 빈을 포함한다.  

증명을 위해 테스트 클래스는 @Autowired로 애너테이트된 CompactDisc 타입 프로퍼티를 가진다. (@Autowired는 뒤에서 논의)   
  
이 간단한 테스트 메소드는 cd 프로퍼티가 널(null)이 아님을 확인한다.  
널이 아니라는 것은 스프링 CompactDisc 클래스를 찾아서 스프링 애플리케이션 컨텍스트에서 빈으로 자동 생성하고, 테스트로 주입했다는 의미이다.  
  
### 2.2.2 컴포넌트 스캔된 빈 명명하기
스프링 애플리케이션 컨텍스트에서 모든 빈은 ID가 주어진다.  
ID가 명시적으로 주어지지 않을 경우 클래스 명으로부터 유추되어 할당된다. (빈은 클래스 명의 첫 글자를 소문자로 바꾼 ID를 가진다.)  
만약 빈에 다른 ID를 주고 싶다면 @Componet 애너테이션에 원하는 ID를 값으로 넣어주면 된다.

    @Component("lonelyHeartsClub")
    public class SgtPeppers implements CompactDisc {
		... 
	}￼
  
### 2.2.3 컴포넌트 스캐닝을 위한 베이스 패키지 세팅
