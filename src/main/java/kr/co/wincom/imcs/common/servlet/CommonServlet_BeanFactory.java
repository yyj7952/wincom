package kr.co.wincom.imcs.common.servlet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.wincom.imcs.common.SpringApplicationContext;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
@Controller
public class CommonServlet_BeanFactory extends HttpServlet {
	
	@Autowired
	private DefaultListableBeanFactory beanFactory;
	
    String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
    Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
    private String mjdServer = StringUtil.replaceNull(serverManager.getProperty("mjd"),"");
    private String vts_serverUrl = StringUtil.replaceNull(serverManager.getProperty("bypass_tamx_url"),"http://123.140.17.253"); //Bypass VTS 서버접속 URL
    
    String SERVER_CONF2 = ImcsProperties.getProperty("filepath.blockapi");
    Properties serverManager2 = GlobalCom.getPropertyFile(SERVER_CONF2);
    private String blockApis = StringUtil.replaceNull(serverManager2.getProperty("BLOCK_APIS"),"");
	
	String mode = "N";
	
	//@ResponseBody
	//@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.GET)
	@ResponseBody
	public void doGet(
	//public String doGet(
			@RequestParam(value = "CMD", required=false)		String szCmd,
			@RequestParam(value = "PARAM", required=false)		String szParam,
			@RequestParam(value = "STATS", required=false)		String szStat,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		Object obj = ""; 
		
		if(request.getRequestURI().equals("/servlets/CommSvl")){
			//request.setCharacterEncoding("UTF-8");
		    response.setContentType("text/html;charset=KSC5601");
		}else if(request.getRequestURI().equals("/servlets/CommSvl_MMI")){
			//request.setCharacterEncoding("KSC5601");
			response.setContentType("text/html;charset=KSC5601");
		}
		
		PrintWriter out = null;		

		//blockApi 확인
		if(blockApis.indexOf(szCmd) == -1){
			
//			// Regacy 서버 체크 로직
//			CommonServletCmd commonServletCmd = new CommonServletCmd(request, response, mode);
//			szParam = commonServletCmd.getServletCmd();
//			//if("".equals(szParam))	return "";
//			
//			this.mode = commonServletCmd.getMode();
//			
//			if("M".equals(this.mode)){
//				
//				Log imcsLogger		= LogFactory.getLog("API_COMMON");
//				
//				if (mode.equals("M")) {
//					if(szCmd.startsWith("n_") || szCmd.startsWith("t_")){
//					}else{
//						szCmd = "m_" + szCmd;
//					}
//				}
//				/* API�� 15�ڸ� ���� */
//				if(szCmd.length() > 15 ){
//					szCmd = szCmd.substring(0, 15);
//				}
//				
//				try{
//	        		// 세션 인증을 위한 URL 설정
//	                URL url = new URL("http://" + mjdServer + "/servlets/CommSvl?CMD="+ szCmd + "&PARAM=" + szParam);
//	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	                conn.setRequestMethod("POST");
//	                
//	                imcsLogger.info(" [CommSvl] getURL() : " + conn.getURL());	// URL 얻어오기                
//	                
//	                if(conn.getResponseCode() == 200){
//	                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//	                    StringBuffer buffer = new StringBuffer();
//	                    String line = null;
//	                    while ((line = reader.readLine()) != null) {
//	                        buffer.append(line).append("\r\n");
//	                    }
//	                    
//	                    reader.close();
//	                    
//	                    imcsLogger.debug(" [CommSvl] responseMessage =" + buffer.toString());	// 연동 결과 로그
//	                    String resultValue = buffer.toString();
//	            
//	                    obj = resultValue;
//
//	                }else{ // SA서버 Timeout 발생시 인증 통과
//	                	imcsLogger.info(" [CommSvl] 무중단 서버 접속 실패");
//	                }
//	            }catch(SocketTimeoutException e){
//	                imcsLogger.info(" [CommSvl] 무중단 서버 접속 실패");
//	            }
//				
//				
//			}else{
				
				if( ("".equals(szStat) || szStat == null)  && (szParam != null && !"".equals(szParam))){
					if(szParam.indexOf("|STATS=") != -1){
						
						String[] strArr = null;
						
						strArr = szParam.split("STATS=");
						
						if(strArr != null && strArr[1] != null && !"".equals(strArr[1])){
							szStat = strArr[1];
							szParam = strArr[0];
						}						
					}
				}
				
				boolean isFindBean = false;
				String strControllerName = String.format("%s%s", szCmd, "Controller");
				String strMethodName = szCmd;
				
				// 2019.12.05 - buyNSContent는 VOD정산 프로세스 개선 수정대상 아니므로 동일 규격
				// (buyNSConts 최초 만들어졌을 때 API명만 바뀜) 의 buyNSConts를 호출하도록 한다.
				if(szCmd.equals("buyNSContent")) {
					strControllerName = "buyNSContsController";
					strMethodName = "buyNSConts";
				} else if(szCmd.equals("getNSSimilarLis")) { // 비슷한 영화(왓챠) 정보 조회
					strControllerName = "getNSSimilarListController";
					strMethodName = "getNSSimilarList";
				} else if(szCmd.equals("getNSMakeLists")) { // VOD 전체 카테고리/컨텐츠 조회
					strControllerName = "getNSListsController";
					strMethodName = "getNSLists";
				} else if(szCmd.equals("getNSCatBillInf")) { // 카테고리내 가격정보 표시
					strControllerName = "getNSCatBillInfoController";
					strMethodName = "getNSCatBillInfo";
				} else if(szCmd.equals("getNSMakeNodeLi")) { // CDN 전체 노드 리스트 생성
					strControllerName = "getNSMakeNodeListController";
					strMethodName = "getNSMakeNodeList";
				/** 아래는 캐시고도화 */
//				} else if(szCmd.equals("getNSChList")) { // 채널 리스트 조회
//					strControllerName = "getNSChList2Controller";
//					strMethodName = "getNSChList2";
//				} else if(szCmd.equals("getNSChPGM")) { // 프로그램 리스트 조회
//					strControllerName = "getNSChPGM2Controller";
//					strMethodName = "getNSChPGM2";
//				} else if(szCmd.equals("getNSMnuList")) { // 메뉴 리스트 조회
//					strControllerName = "getNSMnuList2Controller";
//					strMethodName = "getNSMnuList2";
//				} else if(szCmd.equals("getNSMnuListDtl")) { // 카테고리에 편성된 컨텐츠 정보 제공
//					strControllerName = "getNSMnuListDtl2Controller";
//					strMethodName = "getNSMnuListDtl2";
				}
				
				// 메모리에 있는 모든 bean 을 대상으로 찾음
				for(String beanName : this.beanFactory.getBeanDefinitionNames())
				{
					if(beanName.equalsIgnoreCase(strControllerName))
					{
						isFindBean = true;
						
						// 해당 API의 컨트롤러 Bean 을 찾음.
						Object beanObj = SpringApplicationContext.getBean(beanName);
						
						if(szCmd.equals("getNSLists") || szCmd.equals("getNSMakeLists"))
						{
							// 해당 컨트롤러에서 메소드를 찾음
							Method beanMethod = beanObj.getClass().getDeclaredMethod(strMethodName,
									HttpServletRequest.class, HttpServletResponse.class, String.class, String.class, String.class);
							
							// 메소드 실행
							obj = beanMethod.invoke(beanObj, request, response, szCmd, szParam, szStat);
						}
						else if(szCmd.equals("getNSMakePrefIP"))
						{
							// 해당 컨트롤러에서 메소드를 찾음
							Method beanMethod = beanObj.getClass().getDeclaredMethod(strMethodName,
									HttpServletRequest.class, HttpServletResponse.class, String.class);
							
							// 메소드 실행
							obj = beanMethod.invoke(beanObj, request, response, szParam);
						}
						else
						{
							// 해당 컨트롤러에서 메소드를 찾음
							Method beanMethod = beanObj.getClass().getDeclaredMethod(strMethodName,
									HttpServletRequest.class, HttpServletResponse.class, String.class, String.class);
							
							// 메소드 실행
							obj = beanMethod.invoke(beanObj, request, response, szParam, szStat);
						}
						
						break;
					}
				}
//			}
			
			// Controller를 찾지 못한 경우 강제로 NoSuchBeanDefinitionException을 발생 시킴
			if(isFindBean == false)
				SpringApplicationContext.getBean(szCmd + "Controller");
			
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "KSC5601"));
			out.println(obj.toString());
			out.close();
		} else {
			System.out.println("vts bypath: " + szCmd);
			obj = this.getDataFromVts(request);
			
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "KSC5601"));
			out.println(obj.toString());
			out.close();
		}
		//return "";
	}

	

	//@ResponseBody
	//@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.POST)
	public void doPost(
			@RequestParam(value = "CMD", required=false)		String szCmd,
			@RequestParam(value = "PARAM", required=false)		String szParam,
			@RequestParam(value = "STATS", required=false)		String szStat,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Object obj = doGet(szCmd, szParam, szStat, request, response);
		//return obj.toString();
		
		doGet(szCmd, szParam, szStat, request, response);
	}
	
	//@ResponseBody
	//@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.GET)
	public void doGetMMI(
			@RequestParam(value = "CMD", required=false)		String szCmd,
			@RequestParam(value = "PARAM", required=false)		String szParam,
			@RequestParam(value = "STATS", required=false)		String szStat,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Object obj = doGet(szCmd, szParam, szStat, request, response);
		//return obj.toString();
		
		doGet(szCmd, szParam, szStat, request, response);
	}

	

	//@ResponseBody
	@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	public void doPostMMI(
			@RequestParam(value = "CMD", required=false)		String szCmd,
			@RequestParam(value = "PARAM", required=false)		String szParam,
			@RequestParam(value = "STATS", required=false)		String szStat,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Object obj = doGet(szCmd, szParam, szStat, request, response);
		//return obj.toString();
		
		doGet(szCmd, szParam, szStat, request, response);
	}
	
	private Object getDataFromVts (HttpServletRequest request) throws IOException {

		//System.out.println("request url = " + request.getRequestURI());
		//System.out.println("request = " + request.getQueryString());
		//TMAX 직접호출
		String vts_request = "";
		URL objurl;
		HttpURLConnection conn = null;
		String inputLine;
		StringBuffer vts_response = null;
		BufferedReader in = null;

		Object obj = "";
		
		try {
			vts_request = vts_serverUrl + request.getRequestURI() + "?" + request.getQueryString();
			objurl = new URL(vts_request);
			//System.out.println("vts_request = " + vts_request); //전체 호출 경//		//System.out.println("getHeaderNames = " + request.getHeaderNames());
			
			conn = (HttpURLConnection) objurl.openConnection();
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"EUC-KR"));
			
			vts_response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				vts_response.append(inputLine);
			}
			
		} catch (Exception e) {
			System.out.println("#################################:" + e.getMessage());
		} finally {
			in.close();
			conn.disconnect();
		}
        //conn.disconnect();

        //System.out.println(vts_response.toString()); //결과
        
		try {
			obj = vts_response.toString();
		} catch (Exception e) {
			System.out.println("#################################2:" + e.getMessage());
		} finally {
			if (vts_response != null)
				vts_response = null;
		}
 
		
		//프로그램 정보 가져오기
//		RestTemplate restTemplate = new RestTemplate();
//		String result = "";
//		
//		try {
//			System.out.println(URLDecoder.decode(vts_request, java.nio.charset.StandardCharsets.UTF_8.toString()));
//			result  = restTemplate.getForObject(URLDecoder.decode(vts_request, java.nio.charset.StandardCharsets.UTF_8.toString()), String.class);
//		} catch (Exception e) {
//		}
//		obj = result;
		
        return obj;
	}
	
	
}
