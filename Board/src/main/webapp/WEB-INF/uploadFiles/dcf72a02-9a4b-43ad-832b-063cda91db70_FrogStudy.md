## DB *

    DB 쿼리 작성시 *(asterisk)는 가급적 사용 안하는게 좋습니다. 실무에서 개발할 때 검색속도, 로딩속도 등에 영향을 미치기 때문입니다.
    또한 쿼리태그 작성시 작은따옴표를 안쪽에 두고 큰따옴표를 바깥쪽에 두는게 좋습니다. 그 이유는 문자열을 비교할 때에는 상관없지만 
    숫자랑 비교할 경우 자동으로 바인딩 되기때문에 오류가 날 수 있습니다.

    ```sql 
    <select id="countPopup" parameterType="popup" resultType="int">
		<![CDATA[
			SELECT
				COUNT(1)
			FROM MEC_C_POPUP
			WHERE DEL_STS = 'N'
			AND SITE_CODE = #{siteCode}
			AND POPUP_DISPLAY_GUBUN = #{popupDisplayGubun}
		]]>
		<if test='gubun != "now"'>
			AND NOT SYSDATE() BETWEEN POPUP_START_DAY AND POPUP_END_DAY
		</if>
		<if test='gubun == "now"'>
			AND SYSDATE() BETWEEN POPUP_START_DAY AND POPUP_END_DAY
		</if>
	</select>
    ```

## count(1)

    https://hue9010.github.io/db/select_count/

## 어노테이션 공부 

    @RequiredArgsConstructor

    @Alias("popup") 
    class MecPopupVo 

    위 처럼 alias를 지정할 경우 <select id="countPopup" parameterType="popup" resultType="int">와 같이 파라미터 타입에서 줄여 쓸 수 있다.


    @Getter @Setter

## 인터셉터, 필터 공부 

## 커스텀 태그 라이브러리(tld)

## ENGINE=InnoDB

## mybatis-config.xml 설정 usecache

    mybatis-config.xml 설정 파일에서 usecache를 false로 하는 것보다 true로 해놓고 
    < select id="abc" ... useCache="false" >  같은 방식으로 하는게 좋습니다.
    useCache는 쿼리문안에 if태그 같은걸 사용하여 조회할때에 전에 조회한 내용이랑 충돌이 일어나지 않게 해주는 역할을 합니다.

## log4j mysql 설정 

## 롬복 설치 방법

## 팝업 공부 

## 사이트코드

    localhost8080:/seoul/~ 처럼 seoul을 사이트 코드라하는데, contextPath랑 비슷합니다. 단, contextPath는 서버단위로 하나만 설정 할 수있는 것이고
    사이트 코드는 한 서버내에 여러개 존재 가능합니다.

## 추상화(모델링)

    > 추상화 ?
    >
    > 구체적인 것을 분해해서 관심 영역(애플리케이션 경계, Application Boundary)에 있는 특성만 가지고 재조합 하는 것을 말합니다. 
    추상화는 모델링 이라고도 합니다.
    
    모델(Model)은 실제 사물을 정확히 복제하는 것이 아닌 __목적에 맞게 관심있는 특성만을 추출해서 표현__ 하는 것입니다. 
    즉, 추상화를 통해서 실제 사물을 단순하게 묘사하는 것을 말합니다. 

    > 클래스 설계에서 가장 중요한 것은 추상화이며, 추상화의 결과물이 모델입니다.

    - 클래스 설계를 위해서는 애플리케이션 경계 부터 정해야 합니다.
    - 객체지향에서 추상화의 결과는 클래스 입니다.
    - 클래스 설계에서 추상화가 사용됩니다.
    - 상속을 통한 추상화, 구체화
    - 인터페이스를 통한 추상화
    - 다형성을 통한 추상화 

    __자바는 객체 지향의 추상화(모델링)을 class 키워드로 지원하고 있습니다.__

## static 

    static 키워드가 붙은 것을 클래스 멤버(정적 멤버)라고 하는데, 이 static 키워드는 __클래스의 모든 객체들이 같은 값을 가질때 사용__ 하는것이 정석입니다.
    그 외의 경우에도 사용할 수 있지만, 정당한 이유가 있어야 합니다.

    - 보병 클래스의 주특기 번호
    - 남자 클래스의 주민등록번호 성별코드
    - 고양이 클래스의 다리 개수 

    > static 키워드가 붙은 애들은 T 메모리의 스태틱 영역에 배치 됩니다.

## main() 메서드가 static 인이유 

    T 메모리가 초기화된 순간 객체는 하나도 존재하지 않기 때문에 객체 멤버 메서드를 바로 실행할 수 없습니다. 따라서 main() 메서드를 정적 메서드로 두는 것입니다.

## 추상화(일반화) vs 구체화(특수화)

    객체 지향에서 상속이란 일반인들이 생각하는 상속이 아닌 확장, 세분화, 슈퍼클래스 - 서브클래스(상위클래스 - 하위클래스) 개념으로 이해하는게 좋습니다.

    동물 - 포유류 / 조류 - 고래 박쥐 / 참새 팽귄 다음과 같이 되어있을경우 고래, 참새부터 동물로 갈 수록 `추상화(일반화)`라고하며, 동물에서 아래로 내려갈 수록
    `구체화(특수화)` 라고 합니다.

    이 상속 관계가 성립하기 위해서는 __IS-A : 하위클래스는 상위클래스이다__ 라는 문장이 성립해야 합니다.

    - XXX는 사람이다.
    - 강아지는 동물이다.