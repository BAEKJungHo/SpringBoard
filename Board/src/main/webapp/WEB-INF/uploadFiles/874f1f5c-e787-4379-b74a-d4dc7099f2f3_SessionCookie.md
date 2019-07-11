## Connectionless Protocol 

    세션과 쿠키는 `Connectionless Protocol(비연결지향 프로토콜)` 입니다.

    > Connectionless Protocol
    >
    > 웹 서비스는 HTTP 프로토콜을 기반으로 하는데, HTTP 프로토콜은 클라이언트와 서버의 관계를 유지하지 않습니다. 클라이언트가 서버쪽에 하나의 REQUEST를 보내면 서버는 그에 대한 RESPONSE를 하고 서버 연결을 해제합니다. 비연결지향인 이유는 클라이언트가 여러개 이므로 연결이 지속된다면, 서버에 과부하가 일어나기 때문에, 서버의 효율성을 위해 비연결지향 프로토콜입니다.

    Connectionless Protocol의 장점은 서버의 부하를 줄일 수는 있으나, 클라이언트의 요청 시마다 서버와 매번 새로운 연결이 생성되기 때문에 일반적인 로그인 상태유지, 장바구니 등의 기능을 구현하기 어렵습니다. 이러한 단점을 보완하기 위해서 `세션과 쿠키`를 사용하는 것입니다.

    세션과 쿠키는 클라이언트와 서버의 연결 상태를 유지해주는 방법으로, __세션은 서버에서 연결 정보를 관리하는 반면 쿠키는 클라이언트에서 연결 정보를 관리하는데 차이가 있습니다.__

## HttpServletRequest를 이용한 세션 사용

    HttpServletRequest를 사용하는 방법은 컨트롤러의 메소드 파라미터로 HttpServletRequest를 받으면 됩니다. 

    ```java 
    @Controller
    public class UsersController {
	@Autowired
	private UsersService usersService;
	
	// 로그인 페이지로 이동
	@RequestMapping(value="/login", method = RequestMethod.GET) 
	public String login(UsersDTO usersDTO) {
		return "login";
	}
	
	// 로그인 처리
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UsersDTO usersDTO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("usersDTO", usersService.select(usersDTO.getId()));
		return "boardList";
	}
    }
    ```

## HttpSession을 이용한 세션 사용

    HttpServletRequest와 차이점이 거의 없으며, 단지 세션 객체를 얻는 방법에서 차이가 있습니다.
    파라미터로 HttpServletRequest을 받은 후 getSession()으로 얻습니다. HttpSession은 파라미터로 받아서 사용합니다.

    아래 코드를 보면 아시겠지만 HttpServletRequest보다 HttpSession이 더 편합니다.

    ```java 
    // 로그인 처리 : HttpSession을 사용 
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UsersDTO usersDTO, HttpSession session) {
		session.setAttribute("usersDTO", usersService.select(usersDTO.getId()));
		return "boardList";
	} 
    ``` 

## 세션을 삭제하는 방법 

    로그인을 했으면 로그아웃이 있듯이, 로그아웃할 때 세션을 삭제하는 방법을 배워 보겠습니다.

    세션삭제를 하기 위해서는 `session.invalidate()`만 넣어주면 됩니다. invalidate()는 HttpSession과 HttpServletRequest 둘다 가지고 있는 내장 메소드 입니다.

## 세션 주요 메소드 

    - getId() : 세션 ID를 반환 
    - setAttribute() : 세션 객체의 속성을 저장
    - getAttribute() : 세션 객체에 저장된 속성을 반환
    - removeAttribute() : 세션 객체에 저장된 속성을 제거
    - setMaxInactiveInterval() : 세션 객체의 유지시간을 설정
    - getMaxInactiveInterval() : 세션 객체의 유지시간을 반환
    - invalidate() : 세션 객체의 모든 정보를 삭제

## 쿠키(Cookie) 

    