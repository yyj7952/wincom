package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class M3u8ProfileVO  implements Serializable {
    private int orderNum;
    private String castisM3u8			= "";		// 캐스트이즈 m3u8 파일명
    private String onnuriM3u8			= "";		// 온누리넷 m3u8 파일명
	private String m3u8Type             = "";
	private String nodeGroup            = "";
	
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
	
}
