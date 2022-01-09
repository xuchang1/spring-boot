package smoketest.tomcat.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import smoketest.tomcat.exception.CustomException;

/**
 * @author changxu13
 * @date 2022/1/7 16:49
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

//	@ExceptionHandler(CustomException.class)
	@ResponseBody
	public RespData<String> jsonCustomException(CustomException e) {
		RespData<String> respData = new RespData<>();
		respData.setCode(e.getErrCode());
		respData.setMsg(e.getErrMsg());
		respData.setFlag(false);
		return respData;
	}

	@ExceptionHandler(Exception.class)
	// 修改http请求响应状态码（注意不要配置reason，不然会forward到异常码页面）
	@ResponseStatus
	@ResponseBody
	public RespData<String> jsonException(Exception e) {
		RespData<String> respData = new RespData<>();
		respData.setCode("500");
		respData.setMsg(e.getMessage());
		respData.setFlag(false);
		return respData;
	}
}
