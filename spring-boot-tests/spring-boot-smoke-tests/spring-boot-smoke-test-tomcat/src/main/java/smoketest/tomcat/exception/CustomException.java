package smoketest.tomcat.exception;

/**
 * @author changxu13
 * @date 2022/1/7 16:55
 */
public class CustomException extends RuntimeException{

	private String errMsg;

	private String errCode;

	public CustomException (String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}
