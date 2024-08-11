package org.lzm.common.handler;

import org.lzm.common.CommonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 这是拦截全局统一返回的处理器
 */
/*可以使用通过实现 ResponseBodyAdvice 接口，
并添加 @ControllerAdvice 接口，拦截 Controller 的返回结果。注意，
我们这里 @ControllerAdvice 注解，设置了 basePackages 属性，只拦截org.lzm.controller,
毕竟我们呢并不需要我们去替它们做全局统一的返回*/
@ControllerAdvice(basePackages = "org.lzm.controller")
public class GlobalResponseBodyHandler implements ResponseBodyAdvice {
    /*实现 #supports(MethodParameter returnType, Class converterType)
    方法，返回 true 。表示拦截 Controller 所有 API 接口的返回结果*/
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }
    /*实现 #beforeBodyWrite(...) 方法，当返回的结果不是 CommonResult 类型时，则包装成 CommonResult 类型。这里有两点要注意：
        第一点，可能 API 接口的返回结果已经是 CommonResult 类型，就无需做二次包装了。
        第二点，API 接口既然返回结果，被 GlobalResponseBodyHandler 拦截到，约定就是成功返回，
        所以使用 CommonResult#success(T data) 方法，进行包装成成功的 CommonResult 返回*/
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 如果已经是 CommonResult 类型，则直接返回
        if (body instanceof CommonResult) {
            return body;
        }
        // 如果不是，则包装成 CommonResult 类型
        return CommonResult.success(body);
    }
}
