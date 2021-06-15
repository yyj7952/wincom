package kr.co.wincom.imcs.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ErrorVO;

/**
 * 4XX~5XX HttpStatus 에러핸들러
 * @author medialog
 * @date 2015. 7. 16.
 * kr.co.wincom.imcs.handler.HttpErrorHandler
 */
public class HttpErrorHandler extends HttpServlet {
	
	private static final long serialVersionUID = 4724237233123539358L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try{
			out = response.getWriter();
			
			ImcsException ex = new ImcsException();
			
			ErrorVO rtnVo = new ErrorVO();
			rtnVo.setFlag(ex.getFlag());
			rtnVo.setErrCode(ex.getErrorCode());
			rtnVo.setErrMsg(ex.getMessage());
	
			// Set response content type
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Accept-Charset", "EUC-KR,UTF-8");
			response.setHeader("Content-Length", Integer.toString(rtnVo.toString().getBytes("UTF-8").length));
			
			
			out.print(rtnVo.toString());
		} catch (IOException e) {
			try {
				if(out == null) out = response.getWriter();
				
				ImcsException ex = new ImcsException(ImcsConstants.FAIL_CODE, e);
				ErrorVO rtnVo = new ErrorVO();
				rtnVo.setFlag(ex.getFlag());
				rtnVo.setErrCode(ex.getErrorCode());
				rtnVo.setErrMsg(ex.getMessage());
				
				out.print(rtnVo.toString());
			} catch (IOException e1) {}
		} finally {
			if(out != null) out.close();
		}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
}
