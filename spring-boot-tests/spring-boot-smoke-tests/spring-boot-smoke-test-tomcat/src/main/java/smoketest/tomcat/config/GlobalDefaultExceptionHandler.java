package smoketest.tomcat.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import smoketest.tomcat.exception.CustomException;

/**
 * @author changxu13
 * @date 2022/1/7 16:49
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	@ExceptionHandler(CustomException.class)
	@ResponseBody
	public RespData<String> jsonCustomException(CustomException e) {
		RespData<String> respData = new RespData<>();
		respData.setCode(e.getErrCode());
		respData.setMsg(e.getErrMsg());
		respData.setFlag(false);
		return respData;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public RespData<String> jsonException(Exception e) {
		RespData<String> respData = new RespData<>();
		respData.setCode("500");
		respData.setMsg(e.getMessage());
		respData.setFlag(false);
		return respData;
	}
}
