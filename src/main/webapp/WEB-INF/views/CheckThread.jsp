<%@ page import="java.lang.management.*,java.lang.Thread.State,java.util.Map,java.util.HashMap,java.util.HashSet,java.util.Date"%>
<%@ page import="java.text.DecimalFormat,java.text.SimpleDateFormat"%>

<%!
/************** page variable and method define start ************************/
	final String versionAndTitle="Thread Checker v.2008.12.16.";	
	final int[] optionSeconds={1,3,5,10,20,30,60,600};
	final String numberPattern="###,###,###,###.###";
	final DecimalFormat format=new DecimalFormat(numberPattern);
	public String getTimeString(long nanoSecond) {
		long tempMilli=nanoSecond/1000000;
		if(tempMilli<1000) {
			return format.format(tempMilli/1000.0)+" ms";
		} else if(tempMilli<60000) {
			return format.format(tempMilli/1000.0) +" sec";
		} else  {
			return format.format(tempMilli/60000.0) +" min";
		} 
	}
	final String pageWidth="1024";//"100%";
	final String pageWidth2="1000";//"95%";
	final SimpleDateFormat saveFileFormat =new SimpleDateFormat("yyyy-MM-dd_HH mm ss");

	
	
/************** page variable and method define end ************************/
%>
<HTML>
<HEAD>
	<TITLE><%=versionAndTitle %> by Sangmin, Lee</TITLE>

<%
String saveFileName=saveFileFormat.format(new Date());
/************** request value check start ************************/
//out.println("saveFileName="+saveFileName);
	boolean autoRefresh=false;
	String autoRefreshString="";
	String autoRefreshReceive=request.getParameter("autoRefresh");
	if(autoRefreshReceive!=null ) {
		autoRefresh=true;
		autoRefreshString=" checked ";
		//out.println("auto receive="+autoRefreshReceive);
	}
	
	
	int refreshSecond=3;
	String refreshSecondReceive=request.getParameter("refreshSecond");
	if(refreshSecondReceive!=null) {
		refreshSecond=Integer.parseInt(refreshSecondReceive);
	}
	
	boolean viewAllThreadInfo=false;
	String viewAllThreadInfoString="";
	String viewAllThreadInfoReceive=request.getParameter("viewAllThreadInfo");
	if(viewAllThreadInfoReceive!=null) {
		viewAllThreadInfo=true;
		viewAllThreadInfoString=" checked ";
	}
	boolean viewLockInfo=false;
	String viewLockInfoString="";
	String viewLockInfoReceive=request.getParameter("viewLockInfo");
	if(viewLockInfoReceive!=null) {
		viewLockInfo=true;
		viewLockInfoString=" checked ";
	}
	int stackTraceNumber=100;
	String stackTraceNumberReceive=request.getParameter("stackTraceNumber");
	if(stackTraceNumberReceive!=null) {
		stackTraceNumber=Integer.parseInt(stackTraceNumberReceive);
	}
	
/************** request value check end ************************/
%>
<SCRIPT>
var t=setTimeout("refresh()",<%=refreshSecond*1000%>);
var refreshFlag=<%=autoRefresh%>;
function refresh() {
	if(refreshFlag==true) {
		document.options.submit();
	}
}
function changeOption() {
	document.options.submit();
}
function viewLockDetailTable(){
	if(document.options.viewAllThreadInfo.checked==true) {
		lockDetailInfo.style.visibility="visible";
	} else {
		lockDetailInfo.style.visibility="hidden";
	}
}
function saveCurrentState(){
  if(document.options.autoRefresh.checked) {
    
    document.options.autoRefresh.checked=false;
    document.execCommand('SaveAs', null, '<%=saveFileName%>.htm');
    document.options.autoRefresh.checked=true;
  } else {
    document.execCommand('SaveAs', null, '<%=saveFileName%>.htm');
  }
}
</SCRIPT>
<style type="text/css">
<!--
.txt {
	font-family: "arial";
	font-size: 12px;
	color: #333333;
}
.txtx {
	background-color:red;
	font-family: "arial";
	font-size: 20px;
	color: white;
	font-weight: bold;
}
.style2 {
	font-family: "arial";
	font-size: 14px;
	color: #000066;
	font-weight: bold;
}

