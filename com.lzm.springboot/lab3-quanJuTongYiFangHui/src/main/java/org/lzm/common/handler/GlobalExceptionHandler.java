package org.lzm.common.handler;



import org.lzm.common.CommonResult;
import org.lzm.common.constants.ServiceExceptionEnum;
import org.lzm.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理异常的全局统一返回的处理器
 */
@ControllerAdvice(basePackages = "org.lzm.controller")
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理 ServiceException 异常
     * 拦截处理 ServiceException 业务异常，直接使用该异常的 code + message 属性，构建出 CommonResult 对象返回。
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult serviceExceptionHandler(ServiceException e) {
        logger.debug("[serviceExceptionHandler]", e);
        // 包装 CommonResult 结果
        return CommonResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理 MissingServletRequestParameterException 异常
     *拦截处理 MissingServletRequestParameterException 请求参数异常，
     * 构建出错误码为 ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR 的 CommonResult 对象返回
     * SpringMVC 参数不正确
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException ex) {
        logger.debug("[missingServletRequestParameterExceptionHandler]", ex);
        // 包装 CommonResult 结果
        return CommonResult.error(ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getCode(),
                ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getMessage());
    }
    /**
     * 处理其它 Exception 异常
     */
    @ResponseBody
    //拦截处理 Exception 异常，构建出错误码为 ServiceExceptionEnum.SYS_ERROR 的
    // CommonResult 对象返回。这是一个兜底的异常处理，避免有一些其它异常，
    // 我们没有在 GlobalExceptionHandler 中，提供自定义的处理方式
    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(HttpServletRequest req, Exception e) {
        // 记录异常日志
        logger.error("[exceptionHandler]", e);
        // 返回 ERROR CommonResult
        return CommonResult.error(ServiceExceptionEnum.SYS_ERROR.getCode(),
                ServiceExceptionEnum.SYS_ERROR.getMessage());
    }
}
