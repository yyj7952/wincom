package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class M3u8AWSProfileVO  implements Serializable {
    private int orderNum;
    private String castisM3u8			= "";		// 캐스트이즈 m3u8 파일명
    private String onnuriM3u8			= "";		// 온누리넷 m3u8 파일명
	private String m3u8Type             = "";
	private String nodeGroup            = "";
	private String aws_Filename1        = "";
	private String aws_Filename2        = "";
	private String aws_Filename3        = "";
	private String awsServerPlayIp1     = "";
	private String awsServerPlayIp2     = "";
	private String awsServerPlayIp3     = "";
	private String distrFlag            = "";
	private String serviceYn            = "";
	private String awsServiceYn         = "";
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getCastisM3u8() {
		return castisM3u8;
	}
	public void setCastisM3u8(String castisM3u8) {
		this.castisM3u8 = castisM3u8;
	}
	public String getOnnuriM3u8() {
		return onnuriM3u8;
	}
	public void setOnnuriM3u8(String onnuriM3u8) {
		this.onnuriM3u8 = onnuriM3u8;
	}
	public String getM3u8Type() {
		return m3u8Type;
	}
	public void setM3u8Type(String m3u8Type) {
		this.m3u8Type = m3u8Type;
	}
	public String getNodeGroup() {
		return nodeGroup;
	}
	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}
	public String getAws_Filename1() {
		return aws_Filename1;
	}
	public void setAws_Filename1(String aws_Filename1) {
		this.aws_Filename1 = aws_Filename1;
	}
	public String getAws_Filename2() {
		return aws_Filename2;
	}
	public void setAws_Filename2(String aws_Filename2) {
		this.aws_Filename2 = aws_Filename2;
	}
	public String getAws_Filename3() {
		return aws_Filename3;
	}
	public void setAws_Filename3(String aws_Filename3) {
		this.aws_Filename3 = aws_Filename3;
	}
	public String getAwsServerPlayIp1() {
		return awsServerPlayIp1;
	}
	public void setAwsServerPlayIp1(String awsServerPlayIp1) {
		this.awsServerPlayIp1 = awsServerPlayIp1;
	}
	public String getAwsServerPlayIp2() {
		return awsServerPlayIp2;
	}
	public void setAwsServerPlayIp2(String awsServerPlayIp2) {
		this.awsServerPlayIp2 = awsServerPlayIp2;
	}
	public String getAwsServerPlayIp3() {
		return awsServerPlayIp3;
	}
	public void setAwsServerPlayIp3(String awsServerPlayIp3) {
		this.awsServerPlayIp3 = awsServerPlayIp3;
	}
	public String getDistrFlag() {
		return distrFlag;
	}
	public void setDistrFlag(String distrFlag) {
		this.distrFlag = distrFlag;
	}
	public String getServiceYn() {
		return serviceYn;
	}
	public void setServiceYn(String serviceYn) {
		this.serviceYn = serviceYn;
	}
	public String getAwsServiceYn() {
		return awsServiceYn;
	}
	public void setAwsServiceYn(String awsServiceYn) {
		this.awsServiceYn = awsServiceYn;
	}
}
