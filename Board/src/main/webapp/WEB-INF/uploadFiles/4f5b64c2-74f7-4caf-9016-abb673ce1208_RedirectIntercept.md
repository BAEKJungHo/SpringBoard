## Interceptor

    redirect를 사용해야 하는 경우가 많은 경우 HandlerInterceptor를 이용할 수 있다.

    HandlerInterceptor 인터페이스에는 preHandle(), postHandle(), afterCompletion()이 있는데 
    preHandle()은 컨트롤러가 작업하기 이전에 동작하는 메소드이며, postHandle()은 컨트롤러가 작업하고 난 후에 동작하는 메소드이며, afterCompletion()은 컨트롤러와 View가 모두 작업이 끝난 후에 동작하는 메소드입니다.