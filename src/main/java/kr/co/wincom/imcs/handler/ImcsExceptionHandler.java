package kr.co.wincom.imcs.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ErrorVO;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ImcsExceptionHandler
{
	/**
	 * <pre>
	 * ImcsException이 넘어올 경우 공통 리턴포멧으로 셋팅하여 보내주도록 하기 위해 
	 * </pre>
	 * @author medialog
	 * @date 2015. 8. 25.
	 * @method ImcsExceptionHandler
	 * @return String
	 */
	@ResponseBody
	@ExceptionHandler(ImcsException.class)
    public String ImcsExceptionHandle(ImcsException e) {
		ErrorVO rtnVo = new ErrorVO();
		rtnVo.setFlag(e.getFlag());
		rtnVo.setErrCode(e.getErrorCode());
		rtnVo.setErrMsg(e.getMessage());
		
         return rtnVo.toString(); 
    } 
	
	/**
	 * <pre>
	 *  ImcsException으로 넘어오지 못할 경우 공통 리턴포멧으로 맞춰주기 위해 ImcsException을 제외한 모든 Exception을 잡아서
	 *  공통포멧으로 리턴하기 위한 Handler
	 * </pre>
	 * @author medialog
	 * @date 2015. 8. 25.
	 * @method HandleException
	 * @return String
	 */
	@ResponseBody
	@ExceptionHandler
	public String HandleException(HttpServletRequest req, HttpServletResponse res, Exception e)
	{
		ImcsException ex = new ImcsException(ImcsConstants.FAIL_CODE, e);
		ErrorVO rtnVo = new ErrorVO();
		rtnVo.setFlag(ex.getFlag());
		rtnVo.setErrCode(ex.getErrorCode());
		rtnVo.setErrMsg(ex.getMessage());
		return rtnVo.toString();
	}
}
