package kr.co.wincom.imcs.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Semaphore;

public class FileUtil {

	/**
	 * 파일 쓰기 
	 * @param fileName	파일명
	 * @param cont		내용
	 * @param append	append 여부
	 * @return	결과 
	 */
	
//	public static int fileWrite(String fileName, String cont, boolean append){
//		
//		int nRtn				= 0;
//    	FileOutputStream fos	= null;
//    	OutputStreamWriter osw	= null;
//    	File f 					= new File(fileName);
//    	
//    	try {
//    		
//    		fos = new FileOutputStream(f, append);
//    		osw = new OutputStreamWriter(fos, "UTF8"); //캐시파일 생성 시, UTF8로 저장하도록 수정 (권형도, 20180710) ==> API에서 한글깨지는 것 보완
//    		osw.write(cont);
//    		nRtn = 1;
//    	} catch ( Exception e) {
//    		nRtn = -1;
//    	} finally {
//    		try {
//    			if(null != osw )	osw.close();
//    			if(null != fos)		fos.close(); 
//    		} catch( IOException ee){
//    			//System.out.println("write ee: " + ee.getMessage());;
//    		}
//    	}
//    	return nRtn;
//    }
	
    public static int fileWrite(String fileName, String cont, boolean append){
    	int nRtn = 0;
    	
    	OutputStreamWriter out = null;
    	File f = new File(fileName);
    	
    	try {
    		//out = new OutputStreamWriter(new FileOutputStream(f, append));
    		out = new OutputStreamWriter(new FileOutputStream(f, append),"UTF8"); //캐시파일 생성 시, UTF8로 저장하도록 수정 (권형도, 20180710) ==> API에서 한글깨지는 것 보완
    		out.write(cont);
    		out.close();
    		nRtn = 1;
    	} catch ( Exception e) {
    		nRtn = -1;
    	} finally {
    		try {
    			if ( out != null ) 	out.close();
    		} catch(Exception ee){}
    	}
    	return nRtn;
    }

    
    /**
     * 파일 읽기
     * @param fullPath	경로
     * @return	내용
     */
//	public static String fileRead(String fullPath, String EncType){
//
//		StringBuffer fileString = new StringBuffer();
//		FileInputStream fis		= null;
//		InputStreamReader isr	= null;
//		BufferedReader br		= null;
//		
//		try {
//			
//			fis	= new FileInputStream(fullPath);
//			isr	= new InputStreamReader(fis, EncType);
//			br	= new BufferedReader(isr);
//
//			String readStr = null;
//			while((readStr = br.readLine()) != null) {
//				fileString.append(readStr);
//			}
//		} catch (Exception e) {
//		} finally {
//			try {
//				if(null != br)	br.close();
//				if(null != isr) isr.close();
//				if(null != fis) fis.close();
//			} catch(IOException ee) {
//				//System.out.println("read ee: " + ee.getMessage());;
//			}
//		}
//		
//		return fileString.toString();
//	}
	
	public static String fileRead(String fullPath, String EncType){
		StringBuffer fileString = new StringBuffer();
		try {
			FileInputStream fin = new FileInputStream(fullPath);
			BufferedReader in = new BufferedReader(new InputStreamReader(fin, EncType));
			String readStr = null;

			while((readStr = in.readLine()) != null) {
				fileString.append(readStr);
			}
			
			fin.close();
		} catch (Exception e) {}
		
		return fileString.toString();
	}
	
	
	/**
	 * 파일 LOCK 모듈
	 * @param szLockFileName
	 * @return
	 */
	
//	private FileUtil() {
//		    semaphoreS = 1;
//		    waitProcess = 0;
//	}
//	static private Semaphore instance;
//	static private int semaphoreS;
//	static public int waitProcess;
	public static synchronized  boolean lock(String LOCKFILE, IMCSLog imcsLog){
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		String msg;
		
		File lock = new File(LOCKFILE);
		
		if(lock.exists()){
			//System.out.println("################lock:파일존재: true 리턴#############");
			return true;
		}else{
			try {
				lock.createNewFile();

				msg = " queryLock Success, query execution itself ";
				imcsLog.serviceLog(msg, methodName, methodLine);
			} catch (Exception e) {
			}
			//System.out.println("################lock:파일없음->생성: false 리턴#############: " + LOCKFILE);
			return false;
		}
	}

	/**
	 * 파일 UNLOCK 모듈
	 * @param szLockFileName
	 * @return
	 */
	public static synchronized void unlock(String LOCKFILE, IMCSLog imcsLog){
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		String msg;
		
		File lock = new File(LOCKFILE);
		
		if(lock.delete()){
			msg = " queryUnLock Success, unlink [" + LOCKFILE + "] ";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}else{
			msg = " lockfile ["+LOCKFILE+"] unlink Failed ";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
	}



}
