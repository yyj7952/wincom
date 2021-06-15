package kr.co.wincom.imcs.handler;

public class ImcsException extends RuntimeException {

	private static final long serialVersionUID = 2533173390629509755L;

	private String flag;
	private String message;
	private String errorCode;
	// Service Layer 에서 Controller Layer 로 TLO Log 정보를 전달하기 위한 VO
	private Object list = null;
	
	private Exception e;

	public ImcsException() {}
	
	public ImcsException(String flag, Exception e) {
		this.e = e;
		this.flag = flag;
		processException();
	}
	
	public ImcsException(String flag, String message) {
		this.flag = flag;
		this.message = message;
	}
	
	public ImcsException(String flag, String message, String errorCode) {
		this.flag = flag;
		this.message = message;
		this.errorCode = errorCode;
	}

	public ImcsException(String flag, String message, String errorCode, Object list) {
		this.flag = flag;
		this.message = message;
		this.errorCode = errorCode;
		this.list = list;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public Exception getException() {
		return e;
	}
	
	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}
	
	public void processException(){
		if(e instanceof ImcsException){
			ImcsException se	= (ImcsException)e;
			errorCode			= se.getErrorCode();
			message				= se.getMessage();
		}
	}

	
}
