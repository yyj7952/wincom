package kr.co.wincom.curation.common.util;


public class CacheUtil {
	
/*	*//**
	 * Cache file 명 가져오는 함수
	 *  
	 * @param folderPath
	 * @param fileName
	 * @return
	 *//*
	public static String getCacheFilePath(String folderPath, String fileName) {
		
		Map<String, Integer> map = GlobalCom.getServerIndex();
		if (map == null) {
//			throw new CurationException("Can't found server index.");
			throw new ImcsException();
		}
		
		int serverIndex = map.get("index");
		
		return folderPath + File.separator + fileName + "_" + serverIndex;
	}
	
	*//**
	 * Cache file 저장
	 * 
	 * @param fileName
	 * @param obj
	 * @throws Exception
	 *//*
	public static void saveCacheFile(String fileName, Object obj) throws Exception {
		saveCacheFile(GlobalCom.getProperties("curation.cache.folder"), fileName, obj);
	}
	
	*//**
	 * Cache file 저장
	 * 
	 * @param folderPath
	 * @param fileName
	 * @param obj
	 * @throws Exception
	 *//*
	public static void saveCacheFile(String folderPath, String fileName, Object obj) throws Exception {
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		File file = new File(getCacheFilePath(folderPath, fileName));
		
		try {
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(obj);
			oos.flush();
			
		} catch(Exception e) {
			throw e;
		} finally {
			
			if (oos != null) {
				try { oos.close(); } catch(Exception ignored) { }
			}
			
			if (fos != null) {
				try { fos.close(); } catch(Exception ignored) { }
			}
			
		}
		
	}
	
	*//**
	 * Cache file 로드
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 *//*
	public static Object loadCacheFile(String fileName) throws Exception {
		return loadCacheFile(GlobalCom.getProperties("curation.cache.folder"), fileName);
	}
	
	*//**
	 * Cache file 로드
	 * 
	 * @param folderPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 *//*
	public static Object loadCacheFile(String folderPath, String fileName) throws Exception {
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		String filePath = getCacheFilePath(folderPath, fileName);
		
		File file = new File(filePath);
		if (!file.exists()) {
//			throw new CurationException("Can't found cache file. " + filePath);
			throw new ImcsException();
		}
		
		try {
			
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			return ois.readObject();
			
		} catch(Exception e) {
			throw e;
		} finally {
			
			if (ois != null) {
				try { ois.close(); } catch(Exception ignored) { }
			}
			
			if (fis != null) {
				try { fis.close(); } catch(Exception ignored) { }
			}
			
		}
	}*/
	
}
