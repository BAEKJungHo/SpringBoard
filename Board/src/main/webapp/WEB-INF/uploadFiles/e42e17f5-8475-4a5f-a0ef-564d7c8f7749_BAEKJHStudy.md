## LEFT OUTER JOIN

    SELECT 
	B.NUM, 
	B.TITLE, 
	B.DATE, 
	B.COUNT, 
	B.CONTENTS, 
	B.ID, 
	B.ATCH_FILE_ID,
	F.atch_file_id,
	F.del_chk
FROM 
	B_BOARD AS B
JOIN B_USERS AS U
ON (B.ID = U.ID)
LEFT JOIN B_FILEDETAIL F
ON (B.ATCH_FILE_ID = F.ATCH_FILE_ID)
WHERE NUM = 5 
AND F.del_chk = 'N'


여기서 B_FILEDETAIL 테이블의 값이 없는경우 FROM절과 JOIN을 수행하고나서  WHERE절로 NUM(게시물 번호)이 5인애와 FILEDETAIL의 삭제상태가 'N'인 애들을 판단하기 때문에 정상적으로 나오지 않습니다. 

위 쿼리를 정상적으로 실행 시키기 위해서는 아래와 같이 하면 됩니다.

SELECT 
	B.NUM, 
	B.TITLE, 
	B.DATE, 
	B.COUNT, 
	B.CONTENTS, 
	B.ID, 
	B.ATCH_FILE_ID,
	F.atch_file_id,
	F.del_chk
FROM 
	B_BOARD AS B
JOIN B_USERS AS U
ON (B.ID = U.ID)
LEFT JOIN B_FILEDETAIL F
ON (B.ATCH_FILE_ID = F.ATCH_FILE_ID
	and F.del_chk = 'N');
WHERE NUM = #{NUM}
and B.DEL_CHK = 'N' 
and F.DEL_CHK = 'N'


    A와 B를 left outer 조인 한다고 하였을 때, B의 결과가 null이더라도 A의 결과에 대한 컬럼을 가져와서 붙이기 때문에, 정상적으로 쿼리가 출력됩니다.
    만약 B의 결과가 null인 경우 inner join을 하게 되면 쿼리 결과가 나오지 않습니다. 

## input hidden으로 post로 데이터를 넘기는 방법 

    <input type="hidden" name="num" value="${boardDTO.num}" /> input hidden 태그를 form에 감싸서 보내면
    requestmethod=post를 적용한 컨트롤러에서도 템플릿 변수와 @PathVariable을 사용하여, input hidden의  name 이름으로 사용할 수 있습니다.

    ```java 
    @RequestMapping(value="/boardEdit/{num}", method=RequestMethod.POST)
	public String boardEdit(@Valid BoardDTO boardDTO, @PathVariable int num, BindingResult bindingResult) { }
    ```

## 상속

    상속관계를 is-a라고 많이들 알고 있는데 더 정확한 표현은 `is a kind of`입니다.

    - 뽀로로 is a 펭귄 : 뽀로로는 한 마리 펭귄이다.

    마이크로소프트 개발자 사이트인 MSDN에서도 상속을 표현하는 is a를 더 명확히 표현하면 a kind of라고 명시되어 있습니다.

    - 펭귄 is a kind of 조류 : 펭귄은 조류의 한 분류이다.

## 인터페이스 

    인터페이스는 `be able to`로 표현하는것이 좋습니다. 즉, ~할 수 있는이라는 의미를 가지고 있습니다.

    Cloneable : 복제할 수 있는, Comparable : 비교할 수 있는

    1. 상위 클래스는 하위 클래스에게 물려줄 특성이 많을수록 좋은가?
    2. 인터페이스는 구현을 강제하는 메서드가 많을수록 좋은가? 

    위 2개의 질문에 대한 답은 상위클래스는 물려줄 특성이 많을수록 좋고, 인터페이스는 구현을 강제하는 메서드가 적을 수록 좋습니다.
    1번에 대한 이유는 `LSP(리스코프 치환 원칙)`에 따른 이유 입니다. 2번에 대한 이유는 `ISP(인터페이스 분할 원칙)`에 따른 이유입니다.

## 티베로를 사용하는 이유

    공공기관 프로젝트에 따라 다르지만, 티베로가 국산 DBMS이기 때문에, 이 DB를 사용하면 가산점을 받는다던지 그런 이점이 있습니다.

    cms가 한가지 db만을 사용하는것은 아니기 때문에, cms에서 사용하는 db에 맞게 mapper.xml도 여러개 작성해줘야 합니다.

    ex) mysql, tibero를 사용하면 이 2가지에 대한 Mapper.xml을 작성해야함

## get 방식 

    "redirect:/boardEdit/"+num; 방식으로도 데이터를 넘겨줄 수 있다.

## mybatis-config.xml 

    마이바티스 설정파일을 자바로 설정하는 이유는  자바로하면 디버깅을 사용할 수 있기 때문입니다.

    ```java
@Configuration
@MapperScan(basePackages = "net.mayeye.*.*.*.repository")
public class MybatisConfig {

}
    ```

    @MapperScan(basePackages = "net.mayeye.*.*.*.repository")에 의해서 repository 패키지 안에 있는 DAO Interface들이 구현체로 인식이되어 바로 Mapper.xml과 통신할 수 있습니다.



## Spring Assert 

    https://www.baeldung.com/spring-assert

    Spring Assert 클래스는 인수를 검증하는 데 도움이됩니다. Assert 클래스의 메서드를 사용하여 , 우리는 우리가 기대하는 가정을 작성할 수 있습니다. 그리고 이들이 충족되지 않으면 RuntimeException(Unchecked Exception)이 발생합니다.

## 시리얼라이즈, 마샬링

    ```java 
    public class MecPopupVo extends BaseVO {

	private static final long serialVersionUID = 2224577561626809324L;

    }
    ```

    serialVersionUID(객체의 고유번호) 즉, MecPopupVo객체의 고유번호를 뜻합니다. 

    자바끼리 통신할때에는 상관 없지만, 만약 RESPONSE BODY등 외부로 나가게 되면 필요한 부분입니다.