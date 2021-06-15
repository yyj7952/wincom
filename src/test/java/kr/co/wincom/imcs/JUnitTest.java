//package kr.co.wincom.imcs;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Map;
//
//import kr.co.wincom.imcs.common.ImcsConstants;
//import kr.co.wincom.imcs.common.util.StringUtil;
//
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = { "test-servlet-context.xml", "test-root-context.xml" })
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//// 메소드명으로 실행 순서 지정
//public class JUnitTest {
//	private MockMvc mockMvc;
//
//	@Autowired
//	private WebApplicationContext wac;
//
//	@Before
//	public void init() throws Exception {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//	}
//	
//	@Test
//	public void test_run() throws Exception {
//		test_run(1);
//	}
//
//	// iRowNo : 엑셀파일내 번호(1부터 시작)
//	public void test_run(int iRowNo) {
//		try {
//			String apiName = this.inputApiNo();
//			
//			if(apiName == null || "".equals(apiName)) {
//				System.out.println("----- getApiName Failed-----");
//				return;
//			}
//			
//			String preadFilePath	= apiName + "TestCase_all.xlsx";
//			String pwriteFilePath	= "log/" + apiName + "Case_all.log";
//			
//			File file = new File(".");
//			String baseDir = file.getCanonicalPath() + "/src/test/testcase/";
//			String readFilePath = baseDir + preadFilePath;
//			String writeFilePath = baseDir + pwriteFilePath;
//	
//			FileInputStream fis = new FileInputStream(readFilePath);
//			XSSFWorkbook workbook = new XSSFWorkbook(fis);
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			int rows = sheet.getPhysicalNumberOfRows();
//			String[] keyArr = null;
//			String caseTitle = "";
//			StringBuffer logBuffer = new StringBuffer();
//			
//			
//	
//			int cells = 0;
//			for (int rowindex = 0; rowindex < rows; rowindex++) {
//				XSSFRow row = sheet.getRow(rowindex);
//				if (row == null)
//					continue;
//				if (rowindex == 0) {
//					cells = row.getLastCellNum();
//					keyArr = new String[cells];
//					for (int columnindex = 0; columnindex < cells; columnindex++) {
//						String value = rowCellValue(row, columnindex);
//						keyArr[columnindex] = value;
//					}
//				} else {
//					if (iRowNo > 1) {
//						if (rowindex != (iRowNo - 1))
//							continue;
//					} // = row선택
//	
//					String param = "";
//	
//					MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/servlets/CommSvl");
//					request.param("CMD", apiName);
//					for (int columnindex = 0; columnindex < cells; columnindex++) {
//						String value = StringUtil.nullToSpace(rowCellValue(row, columnindex));
//						if (columnindex == 0) {
//							caseTitle = value;
//						} else if (columnindex != cells - 1) {
//							param += keyArr[columnindex] + "=" + value + "|";
//						}
//					}
//	
//					
//					request.param("PARAM", param);
//	
//					request.characterEncoding("UTF-8");
//					request.accept(MediaType.TEXT_HTML);
//	
//					ResultActions result = this.mockMvc.perform(request);
//					
//					System.out.println(rowindex + ". caseTitle : " + caseTitle);
//					result.andDo(MockMvcResultHandlers.print());
//					result.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));
//					MvcResult mvcResult = result.andReturn();
//	
//					StringBuffer paramBuffer = new StringBuffer();
//					StringBuffer uriBuffer = new StringBuffer();
//					Map<String, String[]> paramMap = mvcResult.getRequest().getParameterMap();
//					
//					for (String key : paramMap.keySet()) {
//						String[] value = paramMap.get(key);
//						for (int i = 0; i < value.length; i++) {
//							paramBuffer.append(key + "=[" + value[i] + "], ");
//							uriBuffer.append(key + "=" + value[i] + "&");
//						}
//					}
//	
//					String responseBody = mvcResult.getResponse().getContentAsString().replace(ImcsConstants.ARRSEP, " ").replace(ImcsConstants.ROWSEP, " ");
//					if (responseBody == null || "".equals(responseBody))	responseBody = " ";
//					
//					XSSFCell xCell = row.getCell(cells - 1);
//					xCell.setCellValue(responseBody);
//	
//					
//					// VTS 요청 결과
//					String szUrl	= mvcResult.getRequest().getRequestURI();
//					int nDot = uriBuffer.indexOf("RESULT=");
//					String szParam	= uriBuffer.toString().substring(0, uriBuffer.toString().lastIndexOf("&"));
//					if(nDot > 0)
//						szParam	= uriBuffer.substring(0, nDot);
//					
//					String vtsResult = this.getVtsResult(szUrl, szParam);
//					if (vtsResult == null || "".equals(vtsResult))	vtsResult = " ";
//					vtsResult = vtsResult.replace(ImcsConstants.ARRSEP, " ").replace(ImcsConstants.ROWSEP, " ");
//					
//					xCell = row.getCell(cells - 2);
//					xCell.setCellValue(vtsResult);
//					
//					
//					// 마지막 행에 비교 결과 입력
//					XSSFCell resultCell = row.createCell(cells);
//					String validYn = responseBody.equals(vtsResult) ? "Y" : "N";
//					resultCell.setCellValue(validYn);
//	
//					logBuffer.append(rowindex + "." + apiName + " " + caseTitle + " start!!" + "\r\n");
//					logBuffer.append("testCase >> " + caseTitle + "\r\n");
//					logBuffer.append("requestParam >> " + paramBuffer.toString().substring(0, paramBuffer.toString().lastIndexOf(",")) + "\r\n");
//					logBuffer.append("requestUri >> " + "http://127.0.0.1:8082" + szUrl + "?" + szParam + "\r\n");
//					logBuffer.append("responseStatus >> " + mvcResult.getResponse().getStatus() + "\r\n");
//					logBuffer.append("LOCAL REsult >> " + responseBody + "\r\n");
//					logBuffer.append("  VTS Result >> " + vtsResult + "\r\n");
//					logBuffer.append("validYn >> " + validYn + "\r\n");
//					logBuffer.append(rowindex + "." + apiName + " " + caseTitle + " end!!" + "\r\n\r\n");
//				}
//			}
//	
//			System.out.println(logBuffer.toString());
//	
//			FileOutputStream fs = new FileOutputStream(readFilePath);
//			workbook.write(fs);
//			fs.close();
//	
//			try {
//				file = new File(writeFilePath);
//				if (!file.isFile()) {
//					file.createNewFile();
//				}
//				BufferedWriter fw = new BufferedWriter(new FileWriter(file, false));
//				fw.write(logBuffer.toString());
//				fw.flush();
//				fw.close();
//			} catch (Exception e) {
//				System.out.println("Exception1 = " + e.toString());
//			}
//		}catch(Exception e) {
//			System.out.println("Exception2 = " + e.toString());
//		}
//	}
//
//	public String rowCellValue(XSSFRow row, int columnindex) {
//		XSSFCell cell = row.getCell(columnindex);
//		String value = "";
//		if (cell == null) {
//			value = "";
//		} else {
//			switch (cell.getCellType()) {
//			case XSSFCell.CELL_TYPE_FORMULA:
//				value = cell.getCellFormula();
//				break;
//			case XSSFCell.CELL_TYPE_NUMERIC:
//				value = cell.getNumericCellValue() + "";
//				break;
//			case XSSFCell.CELL_TYPE_STRING:
//				value = cell.getStringCellValue() + "";
//				break;
//			case XSSFCell.CELL_TYPE_BLANK:
//				value = cell.getBooleanCellValue() + "";
//				break;
//			case XSSFCell.CELL_TYPE_ERROR:
//				value = cell.getErrorCellValue() + "";
//				break;
//			}
//		}
//
//		return value;
//	}
//	
//	
//	public String getVtsResult(String szUrl, String szParam){
//		String line		= "";
//		String result	= "";
//		
//		BufferedReader in = null;
//		 
//        try {
//            URL obj = new URL("http://123.140.17.253" + szUrl + "?" + szParam); // 호출할 url
//            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
// 
//            // 2초 대기
//            Thread.sleep(2000);
//            
//            con.setRequestMethod("GET");
// 
//            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//            while((line = in.readLine()) != null) { // response를 차례대로 출력
//                result = line;
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
//        }
//		
//		return result;
//	}
//	
//	
//	
//	public String inputApiNo(){
//		InputStreamReader isr= new InputStreamReader(System.in); // 리더 연결
//		BufferedReader in= new BufferedReader(isr); // 버퍼 연결
//		String answer	= "";
//		String apiName	= "";
//		
//		try {
//			System.out.print("API NO : ");
//			answer= in.readLine();
//	
//			if (answer == null || "".equals(answer.trim())) {
//				return "";
//			} else{
//				apiName = this.getApiName(answer);
//			}
//		} catch(Exception e) {
//			System.out.print("getApiName Exception : " + e.toString());
//		}
//		
//		return apiName;
//	}
//
//	
//	
//	public String getApiName(String answer) {
//		String apiName	= "";
//		
//		if(answer.equals("004"))   apiName="getNSProdinfo";
//		if(answer.equals("005"))   apiName="buyNSProduct";
//		if(answer.equals("008"))   apiName="getNSSVODInfo";
//		if(answer.equals("014"))   apiName="getNSProdCpInfo";
//		if(answer.equals("031"))   apiName="getNSVPSI";
//		if(answer.equals("032"))   apiName="getNSVSI";
//		if(answer.equals("033"))   apiName="rmCacheFile";
//		if(answer.equals("034"))   apiName="getNSMultiView";
//		if(answer.equals("035"))   apiName="getNSCHLists";
//		if(answer.equals("108"))   apiName="buyContsCp";
//		if(answer.equals("109"))   apiName="buyNSContent";
//		if(answer.equals("119"))   apiName="getNSPurchased";
//		if(answer.equals("129"))   apiName="getNSReposited";
//		if(answer.equals("175"))   apiName="authorizePView";
//		if(answer.equals("177"))   apiName="authorizeVView";
//		if(answer.equals("178"))   apiName="authorizeNView";
//		if(answer.equals("179"))   apiName="authorizeNSView";
//		if(answer.equals("189"))   apiName="setNSPassedTime";
//		if(answer.equals("258"))   apiName="chkBuyNSPG";
//		if(answer.equals("259"))   apiName="chkBuyNSConts";
//		if(answer.equals("269"))   apiName="getNSMainPromos";
//		if(answer.equals("311"))   apiName="buyNSPresent";
//		if(answer.equals("312"))   apiName="getNSPresent";
//		if(answer.equals("313"))   apiName="rmNSPresent";
//		if(answer.equals("314"))   apiName="useNSPresent";
//		if(answer.equals("339"))   apiName="getNSFavorList";
//		if(answer.equals("347"))   apiName="rmNSCHFavor";
//		if(answer.equals("348"))   apiName="rmNSWatchHis";
//		if(answer.equals("349"))   apiName="rmNSFavorite";
//		if(answer.equals("357"))   apiName="rmNSAllCHFavor";
//		if(answer.equals("358"))   apiName="rmNSAllWatchHis";
//		if(answer.equals("359"))   apiName="rmNSAllFavor";
//		if(answer.equals("369"))   apiName="moveNSFavorIdx";
//		if(answer.equals("377"))   apiName="addNSCHFavor";
//		if(answer.equals("379"))   apiName="addNSFavorite";
//		if(answer.equals("509"))   apiName="getNSMainPage";
//		if(answer.equals("569"))   apiName="getNSGuideVod";
//		if(answer.equals("609"))   apiName="setNSPoint";
//		if(answer.equals("649"))   apiName="getNSCHRank";
//		if(answer.equals("661"))   apiName="addNSAlert";
//		if(answer.equals("662"))   apiName="rmNSAlert";
//		if(answer.equals("663"))   apiName="rmNSAllAlert";
//		if(answer.equals("664"))   apiName="getNSAlertList";
//		if(answer.equals("829"))   apiName="getNSMakeLists";
//		if(answer.equals("899"))   apiName="getNSSuggestVOD";
//		if(answer.equals("939"))   apiName="searchByNSStr";
//		if(answer.equals("949"))   apiName="getNSAlbumList";
//		if(answer.equals("982"))   apiName="getNSCHRatings";
//		if(answer.equals("984"))   apiName="getNSVisitDtl";
//		if(answer.equals("985"))   apiName="getNSSimilarList";
//		if(answer.equals("991"))   apiName="getNSContVod";
//		if(answer.equals("992"))   apiName="getNSPeriod";
//		if(answer.equals("993"))   apiName="getNSVODRank";
//		if(answer.equals("994"))   apiName="getNSLists";
//		if(answer.equals("995"))   apiName="getNSContList";
//		if(answer.equals("996"))   apiName="getNSContDtl";
//		if(answer.equals("997"))   apiName="getNSContStat";
//		if(answer.equals("998"))   apiName="getNSInfoByDCA";
//		if(answer.equals("999"))   apiName="setNSRating";
//		if(answer.equals("NS010"))   apiName="buyNSConts";
//		if(answer.equals("FX010"))   apiName="setFXFavorGenre";
//		if(answer.equals("FX020"))   apiName="getFXFavorGenre";
//		if(answer.equals("FX030"))   apiName="getFXProdInfo";
//		if(answer.equals("FX040"))   apiName="getFXRelation";
//		if(answer.equals("FX060"))   apiName="getFXFavorList";
//		if(answer.equals("FX070"))   apiName="rmFXFavorite";
//		if(answer.equals("FX080"))   apiName="rmFXAllFavor";
//		if(answer.equals("FX090"))   apiName="addFXFavorite";
//		if(answer.equals("FX100"))   apiName="getFXReposited";
//		if(answer.equals("FX110"))   apiName="rmFXWatchHis";
//		if(answer.equals("FX120"))   apiName="rmFXAllWatchHis";
//		if(answer.equals("FX050"))   apiName="getFXContStat";
//		
//		return apiName;
//	}
//	
//	
//}
