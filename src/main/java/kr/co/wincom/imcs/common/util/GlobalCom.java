package kr.co.wincom.imcs.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 공통 유틸 클래스
 */
public class GlobalCom {
	
	// 통합DB 함수 계정명. 개발 : VODUSER, 운영 : MCUSTUSER
	// CommonService.init 메소드에서 값을 대입함.
	public static String dbMcustUser = "MCUSTUSER";
	
	// 개발 : NOSQL, 운영 : MCONUSER
	public static String dbMconUser = "MCONUSER";
	
	// MyBatis XML 파일에서 사용. 개발 : VODUSER, 운영 : MCUSTUSER
	public static String getDBMcustUser()
	{
		return dbMcustUser;
	}
	
	// MyBatis XML 파일에서 사용. 개발 : NOSQL, 운영 : MCONUSER
	public static String getDBMconUser()
	{
		return dbMconUser;
	}
	
	public static String checkNullStr(String str) {
		if ((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return "";
		else
			return str.trim();
	}

	
	
	/**
	 * 현재 서버 인덱스를 가져온다
	 * @return HashMap key(index:인덱스, total: WAS 총 개수)
	 */
	/*public static Map<String, Integer> getServerIndex() {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

		String[] ipList = GlobalCom.getProperties("curation.server.ipList").split("\\|");
		String[] portList = GlobalCom.getProperties("curation.server.portList").split("\\|");

		int count = ipList.length;

		for (int i = 0; i < count; i++) {
			StringBuilder key = new StringBuilder();
			key.append(ipList[i]).append(":").append(portList[i]);
			Map<String, Integer> values = new HashMap<String, Integer>();

			values.put("index", i);
			values.put("total", count);

			map.put(key.toString(), values);
		}

		String ip	= getSystemProperty("JBOSS_IP");
		String port	= getSystemProperty("JBOSS_PORT");

		StringBuilder address = new StringBuilder();
		address.append(ip.isEmpty() ? ipList[0] : ip).append(":").append(port.isEmpty() ? portList[0] : port);

		return map.get(address.toString());
	}
	
	*/

	
	/**
	 * 프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
	 * @param filepath	프로퍼티 파일 경로
	 * @param key		입력받은 프로퍼티 파일 경로에 있는 프로퍼티 파일에 있는 프로퍼티 키
	 * @return 프로퍼티 값
	 */
	public static String getProperties(String filepath, String key) {
		String result = "";
		Properties props = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			props = new Properties();
			fis = new FileInputStream(filepath);
			bis = new BufferedInputStream(fis);
			props.load(bis);
			result = props.getProperty(key).trim();
		} catch (Exception e) {
		
		} finally {
			try {
				if(fis != null) fis.close();
				if(bis != null) bis.close();
			} catch (Exception e) {

			}
		}

		return result;
	}
	
	
	
	public static void setProperties(String filepath, Map<String, String> map, String comment) throws Exception {
		Properties props = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			props = new Properties();
			fos = new FileOutputStream(filepath);
			bos = new BufferedOutputStream(fos);
			
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				String value = map.get(key);
				props.setProperty(key.trim(), value.trim());
			}
			
			props.store(bos, comment);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if(fos != null) fos.close();
				if(bos != null) bos.close();
			} catch (Exception e) {
			}
		}
	}
	
	
	
	public static Properties getPropertyFile(String filepath) {
		Properties props = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		
		try {
			props = new Properties();
			fis = new FileInputStream(filepath);
			bis = new BufferedInputStream(fis);
			props.load(bis);
		} catch (Exception e) {

		} finally {
			try {
				if(fis != null) fis.close();
				if(bis != null) bis.close();
			} catch (Exception e) {

			}
		}

		return props;
	}

	
	
	public static Properties getPropertyFileEncoding(String filepath, String encoding) {
		Properties props = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			props = new Properties();
			fis = new FileInputStream(filepath);
			isr = new InputStreamReader(fis, encoding);
			br = new BufferedReader(isr); 
			props.load(br);
		} catch (Exception e) {

		} finally {
			try {
				if(fis != null) fis.close();
				if(isr != null) isr.close();
				if(br != null) br.close();
			} catch (Exception e) {

			}
		}

		return props;
	}


	
	
	/**
	 * 서버의 단말 API를 호출하는 함수(싱크용)
	 * @param host		서버 ip 주소
	 * @param port		서버 포트번호
	 * @param url		단말 API URL
	 * @param param		단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout	HttpClient timeout 시간
	 * @param retrycnt	호출 실패시 재시도 횟수
	 * @param protocolName	URI 호출 프로토콜명(http, https 등)
	 */
/*	public static void callAPISync(String host, int port, String url, String param, int timeout, int retrycnt, String protocolName) {
		String result = "";
		String retryresult = "";

		try {
			result = callHttpClient(host, port, url, param, timeout, protocolName);
			if (result.length() >= 4) {
				result = result.substring(0, 4);
			}

			if (!(ImcsProperties.getProperty("result.code.authsuccess").equals(result))) {

				for (int i = 0; i < retrycnt; i++) {
					retryresult = GlobalCom.callHttpClient(host, port, url, param, timeout, protocolName);
					if (retryresult.length() >= 4) {
						retryresult = retryresult.substring(0, 4);
					}

					if (ImcsProperties.getProperty("result.code.authsuccess").equals(retryresult)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			for (int i = 0; i < retrycnt; i++) {
				try {
					retryresult = GlobalCom.callHttpClient(host, port, url, param, timeout, protocolName);
				} catch (Exception sube) {

				}

				if (retryresult.length() >= 4) {
					retryresult = retryresult.substring(0, 4);
				}

				if (ImcsProperties.getProperty("result.code.authsuccess").equals(retryresult)) {
					break;
				}
			}
		}
	}
*/


	/**
	 * 각 서버에 있는 단말 API를 호출하여 모든 서버의 캐쉬 메모리의 내용을 sync해주는 작업을 진행한다
	 * 
	 * @param port
	 *            서버 포트번호
	 * @param url
	 *            단말에서 사용하는 API URL
	 * @param param
	 *            단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout
	 *            HttpConnection의 Timeout 설정
	 * @param retrycnt
	 *            호출 실패시 재시도 횟수
	 * @param protocolName
	 *            URI 호출 프로토콜명(http, https 등)
	 * @deprecated           
	 */
	/*public static void syncServerCache(String url, String param, int timeout, int retrycnt, String protocolName) {

		String hostsString = GlobalCom.getProperties("curation.server.ipList");
		String[] hostList = hostsString.split("\\|");
		String portsString = GlobalCom.getProperties("curation.server.portList");
		String[] portList = portsString.split("\\|");
		int idx = -1;
		for (String host : hostList) {
			idx++;

			// 다른 서버의 단말 API를 호출한다
			callAPISync(host, Integer.parseInt(portList[idx]), url, param, timeout, retrycnt, protocolName);
		}
	}
*/
}
