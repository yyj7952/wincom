package kr.co.wincom.imcs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Map;

import kr.co.wincom.imcs.common.ImcsConstants;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"test-servlet-context.xml", "test-root-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 메소드명으로 실행 순서 지정
public class GetNSCHRatingsTest
{
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;
	
	@Before
    public void init() throws Exception
    {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
   @Test
   public void test_getNSCHRatings() throws Exception
   {
	   test_run("getNSCHRatingsTestCase_all.xlsx", "log/getNSCHRatingsTestCase_all.log");
   }
   public void test_run(String preadFilePath, String  pwriteFilePath) throws Exception
   {
	   test_run(preadFilePath, pwriteFilePath, 1); 
	}
   //iRowNo : 엑셀파일내 번호(1부터 시작)
   public void test_run(String preadFilePath, String  pwriteFilePath, int iRowNo ) throws Exception
   {
	   File file = new File(".");
	   String baseDir = file.getCanonicalPath() + "/src/test/testcase/";
	   String readFilePath = baseDir + preadFilePath;
	   String writeFilePath = baseDir + pwriteFilePath;
	   
	   FileInputStream fis=new FileInputStream(readFilePath);
	   XSSFWorkbook workbook=new XSSFWorkbook(fis);
	   XSSFSheet sheet=workbook.getSheetAt(0);
	   int rows=sheet.getPhysicalNumberOfRows();
	   String[] keyArr = null;
	   String caseTitle = "";
	   String caseResult = "";
	   StringBuffer logBuffer = new StringBuffer();
	   
	   int cells = 0;
	   for( int rowindex = 0 ; rowindex < rows; rowindex++){
	       XSSFRow row = sheet.getRow(rowindex);
	       if(row==null) continue;
	       if( rowindex == 0 ){
	    	   cells = row.getLastCellNum();
	    	   keyArr = new String[cells];
	    	   for( int columnindex = 0; columnindex < cells; columnindex++ ){
	    		   String value = rowCellValue(row, columnindex);
	               keyArr[columnindex] = value;
	           }
	       }else{
	    	   if(iRowNo > 1) {
	    		   if(rowindex != (iRowNo - 1) ) continue;
	    	   } //= row선택
	    	   
	    	   String param = "";
	    	   
	    	   MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/servlets/CommSvl");
	    	   request.param("CMD", "getNSCHRatings");
	           for( int columnindex = 0; columnindex < cells; columnindex++ ){
	        	   String value = rowCellValue(row, columnindex);
	               if( columnindex == 0 ){
	            	   caseTitle = value;
	               }else if( columnindex == cells-2 ){
	            	   caseResult = value;
	               }else if( columnindex != cells-1 ){
	            	   //request.param(keyArr[columnindex], value);  
	            	   param += keyArr[columnindex]+"="+value+"|";
	               }
	           }
	           request.param("PARAM", param);
	           
	            request.characterEncoding("UTF-8");
				request.accept(MediaType.TEXT_HTML);
			   
				ResultActions result = this.mockMvc.perform(request);
				System.out.println("=========== caseTitle : " + caseTitle + " ==========");
				result.andDo(MockMvcResultHandlers.print());
				result.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));
				MvcResult mvcResult = result.andReturn();
				
//				assertThat(mvcResult.getResponse().getContentAsString(), startsWith(caseResult));
				
				StringBuffer paramBuffer = new StringBuffer();
				StringBuffer uriBuffer = new StringBuffer();
				Map<String, String[]> paramMap = mvcResult.getRequest().getParameterMap();
				for(String key : paramMap.keySet()){
					String[] value = paramMap.get(key);
					for( int i = 0; i < value.length; i++ ) {
						paramBuffer.append(key + "=[" + value[i] + "], ");
						uriBuffer.append(key + "=" + value[i] + "&");
					}
				}
				
				//String resultHeader = mvcResult.getResponse().getContentAsString().split(ImcsConstants.ROWSEP)[0];
				String responseBody = mvcResult.getResponse().getContentAsString().replace(ImcsConstants.ARRSEP, " ").replace(ImcsConstants.ROWSEP, " ");
				if(responseBody == null || "".equals(responseBody)) responseBody = " ";
				XSSFCell xCell = row.getCell(cells-1);
				xCell.setCellValue(responseBody);
				
				// 마지막 행에 비교 결과 입력
				XSSFCell resultCell = row.createCell(cells);
				//XSSFCell resultCell = row.getCell(cells);
				String validYn = responseBody.equals(caseResult) ? "Y" : "N";
				resultCell.setCellValue(validYn);
				
				logBuffer.append("=========== getNSCHRatings " + caseTitle + " start!! ==========" + "\r\n");
				logBuffer.append("testCase >> " + caseTitle + "\r\n");
				logBuffer.append("requestParam >> " + paramBuffer.toString().substring(0, paramBuffer.toString().lastIndexOf(",")) + "\r\n");
				logBuffer.append("requestUri >> " + "http://127.0.0.1:8082" + mvcResult.getRequest().getRequestURI() + "?" + uriBuffer.toString().substring(0, uriBuffer.toString().lastIndexOf("&")) + "\r\n");
				logBuffer.append("responseStatus >> " + mvcResult.getResponse().getStatus() + "\r\n");
				logBuffer.append("responseBody >> " + responseBody + "\r\n");
				//logBuffer.append("resultHeader >> " + resultHeader + "\r\n");
				logBuffer.append("compareValue >> " + caseResult + "\r\n");
				logBuffer.append("validYn >> " + validYn + "\r\n");
				logBuffer.append("=========== getNSCHRatings " + caseTitle + " end!! ==========" + "\r\n\r\n");

	       }
       }
	   
	   System.out.println(logBuffer.toString());
	   
	   FileOutputStream fs = new FileOutputStream(readFilePath);
	   workbook.write(fs);
	   fs.close();
		
       try{
       	file = new File(writeFilePath);
       	if( !file.isFile() ){
       		file.createNewFile();
       	}
           BufferedWriter fw = new BufferedWriter(new FileWriter(file, false));
           fw.write(logBuffer.toString());
           fw.flush();
           fw.close();
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public String rowCellValue(XSSFRow row, int columnindex){
	   XSSFCell cell = row.getCell(columnindex);
       String value="";
       if(cell==null){
           value="";
       }else{
           switch (cell.getCellType()){
           case XSSFCell.CELL_TYPE_FORMULA:
               value = cell.getCellFormula();
               break;
           case XSSFCell.CELL_TYPE_NUMERIC:
               value = cell.getNumericCellValue()+"";
               break;
           case XSSFCell.CELL_TYPE_STRING:
               value = cell.getStringCellValue()+"";
               break;
           case XSSFCell.CELL_TYPE_BLANK:
               value = cell.getBooleanCellValue()+"";
               break;
           case XSSFCell.CELL_TYPE_ERROR:
               value = cell.getErrorCellValue()+"";
               break;
           }
       }
       
       return value;
   }
}
