package kr.co.wincom.imcs.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * URL별로 캐릭터셋(UTF-8, EUC-KR)을 다르게 사용해야 하기 때문에 CharacterEncodingFilte를 상속받아서 새로 만들었음.
 * 톰캣 server.xml 에서는 useBodyEncodingForURI="true" 만 설정해야 함.
 *
 */
public class ImcsCharacterEncodingFilter extends CharacterEncodingFilter
{
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		String reqURI = request.getRequestURI();
		String contentType = request.getContentType();
		
		if("/servlets/CommSvl".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("UTF-8");
		}
		else if("/servlets/CommSvl2".equalsIgnoreCase(reqURI)) //권형도 추가
		{
			request.setCharacterEncoding("UTF-8");
		}
		else if("/servlets/CommSvl_I20".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("KSC5601");
		}
		else if("/servlets/CommSvl_MMI".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("KSC5601");
		}
		else if("/servlets/CommSvl_SSL".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("UTF-8");
		}
		else if("/servlets/CommSvl_UTF8".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("KSC5601");
		}
		else if("/servlets/CommSvl_UX".equalsIgnoreCase(reqURI))
		{
			request.setCharacterEncoding("KSC5601");
		}
		else if("/api/".startsWith(reqURI)) // Open API
		{
			if(StringUtils.isBlank(contentType) || contentType.equals("application/x-www-form-urlencoded"))
				contentType = "application/xml";
			
			if(contentType.startsWith("text/html"))
				request.setCharacterEncoding("KSC5601");
			else
				request.setCharacterEncoding("UTF-8");
		}
		
		super.doFilterInternal(request, response, filterChain);
	}

}
