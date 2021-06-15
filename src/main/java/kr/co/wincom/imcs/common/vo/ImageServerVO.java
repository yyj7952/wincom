package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ImageServerVO  implements Serializable {
	
	private String saId="";
	private String stbMac="";
	private String imageServerIp="";
	private String tImgsvrip1="";
	private String tImgsvrip2="";
	private String tImgsvrip3="";
	private String drImgsvrip="";
	
	public String getImageServerIp() {
		return imageServerIp;
	}

	public void setImageServerIp(String imageServerIp) {
		this.imageServerIp = imageServerIp;
	}

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}

	public String gettImgsvrip1() {
		return tImgsvrip1;
	}

	public void settImgsvrip1(String tImgsvrip1) {
		this.tImgsvrip1 = tImgsvrip1;
	}

	public String gettImgsvrip2() {
		return tImgsvrip2;
	}

	public void settImgsvrip2(String tImgsvrip2) {
		this.tImgsvrip2 = tImgsvrip2;
	}

	public String gettImgsvrip3() {
		return tImgsvrip3;
	}

	public void settImgsvrip3(String tImgsvrip3) {
		this.tImgsvrip3 = tImgsvrip3;
	}

	public String getDrImgsvrip() {
		return drImgsvrip;
	}

	public void setDrImgsvrip(String drImgsvrip) {
		this.drImgsvrip = drImgsvrip;
	}

}
