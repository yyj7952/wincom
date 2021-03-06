//package kr.co.imcs.api.patch;
//
//
//import java.io.File;
//import java.io.FileFilter;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedHashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//import org.tmatesoft.svn.core.SVNException;
//import org.tmatesoft.svn.core.SVNLogEntry;
//import org.tmatesoft.svn.core.SVNLogEntryPath;
//import org.tmatesoft.svn.core.SVNNodeKind;
//import org.tmatesoft.svn.core.SVNURL;
//import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
//import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
//import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
//import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
//import org.tmatesoft.svn.core.io.SVNRepository;
//import org.tmatesoft.svn.core.wc.SVNWCUtil;
//
//public class CurationApiPatchMaker {
//	private  static CurationApiPatchMaker application = null;  
//	
//	private  final long REV_MAX = -1; 
//	private  final String SEPARATOR = System.getProperty("file.separator");
//	private  final String TEMP_WAR_FOLDER_NAME = "temp-war";
//	private  final String PATCH_FOLDER_NAME = "patch";
//	private  final String SOURCE_BASE_FOLDER_NAME = "/app/ctis/war";
//	private  final String DEPLOY_FOLDER_NAME1 = "/app/ctis/webroot1/curation_api.war";
//	private  final String DEPLOY_FOLDER_NAME2 = "/app/ctis/webroot2/curation_api.war";
//	private  final String DEPLOY_LIB_FOLDER = "/WEB-INF/lib";
//	private  final String LOG_COMMAND = " | tee -a $LOGPATH/$LOG";
//	
//	private final String LOG_FOLDER      = SOURCE_BASE_FOLDER_NAME + "/log";
//	private final String BACKUP_FOLDER =SOURCE_BASE_FOLDER_NAME + "/backup";
//	private final String BACKUP_FILE_NAME = "curation_api.war.backup"; 
//	
//	private boolean bSuccess = true;
//	
//	public  final String ENTER = "\n"; 
//	
//	private final String url = "svn://49.168.58.40/curation/api/trunk/Curation_API_Dev";
//	private final String[] includes = new String[] { 
//			"/trunk/Curation_API_Dev/src/main/resources@/WEB-INF/classes",
//			"/trunk/Curation_API_Dev/src/main/java@/WEB-INF/classes",
//			"/trunk/Curation_API_Dev/src/main/webapp" };
//	private final String id = "jaguar72";
//	private final String pwd = "jaguar72./123!@#$";
//	
//		
//	private SVNRepository repository = null;  
//	
//	//= 1305 - 1322 : 2016-02-03 ????????????
//	//= 1325 - 1335 : 2016-03-03 ????????????  : 1323,1324 ??? ?????? ??????(?????? ??????)
//	private long REV_START = 1325;  //= ??? ????????? ?????? 
//	private long REV_END = 1335;//= ??? ????????? ??????
////	private long REV_END = REV_MAX;//REV_MAX
//	private String SERVER_USER = "ctis";  //= ?????? ????????? Shell??? ???????????? User  : API = ctis, Admin = ctas
//	//"????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" 
//	//= ?????? ????????? <------------- ????????? ?????? ????????? ???.....: ......................................................
//	//= ??????????????? ?????? ???????????? ????????? ?????? DEPLOY_DATE ??? ????????? shell??? ???????????? ??????
//	private static  String DEPLOY_DATE = "20160303"; //= ??? ????????? ??????
//	//= ??????????????? ?????? Library ??????.. ?????? ?????? ????????? ?????? ?????????????????? ??????.
//	private final String[] libraryList = new String[] { //= ??? ????????? ??????
//			"nosql-cache-0.1.0.jar"
////			, "tempdb-0.1.0.jar" 
//	};
//	//"????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"
//	
//	private String SOURCE_FOLDER_NAME = "curation_api" + "_"+ DEPLOY_DATE;     //= source folder name
//	private final String SHELL_FILE_NAME = "deploy_src"+"_"+ DEPLOY_DATE + ".sh"; //= Run Shell File Name
//	
//	public static void main(String[] args) {
//		application = new CurationApiPatchMaker();
//		application.start();	
//	}
//	
//	private void start() {
//		String path = this.getClass().getResource("").getPath();
//		path = path.substring(0, path.lastIndexOf("/target/") + 8); 	
//		System.out.println("----- TARGET PATH : " + path + " -----");
//		
//		try {
//			if (url.startsWith("http://") || url.startsWith("https://")) {
//				DAVRepositoryFactory.setup();
//				repository = DAVRepositoryFactory.create(SVNURL.parseURIDecoded(url), null);
//			} else if (url.startsWith("svn://")) {
//				SVNRepositoryFactoryImpl.setup();
//				repository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIDecoded(url), null);
//			} else if (url.startsWith("file://")) {
//				FSRepositoryFactory.setup();
//				repository = FSRepositoryFactory.create(SVNURL.parseURIDecoded(url), null);
//			} else {
//				throw new RuntimeException("Bad URL !!!");
//			}
//			ISVNAuthenticationManager authenticationManager =  SVNWCUtil.createDefaultAuthenticationManager(id, pwd);
//			repository.setAuthenticationManager(authenticationManager);
//			
//			REV_END = (REV_END == -1 ? repository.getLatestRevision() : REV_END);
//			showSvnRevisionHistoryAndMakeShell(path, REV_START, REV_END);
//			
//		} catch (SVNException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * target ??????????????? ?????? war ?????? ?????? 
//	 * @Author : medialog
//	 * @Date : 2015. 8. 19. ?????? 11:25:15
//	 * @Description :
//	 * @param prjTargetPath
//	 * @return
//	 */
//	private File findWarFile(File prjTargetPath) {
//		File[] files = prjTargetPath.listFiles(new FileFilter() {
//			@Override
//			public boolean accept(File pathname) {
//				return pathname.getName().endsWith(".war");
//			}
//		});
//		File rtnFile = (files != null && files.length > 0 ? files[0] : null);
//		if(rtnFile != null) {
//			for(int i = 0; i < files.length-1; i++ ) {
//				rtnFile = files[i].lastModified() > files[i+1].lastModified() ?  files[i] : files[i+1]; 
//			}
//		}
//		return rtnFile;
//	}
//	
//	/**
//	 * war ????????? ?????? ????????? ????????? ?????? ?????????. 
//	 * @Author : medialog
//	 * @Date : 2015. 8. 19. ?????? 11:28:05
//	 * @Description :
//	 * @param zipFile
//	 * @param print
//	 * @return
//	 * @throws IOException
//	 */
//	private File unZipCompress(String root, File zipFile, boolean print) throws IOException {
//		System.out.println("------------- unZipCompress -----------------");
//		File rootDirectory = new File(root + SEPARATOR + TEMP_WAR_FOLDER_NAME);
//		rootDirectory.mkdirs();
//		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
//        ZipEntry ze = null;
//        while((ze = zis.getNextEntry())!=null){
//            String extractName = rootDirectory.getAbsolutePath() + SEPARATOR + ze.getName();
//            extractName = extractName.replace("/","\\");
//            if(print) {
//            	System.out.print("Extracting " + ze.getName() + " -> " + extractName + "...");
//            }
//            //create all folder needed to store in correct relative path.
//            File f = new File(extractName);
//            if(ze.isDirectory()) {
//            	f.mkdirs();
//            } else {
//            	f.getParentFile().mkdirs();
//            	f.createNewFile();
//            }
//            if(f.isFile()) {
//            	FileOutputStream fos = new FileOutputStream(f);
//                int len;
//                byte buffer[] = new byte[1024];
//                while ((len = zis.read(buffer)) > 0) {
//                    fos.write(buffer, 0, len);
//                }
//                fos.close();
//            }
//            if(print) {
//            	System.out.println("OK!");
//            }
//        }
//        zis.closeEntry();
//        zis.close();
//        return rootDirectory;
//	}
//	
//	/**
//	 * Shell ??? ?????? ??????
//	 * @param shellScripts
//	 * @author Jeffery
//	 * @since 2016.02.01
//	 */
//	private void baseShellPg(List<String> shellScripts)
//	{
//		shellScripts.add("#! /bin/bash");
//		shellScripts.add("\n");
//		shellScripts.add("ECHO=`which echo`");
//		shellScripts.add("DATE=`which date`");
//		shellScripts.add("PWD=`which pwd`");
//		shellScripts.add("PWDD=`$PWD`");
//		shellScripts.add("TODAY=`$DATE +%Y%m%d`");
//		shellScripts.add("BACKUPPATH=\"" + BACKUP_FOLDER + "\"");  //= Backup folder ??????
//		shellScripts.add("LOGPATH=\"" + LOG_FOLDER + "\"");  //= Patch Log ??????
//		shellScripts.add("LOG=\"patch.$TODAY.log\"");
//
//		//= Shell ???????????? User ??? ?????? ??????
//		shellScripts.add("UNAME=`id -u -n`");
//		shellScripts.add("if [ $UNAME != \""+ SERVER_USER+"\" ]");
//		shellScripts.add("then");
//		shellScripts.add("echo \"Starting Failed !!  Use "+ SERVER_USER+" account to start Patch ...\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("exit;");
//		shellScripts.add("fi");
//		
//		
//		//= ?????? Base ????????? ?????? ?????? Shell ??????
//		shellScripts.add("if [ \"$PWDD\" = \"" + SOURCE_BASE_FOLDER_NAME + "\" ]");
//		shellScripts.add("then");
//		shellScripts.add("echo \"*********************************************************************\"\n");
//		shellScripts.add("else");
//		shellScripts.add("echo \"Invalid Base Path : $PWDD \""  + LOG_COMMAND  +"\n");
//		shellScripts.add("exit"); 
//		shellScripts.add("fi");
//		
//		//= Patch ????????? ????????? Shell??? ???????????? ??????
//		shellScripts.add("if [ \"$TODAY\" = \"" + DEPLOY_DATE + "\" ]");
//		shellScripts.add("then");
//		shellScripts.add("echo \"*********************************************************************\"\n");
//		shellScripts.add("else");
//		shellScripts.add("echo \"Check deploying date.\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("echo \"Deploying date is " + DEPLOY_DATE +" \""  + LOG_COMMAND  +"\n");
//		shellScripts.add("exit"); 
//		shellScripts.add("fi");
//		
//		//= Shell ?????? Patch????????? ?????? ?????? Shell ??????
//		shellScripts.add("if [ \"${0##*/}\" = \"" + SHELL_FILE_NAME + "\" ]");
//		shellScripts.add("then");
//		shellScripts.add("echo \"*********************************************************************\"\n");
//		shellScripts.add("else");
//		shellScripts.add("echo \"Shell Program is inValid. \""  + LOG_COMMAND  +"\n");
//		shellScripts.add("echo \"Check your shell Program and Patch File  and shell file name is "+SHELL_FILE_NAME+" and Deploying date is " + DEPLOY_DATE + "\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("exit"); 
//		shellScripts.add("fi");
//		
//		
//		//= ?????? ????????? ?????? ????????? ???????????? ????????? Shell ??????
//		shellScripts.add("if [ -f \"./" + SOURCE_FOLDER_NAME + ".zip"+ "\" ]");
//		shellScripts.add("then");
//		shellScripts.add("echo \"*********************************************************************\"\n");
//		shellScripts.add("else");
//		shellScripts.add("echo \"Patch File Not Found : "+SOURCE_FOLDER_NAME + ".zip"+ " \""  + LOG_COMMAND  +"\n");
//		shellScripts.add("exit"); 
//		shellScripts.add("fi");
//	}
//	
//	
//	/**
//	 * ?????? ????????? ?????? ??????
//	 * @author Jeffery
//	 * @since 2016.01.28
//	 */
//	private void backupSrc(List<String> shellScripts)
//	{
//
//		shellScripts.add("echo \"*********************************************************************\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("mkdir -pv $LOGPATH" + LOG_COMMAND );
//		shellScripts.add("rm -fv $LOGPATH/$LOG" + LOG_COMMAND );
//		shellScripts.add("mkdir -pv $BACKUPPATH" + LOG_COMMAND );
//		shellScripts.add("rm -v $BACKUPPATH/"+BACKUP_FILE_NAME+".$TODAY" + LOG_COMMAND );
//		shellScripts.add("\n");
//		shellScripts.add("echo \"*********************************************************************\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("echo \"backup start\""  + LOG_COMMAND );
//		shellScripts.add("\nsleep 2 \n");
//		
//		shellScripts.add("tar czf $BACKUPPATH/"+BACKUP_FILE_NAME+".$TODAY "+ DEPLOY_FOLDER_NAME1 + LOG_COMMAND );
//		shellScripts.add("\n");
//		shellScripts.add("echo \"backup end\"" + LOG_COMMAND ); 
//	}
//	
//	/**
//	 * ?????? Lib ??????
//	 * @param shellScripts
//	 * @author Jeffery
//	 * @since 2016.01.28
//	 */
//	private void backupLib(List<String> shellScripts, File warDeComporessDir, File patchSourceDir) throws  IOException
//	{
//		//= ????????? ???????????? ?????? ????????? ??????????????? ??????.
//		shellScripts.add("\necho \"*********************************************************************\""  + LOG_COMMAND  +"\n");
//		shellScripts.add("mkdir -p " + DEPLOY_FOLDER_NAME1 + DEPLOY_LIB_FOLDER + LOG_COMMAND);
//		shellScripts.add("mkdir -p " + DEPLOY_FOLDER_NAME2 + DEPLOY_LIB_FOLDER + LOG_COMMAND);
//		
//		for(String library : libraryList) {
//			shellScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + library + " "+ DEPLOY_FOLDER_NAME1 + DEPLOY_LIB_FOLDER + "/" + library+ LOG_COMMAND);
//			shellScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + library + " "+ DEPLOY_FOLDER_NAME2 + DEPLOY_LIB_FOLDER + "/" + library+ LOG_COMMAND);
//			
//			System.out.println(warDeComporessDir.getAbsolutePath() + DEPLOY_LIB_FOLDER + SEPARATOR + library);
//			System.out.println(patchSourceDir + "/" + library);
//			
//			File f = new File(warDeComporessDir.getAbsolutePath() + DEPLOY_LIB_FOLDER + SEPARATOR + library);
//			File createFile = new File(patchSourceDir + "/" + library);
//			if(f.exists()) {
//				fileCopy(f, createFile);
//			} else {
//				bSuccess = false;
//				System.err.println("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" );
//				System.err.print("?????? ????????? ?????? ?????? ?????? ?????? ?????? :" + warDeComporessDir.getAbsolutePath() + DEPLOY_LIB_FOLDER + "/" + library);
//			}
//		}
//	}
//
//	/**
//	 * ?????? ??????(?????? ??????)
//	 * @param file
//	 */
//	private void removeDir(File file){
//
//		 File[] fileList = file.listFiles();
//
//		 for(int k=0;k<fileList.length;k++)
//		 {
//			  if(fileList[k].isFile()){
//			   fileList[k].delete();
//		  }
//
//		  if(fileList[k].isDirectory()){
//			  removeDir(fileList[k]);
//		  }
//
//		  fileList[k].delete();
//		 }
//	}
//
//	
//	/**
//	 * SVN Info.
//	 * 1. SVN??? ?????? ????????? ????????? ????????? ?????? ?????? ????????? Console??? ????????? ??? ????????? Patch ????????? Shell Script ????????? ????????? ????????????.
//	 * @param root
//	 * @param startRevision
//	 * @param endRevision
//	 * @throws SVNException
//	 * @throws IOException
//	 */
//	private void showSvnRevisionHistoryAndMakeShell(String root, long startRevision, long endRevision) throws SVNException, IOException {
//		List<String> shellScripts = new LinkedList<String>();
//		List<String> baseShellScripts = new LinkedList<String>();
//		List<String> backupShellScripts = new LinkedList<String>();
//		Set<String> sourceScripts = new LinkedHashSet<String>();
//		
//		File warFile = findWarFile(new File(root));
//		File warDeComporessDir = unZipCompress(root, warFile, false);
//		File patchDir = new File(root + SEPARATOR + PATCH_FOLDER_NAME);
//		File patchFile = new File(root + SEPARATOR + PATCH_FOLDER_NAME + SEPARATOR + SHELL_FILE_NAME);
//		File patchSourceDir = new File(root + SEPARATOR + PATCH_FOLDER_NAME + SEPARATOR + SOURCE_FOLDER_NAME);
//		patchDir.mkdirs();
//		//= Remove Patch Source Folder with files
//		if(patchSourceDir.exists()) {
//			removeDir(patchSourceDir);
//		} 
//		patchSourceDir.mkdirs();
//		patchFile.createNewFile();
//		
//		FileWriter fw = new FileWriter(patchFile);
//		
//		//= Base Script ??????
//		this.baseShellPg(baseShellScripts);
//		
//		for(String script : baseShellScripts) {
//			System.out.println(script);
//			fw.write(script + "\n");
//		}
//		baseShellScripts.clear();		
//		//= Backup Script ??????
//		this.backupSrc(backupShellScripts);
//		
//		for(String script : backupShellScripts) {
//			System.out.println(script);
//			fw.write(script + "\n");
//		}
//		backupShellScripts.clear();
//		
//		fw.write("\nsleep 2 \n");
//		fw.write("\necho \"*********************************************************************\" " + LOG_COMMAND + "\n");
//		fw.write("\necho \"Unzipcompress..........\" " + LOG_COMMAND + " \n");
//
//		//= source folder ??? zip?????? ????????? ?????? ???????????? ??????.
//		fw.write("unzip -d " + SOURCE_FOLDER_NAME + " " + SOURCE_FOLDER_NAME + ".zip"  + LOG_COMMAND + "\n"); 
//		
//		fw.write("\nsleep 2 \n");
//		fw.write("\necho \"*********************************************************************\" " + LOG_COMMAND + " \n");
//		fw.write("\n\necho \"Patch Start\" " + LOG_COMMAND + " \n");
//		
//		//= Library ??? Patch ????????? ?????? ??????
//		this.backupLib(shellScripts, warDeComporessDir, patchSourceDir);
//		
//		if (repository != null) {
//			Collection<SVNLogEntry> logEntries = repository.log(new String[] {}, null, startRevision, endRevision, true, true);
//			for (Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext();) {
//				SVNLogEntry logEntry = entries.next();
//				if (logEntry.getChangedPaths().size() > 0) {
//					System.out.println("Change Date : " + logEntry.getDate() + "Revision : " + logEntry.getRevision());
//				
//					String path = null;
//					SVNLogEntryPath deleteLog = null;
//					for(String key : logEntry.getChangedPaths().keySet()) {
//						SVNLogEntryPath log = logEntry.getChangedPaths().get(key);
//						
//						if((path = filterString(log.getPath())) != null) {
//							System.out.println(log.getType() + " : " + log.getPath() + ", " + log.getCopyPath() + "....OK");
//							if(path.endsWith(".java")) {
//								path = path.replace(".java", ".class");
//							}
//							switch (log.getType()) {
//								case SVNLogEntryPath.TYPE_ADDED:
//									if(log.getCopyPath() != null) {
//										String copyPath = filterString(log.getCopyPath());
//										if(copyPath != null && deleteLog != null) {
//											shellScripts.add("mv -fv "+ DEPLOY_FOLDER_NAME1 + copyPath + " " + DEPLOY_FOLDER_NAME1 + path + LOG_COMMAND );
//											shellScripts.add("mv -fv "+ DEPLOY_FOLDER_NAME2 + copyPath + " " + DEPLOY_FOLDER_NAME2 + path + LOG_COMMAND );
//										} else {
//											shellScripts.add("cp -rfv "+ DEPLOY_FOLDER_NAME1 + copyPath + " " + DEPLOY_FOLDER_NAME1 + path + LOG_COMMAND );
//											shellScripts.add("cp -rfv "+ DEPLOY_FOLDER_NAME2 + copyPath + " " + DEPLOY_FOLDER_NAME2 + path + LOG_COMMAND );
//										}
//									} else {
//										if(log.getKind().equals(SVNNodeKind.DIR)) {
//											shellScripts.add("mkdir -p "+ DEPLOY_FOLDER_NAME1 + path + LOG_COMMAND );
//											shellScripts.add("mkdir -p "+ DEPLOY_FOLDER_NAME2 + path + LOG_COMMAND );
//										} else {
//											String filename = path.substring(path.lastIndexOf("/"));
//											
//											File f = new File(warDeComporessDir.getAbsolutePath() + path);
//											File createFile = new File(patchSourceDir + filename);
//											if(f.exists()) {
//												//=?????? ?????? ?????? ?????? ??????
//												fileCopy(f, createFile);
//												//= ?????? ?????? ????????????
//												copyShellScript(f, path, sourceScripts) ;
//											} else {
//												bSuccess = false;
//												System.err.println("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" );
//												System.err.println("?????? ????????? ?????? ?????? ?????? ?????? ?????? " + path);
//											}
//										}
//									}
//									deleteLog = null;
//									break;
//								case SVNLogEntryPath.TYPE_REPLACED:
//									String copyPath = filterString(log.getCopyPath());
//									shellScripts.add("mv -fv "+ DEPLOY_FOLDER_NAME1 + copyPath + " " + DEPLOY_FOLDER_NAME1 + path + LOG_COMMAND );
//									shellScripts.add("mv -fv "+ DEPLOY_FOLDER_NAME2 + copyPath + " " + DEPLOY_FOLDER_NAME2 + path + LOG_COMMAND );
//									deleteLog = null;
//									break;
//								case SVNLogEntryPath.TYPE_MODIFIED:
//									String filename = path.substring(path.lastIndexOf("/"));
//									File f = new File(warDeComporessDir.getAbsolutePath() + path);
//									File createFile = new File(patchSourceDir + filename);
//									if(f.exists()) {
//										//=?????? ?????? ?????? ?????? ??????
//										fileCopy(f, createFile);
//										//= ?????? ?????? ????????????
//										copyShellScript(f, path, sourceScripts) ;
//									} else {
//										bSuccess = false;
//										System.err.println("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" );
//										System.err.println("?????? ????????? ?????? ?????? ?????? ?????? ?????? " + path);
//									}
//									deleteLog = null;
//									break;
//								case SVNLogEntryPath.TYPE_DELETED:
//									if(deleteLog != null) {
//										shellScripts.add("rm -rfv "+ DEPLOY_FOLDER_NAME1 + filterString(deleteLog.getPath()) + LOG_COMMAND );
//										shellScripts.add("rm -rfv "+ DEPLOY_FOLDER_NAME2 + filterString(deleteLog.getPath()) + LOG_COMMAND );
//									}  
//									deleteLog = log;
//								default:
//									break;
//							}
//						} else {
//							System.err.println(log.getType() + " : " + log.getPath() + ", " + log.getCopyPath() + "....PASS");
//						}
//					}
//					if(deleteLog != null) {
//						shellScripts.add("rm -rfv "+ DEPLOY_FOLDER_NAME1 + filterString(deleteLog.getPath()) + LOG_COMMAND );
//						shellScripts.add("rm -rfv "+ DEPLOY_FOLDER_NAME2 + filterString(deleteLog.getPath()) + LOG_COMMAND );
//					}
//				}
//				for(String script : shellScripts) {
//					System.out.println(script);
//					fw.write(script + "\n");
//				}
//				
//				System.out.println();
//				shellScripts.clear();
//			}
//			for(String source : sourceScripts) {
//				System.out.println(source);
//				fw.write(source+ "\n");
//			}
//			sourceScripts.clear();
//		}
//		
//		fw.write("\necho \"Patch End...............\" " +LOG_COMMAND+ "\n");
//		//= ??????????????? ???????????? ?????? ?????? ??????
//		fw.write("\necho \"*********************************************************************\" " +LOG_COMMAND+ "\n");
//		fw.write("\necho \"Last processing..... \"  " +LOG_COMMAND+ "\n");
//		fw.write("rm -rf " + SOURCE_FOLDER_NAME +LOG_COMMAND + "\n");   //= Source Folder ??????
//		fw.write("mv -f " + SOURCE_FOLDER_NAME + ".zip" + " " + "$BACKUPPATH/" +SOURCE_FOLDER_NAME + ".zip.bak" +LOG_COMMAND + "\n");  //= zip ?????? backup ?????? ??????
//		fw.write("mv -f " + SHELL_FILE_NAME + " " + "$BACKUPPATH/" + SHELL_FILE_NAME + ".bak" + LOG_COMMAND + "\n"); //= Shell Pg ?????? backup?????? ??????
//		
//		
//		fw.write("\necho \"*********************************************************************\" "+LOG_COMMAND+ "\n");
//		fw.write("\nsleep 2 \n");
//		fw.write("echo \"Required !! Configuration File(curation_api.properties, errorcode.properties, \ncuration_api_ext.properties, logback.xml, jar etc) Check!!\"  " +LOG_COMMAND+ "\n");
//		fw.close();
//		
//		if (bSuccess)
//		{
//			System.out.println("-------------All files created -----------------");	
//		} else
//		{
//			System.err.println("-------------Patch File creation Failed -----------------");
//		}
//		
//		
//	}
//	
//	/**
//	 * Shell Script??? ?????? ???????????? ??????
//	 * @param oFile
//	 * @param destCopy
//	 * @throws IOException
//	 * @author Jeffery
//	 * @since 2016.01.28
//	 */
//	private void copyShellScript(File oFile, String sSVNPath, Set<String> sourceScripts) throws IOException{
//		
//		String sNewPath = sSVNPath.substring(0, sSVNPath.lastIndexOf("/")); //= class Path
//		//-----------------------------------------------------------------------------
//		//= Original File Info.
//		String sName = oFile.getName(); //= ?????? ?????????
//		String sFileName = sName.substring(0, sName.lastIndexOf("."));  //= ????????? ?????? ?????????
//		String sExt         = sName.substring(sName.lastIndexOf(".") + 1); //= ?????????
//		
//		
//		String sFullPath   = oFile.getAbsolutePath();
//		String sPath = sFullPath.substring(0, sFullPath.lastIndexOf(SEPARATOR));;
//		
//		
//		//= ????????? ???????????? ?????? ????????? ??????????????? ??????.
//		sourceScripts.add("\necho \"*********************************************************************\" "  + LOG_COMMAND );
//		sourceScripts.add("mkdir -p " + DEPLOY_FOLDER_NAME1 + sNewPath  + LOG_COMMAND );
//		sourceScripts.add("mkdir -p " + DEPLOY_FOLDER_NAME2 + sNewPath  + LOG_COMMAND );
//		//= ????????? java??? ???????????? ???????????? compiled ????????? ?????? ???????????? ????????? + "*" ??? ?????? ?????? ??? ??? ????????? ???????????? ??????
//		
//		if(sExt.equalsIgnoreCase("class"))
//		{//= ????????? ????????? ?????????
//			sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sName + "    "+ DEPLOY_FOLDER_NAME1 + sNewPath + "/"  + LOG_COMMAND );
//			sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sName + "    "+ DEPLOY_FOLDER_NAME2 + sNewPath + "/"  + LOG_COMMAND );			
//			
//			boolean b$Finded = false;
//			File dirFile = new File(sPath);
//			
//			String[] sFileList = dirFile.list(); //= Folder ??? ?????? ??????
//			//= Subclass ??? ?????? ??? ???????????? ??????
//			for(String sOneFile : sFileList) {
//
//				if(sOneFile.startsWith(sFileName+"$") && sOneFile.endsWith(".class"))
//				{
//					b$Finded = true;
//					break;
//				}
//			}
//			//= Sub class ??? ?????? ?????? ?????? ??????
//			if(b$Finded)
//			{
//				sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sFileName + "\"$\"*." + sExt + "  "+ DEPLOY_FOLDER_NAME1 + sNewPath + "/"  + LOG_COMMAND );
//				sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sFileName + "\"$\"*." + sExt + "  "+DEPLOY_FOLDER_NAME2 + sNewPath + "/"  + LOG_COMMAND );		
//			}
//			
//			
//		} else
//		{ //= ?????? ?????????
//			sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sName + "  "+DEPLOY_FOLDER_NAME1 + sNewPath + "/"  + LOG_COMMAND );
//			sourceScripts.add("cp -rfv ./" + SOURCE_FOLDER_NAME + "/" + sName + "  "+DEPLOY_FOLDER_NAME2 + sNewPath + "/"  + LOG_COMMAND );
//		}
//		
//
//	}
//	
//	/**
//	 * ?????? ???????????? ??????
//	 * @param oFile
//	 * @param tFile
//	 * @throws IOException
//	 * @author Jeffery
//	 * @since 2016.01.28
//	 */
//	private void fileCopy(File oFile, File tFile) throws IOException{
//		//-----------------------------------------------------------------------------
//		//= Original File Info.
//		String sFullPath   = oFile.getAbsolutePath();
//		String sPath = "";
//		String sName = oFile.getName();
//		
//		sPath = sFullPath.substring(0, sFullPath.lastIndexOf(SEPARATOR));
//		
//		String sFileName = sName.substring(0, sName.lastIndexOf("."));
//		String sExt         = sName.substring(sName.lastIndexOf(".") + 1);
//		//-----------------------------------------------------------------------------
//		//= Target File Info.
//		String sDFullPath   = tFile.getAbsolutePath();
//		String sDPath = "";
//
//		sDPath = sDFullPath.substring(0, sDFullPath.lastIndexOf(SEPARATOR));
//		
//		File originalFile = null;
//		File targetFile = null;
//		if(sExt.equalsIgnoreCase("class"))
//		{
//			File dirFile = new File(sPath);
//			
//			String[] sFileList = dirFile.list(); //= Folder ??? ?????? ??????
//			
//			for(String sOneFile : sFileList) {
//
//				if((sName).equals(sOneFile) ||  sOneFile.startsWith(sFileName+"$") )
//				{
//					System.out.println("In 1 :: " + sOneFile);
//					
//					originalFile = new File(sPath + SEPARATOR + sOneFile);
//					targetFile = new File(sDPath + SEPARATOR + sOneFile);
//					
//				} else
//				{
////					System.out.println("In else :: SKIPPED ::: " + sOneFile);
//					continue;
//				}
//				
//				//= File??? ?????? ???????????? ??????
//				fileCopying(originalFile, targetFile);
//			}
//			
//		} else
//		{
//			//= File??? ?????? ???????????? ??????
//			fileCopying(oFile, tFile);
//		}
//		
//	}
//	
//	/**
//	 * ?????? ????????? ???????????? ??????
//	 * @param originalFile
//	 * @param targetFile
//	 * @throws IOException
//	 * @author Jeffery
//	 * @since 2016.01.28
//	 * 
//	 */
//	private void fileCopying(File originalFile, File targetFile) throws IOException{
//		if(targetFile.exists()) {
//			targetFile.delete();
//		} 
//		targetFile.createNewFile();
//		FileInputStream fis = new FileInputStream(originalFile);
//		FileOutputStream fos = new FileOutputStream(targetFile);
//        int len;
//        byte buffer[] = new byte[1024];
//        while ((len = fis.read(buffer)) > 0) {
//            fos.write(buffer, 0, len);
//        }
//        fos.close();
//        fis.close();
//
//        originalFile = null;
//        targetFile = null;
//	}
//	
//	
//	private String filterString(String path) {
//		for(String filter : includes) {
//			int pos = filter.indexOf("@");
//			
//			if(pos != -1 ) {
//				String[] replaceFilter = filter.split("@");
//				if(path.startsWith(replaceFilter[0])) {
//					return replaceFilter[1] + path.substring(replaceFilter[0].length());
//				}
//			} else {
//				if(path.startsWith(filter)) {
//					return path.substring(filter.length());
//				}
//			}
//			
//		}
//		return null;
//	}
//
//}
