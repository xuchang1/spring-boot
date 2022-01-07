package smoketest.tomcat.config;

/**
 * @author changxu13
 * @date 2022/1/7 16:57
 */
public class RespData<T> {

	private boolean flag;

	private String code;

	private String msg;

	private T data;

	public RespData() {
	}

	public RespData(String code, String msg) {
		this(false, code, msg, null);
	}

	public RespData(T data) {
		this(true, "200", "", data);
	}

	public RespData(boolean flag, String code, String msg, T data) {
		this.flag = flag;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
