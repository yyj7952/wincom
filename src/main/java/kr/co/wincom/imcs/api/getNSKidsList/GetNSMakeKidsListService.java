package kr.co.wincom.imcs.api.getNSKidsList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSMakeKidsListService
{
	private final static String API_LOG_NAME = "000_w/getNSKidsList";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsList");
	private Log imcsComLogger = LogFactory.getLog("API_Common_getNSKidsEStudy");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSKidsListDao getNSKidsListDao;
	
	private static String MYOS = System.getProperty("os.name");
	
	@Async
	public void getNSKidsList(GetNSKidsListRequestVO paramVO,
			String localFilePath, String nasFilePath, String lockNasFilePath) throws Exception
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		IMCSLog imcsMkLog	= new IMCSLog(imcsComLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSMakeKidsList service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		long tp1 = 0, tp2 = 0;
		long tp_start = System.currentTimeMillis();
		String msg = "";
		
		msg = " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[start] rcv[" + paramVO.getParam()+ "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		List<HashMap<String, String>> list = null;
		HashMap<String, String> hmPoster = null;
		
		String contents_id_tmp = "";
		
		StringBuilder sb = new StringBuilder();
		
		try
		{
			tp1 = System.currentTimeMillis();
			
			try {
				list = this.getNSKidsListDao.listKidsContentsCache(paramVO);
			} catch(Exception ex) {
				msg = "컨텐츠 정보 조회 error!!!";
				imcsLog.serviceLog(msg, methodName, methodLine);
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("컨텐츠 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(list != null && list.size() > 0)
			{
				for(int i = 0; i < list.size(); i++)
				{
					HashMap<String, String> hm = list.get(i);
					String c_price_desc_tmp = "";
					
					if(hm.get("c_result_type").equals("ALB"))
					{
						// 중복되는 데이터는 첫번째 것만 가져온다.
						if(contents_id_tmp.equals(hm.get("c_contents_id")) == false)
						{
							hm.put("c_is_51_ch", "N");
							hm.put("c_img_url", ""); 			hm.put("c_price_desc", ""); 		hm.put("c_img_file_name_v", "");
							hm.put("c_img_file_name_h", ""); 	hm.put("c_type", ""); 				hm.put("c_is_hot", "");
							hm.put("c_watcha_point", ""); 		hm.put("c_cine21_point", ""); 		hm.put("c_pps_yn", "");
							hm.put("c_animation_file", ""); 	hm.put("c_ppm_yn", ""); 			hm.put("c_ppm_prod_id", "");
							hm.put("c_cat_img_type", ""); 		hm.put("c_order_gb", ""); 			hm.put("c_watch_date", "");
							hm.put("c_cat_sub_name", ""); 		hm.put("c_category_flag", ""); 		hm.put("c_cat_level_month", "");
							hm.put("cat_image_url", ""); 		hm.put("c_recommend_id", ""); 		hm.put("c_last_watch_yn", "");
							hm.put("c_image_file", "");
							
							// 일단 SCREEN_TYPE 은 N 으로 설정
							hm.put("c_screen_type", "N");
							
							if(hm.get("c_51ch").equals("DOLBY 5.1"))
								hm.put("c_is_51_ch", "Y");
							
							if(hm.get("c_product_type").equals("0"))
							{
								if(hm.get("c_album_type").equals("HD") || hm.get("c_album_type").equals("SH"))
									c_price_desc_tmp = "HD ";
								else if(hm.get("c_album_type").equals("3D"))
									c_price_desc_tmp = "3D ";
								else if(hm.get("c_album_type").equals("SD"))
									c_price_desc_tmp = "SD ";
								
								if(hm.get("c_album_type").equals("PR") == false)
									c_price_desc_tmp = String.format("%s무료", c_price_desc_tmp);
								
								hm.put("c_price_desc", c_price_desc_tmp);
							}
							else
							{
								if(hm.get("c_album_type").equals("HD") || hm.get("c_album_type").equals("SH"))
									c_price_desc_tmp = "HD ";
								else if(hm.get("c_album_type").equals("PR"))
									c_price_desc_tmp = "예고편 ";
								else if(hm.get("c_album_type").equals("3D"))
									c_price_desc_tmp = "3D ";
								else if(hm.get("c_album_type").equals("SD"))
									c_price_desc_tmp = "SD ";
								
								c_price_desc_tmp = String.format("%s%s원", c_price_desc_tmp, hm.get("c_price"));
								
								hm.put("c_price_desc", c_price_desc_tmp);
							}
							
							try {
								hmPoster = this.getNSKidsListDao.getContsPoster(hm.get("c_contents_id"));
							} catch(Exception ex) {
								msg = "포스터 정보 조회 error!!!";
								imcsLog.serviceLog(msg, methodName, methodLine);
							}
							
							if(hmPoster != null && hmPoster.size() > 0)
							{
								hm.put("c_img_file_name_v", hmPoster.get("c_img_file_name_v"));
								hm.put("c_img_file_name_h", hmPoster.get("c_img_file_name_h"));
								
								hm.put("c_image_file", hmPoster.get("c_img_file_name_v"));
							}
							
							// NSC (c_img_url, c_order_gb, c_watch_date, c_last_watch_yn => GetNSKidsListService 에서 처리)
							sb.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
									hm.get("c_result_type"), 		hm.get("c_contents_id"), 			hm.get("c_contents_name"),
									hm.get("c_type"), 				hm.get("c_cat_id"),					hm.get("c_cat_sub_name"),
									hm.get("c_category_flag"),		hm.get("c_cat_level_month"), 		hm.get("c_img_url"),
									hm.get("c_image_file"),			hm.get("cat_image_url"), 			hm.get("c_animation_file"),
									hm.get("c_recommend_id"),		hm.get("c_service_gb"),				hm.get("c_kids_grade"),
									hm.get("c_runtime"),			hm.get("c_is_51_ch"),				hm.get("c_is_caption"),
									hm.get("c_is_hd"), 				hm.get("c_3d_yn"),					hm.get("c_watcha_point"),
									hm.get("c_cine21_point"),		hm.get("c_order_gb"),				hm.get("c_watch_date"),
									hm.get("c_last_watch_yn"), 		hm.get("c_screen_type")
							));
						}
						
						contents_id_tmp = hm.get("c_contents_id");
					}
				}
			}
			
			// 빈 파일 생성
			Path nasPath = Paths.get(nasFilePath);
			Files.createFile(nasPath);
			
			// 파일 저장
			FileUtil.fileWrite(nasFilePath, sb.toString(), false);
			
			// 파일 퍼미션(https://infotake.tistory.com/91)            
			if(MYOS == null || MYOS.toLowerCase().indexOf("win") == -1)
			{
				// 아래 Files.setPosixFilePermissions 소스는 윈도우에서 실행시 오류 발생함.
				try {
					Set<PosixFilePermission> posixPermissions = PosixFilePermissions.fromString("rw-rw-rw-");
					Files.setPosixFilePermissions(nasPath, posixPermissions);
				} catch(Exception ex) {
					imcsLog.serviceLog(nasFilePath + " Files.setPosixFilePermissions error!!!", methodName, methodLine);
					
					ex.printStackTrace();
				}
			}
            
			// 로컬로 파일 복사
			// (REPLACE_EXISTING : 기존에 파일이 있으면 덮어씀, COPY_ATTRIBUTES : 파일의 모든 속성 복사)
			Files.copy(nasPath, Paths.get(localFilePath),
					StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			
			// lock 파일 삭제
			for(int i = 0; i < 3; i++)
			{
				try {
					boolean isDel = Files.deleteIfExists(Paths.get(lockNasFilePath));
					
					if(isDel == true)
						break;
				} catch(Exception e) {
					imcsLog.serviceLog(lockNasFilePath + " Lock File Delete Exception!!!!!", methodName, methodLine);
				}
			}
		}
		catch(ImcsException ce)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			throw ce;
		}
		catch(Exception e)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			throw e;
		}
		finally
		{
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			long tp_end = System.currentTimeMillis();
			imcsMkLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
		}
	}
}































