package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ComCdnVO  implements Serializable {
    String vodServer1		= "";
    String vodServer2		= "";
    String vodServer3		= "";
    String vodFileName		= "";	// 시청타입이D일 경우의 파일명
    String vodFileNameH1	= "";	// CDN_LOCAL_TYPE의 m3u8 파일명(High)
    String vodFileNameH2	= "";	// CDN_NEAR_TYPE의 m3u8 파일명(High)
    String vodFileNameH3	= "";	// CDN_CENTER_TYPE의 m3u8 파일명(High)
    String vodFileNameL1	= "";	// CDN_LOCAL_TYPE의 m3u8 파일명(Low)
    String vodFileNameL2	= "";	// CDN_NEAR_TYPE의 m3u8 파일명(Low)
    String vodFileNameL3	= "";	// CDN_CENTER_TYPE의 m3u8 파일명(Low)
    String vodServer1Type	= "";	// CDN_LOCAL_TYPE
    String vodServer2Type	= "";	// CDN_NEAR_TYPE
    String vodServer3Type	= "";	// CDN_CENTER_TYPE
    String contentFileSize  = "";
    
    
	public String getVodServer1() {
		return vodServer1;
	}
	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}
	public String getVodServer2() {
		return vodServer2;
	}
	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}
	public String getVodServer3() {
		return vodServer3;
	}
	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}
	public String getVodFileNameH1() {
		return vodFileNameH1;
	}
	public void setVodFileNameH1(String vodFileNameH1) {
		this.vodFileNameH1 = vodFileNameH1;
	}
	public String getVodFileNameH2() {
		return vodFileNameH2;
	}
	public void setVodFileNameH2(String vodFileNameH2) {
		this.vodFileNameH2 = vodFileNameH2;
	}
	public String getVodFileNameH3() {
		return vodFileNameH3;
	}
	public void setVodFileNameH3(String vodFileNameH3) {
		this.vodFileNameH3 = vodFileNameH3;
	}
	public String getVodFileNameL1() {
		return vodFileNameL1;
	}
	public void setVodFileNameL1(String vodFileNameL1) {
		this.vodFileNameL1 = vodFileNameL1;
	}
	public String getVodFileNameL2() {
		return vodFileNameL2;
	}
	public void setVodFileNameL2(String vodFileNameL2) {
		this.vodFileNameL2 = vodFileNameL2;
	}
	public String getVodFileNameL3() {
		return vodFileNameL3;
	}
	public void setVodFileNameL3(String vodFileNameL3) {
		this.vodFileNameL3 = vodFileNameL3;
	}
	public String getVodServer1Type() {
		return vodServer1Type;
	}
	public void setVodServer1Type(String vodServer1Type) {
		this.vodServer1Type = vodServer1Type;
	}
	public String getVodServer2Type() {
		return vodServer2Type;
	}
	public void setVodServer2Type(String vodServer2Type) {
		this.vodServer2Type = vodServer2Type;
	}
	public String getVodServer3Type() {
		return vodServer3Type;
	}
	public void setVodServer3Type(String vodServer3Type) {
		this.vodServer3Type = vodServer3Type;
	}
	public String getVodFileName() {
		return vodFileName;
	}
	public void setVodFileName(String vodFileName) {
		this.vodFileName = vodFileName;
	}
	public String getContentFileSize() {
		return contentFileSize;
	}
	public void setContentFileSize(String contentFileSize) {
		this.contentFileSize = contentFileSize;
	}
    
	
}