.style5 {color: #C2C2C2}
.style6 {font-size: 10px}
-->
</style>
</HEAD>
<BODY onload="viewLockDetailTable()">
<table width="<%=pageWidth %>" border="0" cellspacing="0">
  <tr>
    <td height="60" align="center" bgcolor="#B8C6CD">
    <table width="<%=pageWidth2 %>" border="0" cellspacing="0">
      <tr>
        <td height="44" align="center" bgcolor="#EBEBEB"><span class="style2"><%=versionAndTitle%> by Sangmin, Lee</span></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="4" align="center" bgcolor="#003399"></td>
  </tr>
  <tr>
    <td height="4" align="center" bgcolor="#ffffff"></td>
  </tr>
  <tr>
    <td height="2" align="center" bgcolor="#999999"></td>
  </tr>
</table>
<BR>
<script>
if(navigator.appName.indexOf("Microsoft") != -1){
  document.write("<button onclick='saveCurrentState()'>Save current state</button>");
  //window.alert("IE");
} 
</script>
<BR>
<FORM name=options method=post>
<!---------------------- Top option part start ---------------------->
<TABLE width="900>">
<TR>
<TD width="713">
	<TABLE width="100%" border=0 cellpadding="5" cellspacing="1" bgcolor="#003399">
<TR>
		<TD bgcolor="#FFFFFF">
		<button onclick="changeOption()">Change Options</button>
		</TD>
		<TD bgcolor="#FFFFFF" class="txt">
			<input type=checkbox name=autoRefresh <%=autoRefreshString %>> Auto Refresh
		</TD>
		<TD bgcolor="#FFFFFF" class="txt">Refresh rate:
			<SELECT name="refreshSecond">
<%
		for(int loop=0;loop<7;loop++) {
			out.print("<option value=");
			out.print(optionSeconds[loop]);
			if(refreshSecond==optionSeconds[loop]) {
				out.print(" selected ");
			}
			out.print(">");
			out.print(optionSeconds[loop]);
			out.println(" Seconds</option>\n");
		}
%>		
			</SELECT>
		</TD>
		<TD bgcolor="#FFFFFF" class="txt">
			<input type=checkbox name=viewAllThreadInfo onclick="viewLockDetailTable()" <%=viewAllThreadInfoString %>> View all threads
		</TD>
		</TR>
	</TABLE>
</TD><TD width="175" align="right">
	<DIV id=lockDetailInfo>
	<TABLE>
		<TR>
		<TD bgcolor="#FFFFFF" class="txt">
			<input type=checkbox name=viewLockInfo <% out.println(viewLockInfoString); %>> 
			View Lock Info
		</TD>
		<TD bgcolor="#FFFFFF" class="txt">
			<Select name=stackTraceNumber>
				<option value=100 <%= stackTraceNumber==100 ? "selected" : "" %>>ALL</option>
				<option value=1 <%= stackTraceNumber==1 ? "selected" : "" %>>1</option>
				<option value=5 <%= stackTraceNumber==5 ? "selected" : "" %>>5</option>
				<option value=10 <%= stackTraceNumber==10 ? "selected" : "" %>>10</option>
				<option value=20 <%= stackTraceNumber==20 ? "selected" : "" %>>20</option>
				<option value=30 <%= stackTraceNumber==30 ? "selected" : "" %>>30</option>
			</Select>
		</TD>
		</TR>
	</TABLE>
	</DIV>
</TD></TR></TABLE>
<!---------------------- Top option part end ---------------------->
</FORM>

<%
/*************************** Data Collect start *******************/
  //long periodMillis=1000;
	
	StringBuffer sb=new StringBuffer();
	int blocked=0;
	int newthread=0;
	int runnable=0;
	int terminated=0;
	int timed_waiting=0;
	int waiting=0;
	//this attribute is used to change Lock Owner's color
	HashSet<String> lockOwnerSet=new HashSet<String>();
	
	sb.append("<TABLE id=detailThreadInfoTable  width=\"")
	.append(pageWidth)
	.append("\" border=0 cellpadding=\"5\" cellspacing=\"1\" bgcolor=\"#003399\">");
	/********************* check if I can see the lock info ********************************/
	if(viewLockInfo) {
		sb.append("<TR><TD width=\"40\" bgcolor=\"#E0E0E0\" class=\"txt\">Ordinal</TD>")
		.append("<TD width=\"88\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread Name</TD>")
		.append("<TD width=\"65\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread State</TD>")
		.append("<TD width=\"51\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread Time</TD>")
		.append("<TD width=\"88\" bgcolor=\"#E0E0E0\" class=\"txt\">Lock Owner</TD>")
		.append("<TD width=\"543\" bgcolor=\"#E0E0E0\" class=\"txt\">Stack Trace information</TD></TR>"); 
		
		
	} else {
		sb.append("<TR><TD width=\"40\" bgcolor=\"#E0E0E0\" class=\"txt\">Ordinal</TD>")
		.append("<TD width=\"88\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread Name</TD>")
		.append("<TD width=\"65\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread State</TD>")
		.append("<TD width=\"51\" bgcolor=\"#E0E0E0\" class=\"txt\">Thread Time</TD></TR>");
	}
	/********************* get ThreadMXBean info ********************************/
	ThreadMXBean tmxBean=ManagementFactory.getThreadMXBean();
	
	long threadList[]=tmxBean.getAllThreadIds();
	HashMap<Long,StackTraceElement[]> stackTraceMap=new HashMap<Long,StackTraceElement[]>();
	/********************* get Stack Traces infos start *******************/
	// Because tempThreadInfo.getStackTrace() don't return anything, I use this way
	if(viewLockInfo) {
		Map<Thread,StackTraceElement[]> stackInfos=null;
		stackInfos=Thread.getAllStackTraces();

		for (Thread  tempThread: stackInfos.keySet()) {
		    stackTraceMap.put(tempThread.getId(),stackInfos.get(tempThread));
	    }
	}
	/********************* get Stack Traces infos end *******************/
	try {
		boolean blockFlag=false;
		
		for(long id : threadList) {
			blockFlag=false;
			sb.append("<TR>");
			long tempThreadTime=tmxBean.getThreadCpuTime(id);
			ThreadInfo tempThreadInfo=tmxBean.getThreadInfo(id);
			
			String threadName=tempThreadInfo.getThreadName();
			State threadState=tempThreadInfo.getThreadState();//.getLockedSynchronizers();
			if(threadState==State.BLOCKED) {blocked++;
				blockFlag=true;
			} else if(threadState==State.NEW) {newthread++;
			} else if(threadState==State.RUNNABLE) {runnable++;
			} else if(threadState==State.TERMINATED) {terminated++;
			} else if(threadState==State.TIMED_WAITING) {timed_waiting++;
			} else if(threadState==State.WAITING){ waiting++;
			}

			//MonitorInfo[] mis=tempThreadInfo.getLockedMonitors();
			int tempOrdinal=threadState.ordinal();
			if(viewAllThreadInfo) {
				//sb.append("<BR>Ordinal="+tempOrdinal+" "+threadName+"=>"+threadState +" ThreadTime="+tempThreadTime);
				sb.append("<TD  align=\"center\" bgcolor=\"#FFFFFF\" class=\"txt\">");
				sb.append(tempOrdinal);
				sb.append("</TD><TD bgcolor=\"#FFFFFF\" class=txt id=\"")
						.append(threadName)
						.append("\">"); 
				sb.append(threadName);
				sb.append("</TD>");
				
				if(blockFlag) {
					sb.append("<TD bgcolor=\"red\" class=\"txt\" >");
					sb.append("<B><FONT color=white>");
					sb.append(threadState);
					sb.append("</FONT></B>");
				} else {
					sb.append("<TD bgcolor=\"#FFFFFF\" class=\"txt\" >");
					sb.append(threadState);
				}
				sb.append("</TD><TD align=right  bgcolor=\"#FFFFFF\" class=\"txt\" >");
				sb.append(getTimeString(tempThreadTime));
				sb.append("</TD>");
				if(viewLockInfo) {
					sb.append("<TD  bgcolor=\"#FFFFFF\" class=\"txt\" >");
					String lockOwnerName=tempThreadInfo.getLockOwnerName();
					if(lockOwnerName!=null) {
						sb.append(lockOwnerName);
						lockOwnerSet.add(lockOwnerName);
						
					} else {
						sb.append("&nbsp;");
					}
					sb.append("</TD>");
					
					sb.append("<TD  bgcolor=\"#FFFFFF\" class=\"txt\" >");
					StackTraceElement[] tempStackTraceElement =stackTraceMap.get(id);
					int stackTraceSize=tempStackTraceElement.length;
					if(stackTraceSize!=0) {
						if(stackTraceSize<=stackTraceNumber) {
							for (StackTraceElement line: tempStackTraceElement) {
								sb.append(line).append("<BR>");
							}
						} else{
							for(int loop=0;loop<stackTraceNumber;loop++) {
								sb.append(tempStackTraceElement[loop]).append("<BR>");
							}
						}
					} else {
						sb.append("&nbsp;");
					}
					sb.append("</TD>");
				} 
			}
			sb.append("</TR>\n");
		}
		sb.append("</TABLE>");
		out.flush();
		//Thread.sleep(periodMillis);
		
	} catch(Exception e) {
		e.printStackTrace();
	}
/*************************** Data Collect end *******************/
%>

<!-------------------------- Data View Start -------------------->
<table width="<%=pageWidth %>" border=0 cellpadding="5" cellspacing="1" bgcolor="#003399">
<tr>
	<td width="163" bgcolor="#E0E0E0" class="txt">Total Started Thread Count</td>
	<td width="55" bgcolor="#FFFFFF" class="txt"><%=tmxBean.getTotalStartedThreadCount()%></td> 
	<td width="149" bgcolor="#E0E0E0" class="txt">Peak Thread Count</td>
	<td width="41" bgcolor="#FFFFFF" class="txt"><%=tmxBean.getPeakThreadCount()%></td>
	<td width="149" bgcolor="#E0E0E0" class="txt">Current Thread Count</td>
	<td width="41" bgcolor="#FFFFFF" class="txt"><%=tmxBean.getThreadCount() %></td>
	<td width="149" bgcolor="#E0E0E0" class="txt">Daemon Thread Count</td>
	<td width="41" bgcolor="#FFFFFF" class="txt"><%= tmxBean.getDaemonThreadCount() %> </td>	
</tr>
</table>
<BR>
<Table width="<%=pageWidth %>" border=0 cellpadding="5" cellspacing="1" bgcolor="#003399">
<tr>
  <td bgcolor="#E0E0E0" class="txt">Thread State</td>
  <td bgcolor="#FFFFFF" class="txt">No. of state</td>
  <td bgcolor="#FFFFFF" class="txt">Description</td>
</tr>
<tr>
  <td bgcolor="#E0E0E0" class="txt">NEW</td>
  <td bgcolor="#FFFFFF" class="txt"><%=newthread %></td>
  <td bgcolor="#FFFFFF" class="txt">A thread that has not yet started is in this state. </td>
</tr><tr>
  <td bgcolor="#E0E0E0" class="txt">RUNNABLE</td>
  <td bgcolor="#FFFFFF" class="txt"><%=runnable%></td>
  <td bgcolor="#FFFFFF" class="txt">A thread executing in the Java virtual machine is in this state. </td>
</tr><tr>
  <td bgcolor="#E0E0E0" class="txt">BLOCKED</td>
  <td bgcolor="#FFFFFF" class="txt"><%=blocked%></td>
  <td bgcolor="#FFFFFF" class="txt">A thread that is blocked waiting for a monitor lock is in this state. </td>
</tr><tr>
  <td bgcolor="#E0E0E0" class="txt">WAITING</td>
  <td bgcolor="#FFFFFF" class="txt"><%=waiting%></td>
  <td bgcolor="#FFFFFF" class="txt">A thread that is waiting indefinitely for another thread to perform a particular action is in this state. </td>
</tr><tr>
  <td bgcolor="#E0E0E0" class="txt">TIMED_WAITING</td>
  <td bgcolor="#FFFFFF" class="txt"><%=timed_waiting%></td>
  <td bgcolor="#FFFFFF" class="txt">A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state. </td>
</tr><tr>
  <td bgcolor="#E0E0E0" class="txt">TERMINATED</td>
  <td bgcolor="#FFFFFF" class="txt"><%=terminated%></td>
  <td bgcolor="#FFFFFF" class="txt">A thread that has exited is in this state.</td>
</tr>
</table>
<BR>
<%
	if(viewAllThreadInfo) {
		if(lockOwnerSet.size()!=0) {
			out.println("<span class=\"style2\"><span class=\"style5\">*</span> Current Lock Owner List</span><BR>");
			out.println("<TABLE><TR>");
			for(String tempOwnerName:lockOwnerSet)  {
				out.println("<td bgcolor=\"#FFFFFF\" class=\"txt\"><font color=blue><B>&nbsp;"+tempOwnerName+"</B></font>&nbsp;</TD>");
			}
			out.println("</TR></TABLE><BR>");
		}
%>
		
		<span class="style2"><span class="style5">*</span> All Thread infos</span><BR>
<%
		out.println(sb.toString());

	}
%>

<!-------------------------- Data View end -------------------->
	
<Script>
<%
if(viewAllThreadInfo) {
	for(String tempOwnerName:lockOwnerSet)  {
%>
document.getElementById('<%=tempOwnerName%>').className='txtx';
<%
	}
}
%>
</Script>
<BR>
<script>
if(navigator.appName.indexOf("Microsoft") != -1){
  document.write("<button onclick='saveCurrentState()'>Save current state</button><BR>");
}
</script> 
<BR>

<span class="style2">Copyright  Sangmin, Lee.  <a href="http://www.tuning-java.com">http://www.tuning-java.com</a>  All rights Reserved</span>
</BODY>
</HTML>
