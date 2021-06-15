package kr.co.wincom.imcs.api.getNSKidsList;

import java.io.Serializable;

public class MyKidsVO implements Serializable
{
	private static final long serialVersionUID = 1914656807451995661L;
	
	private String catJungBokYn = "N";
	private String resultJungBokYn = "N";
	private int idx = -1;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getCatJungBokYn() {
		return catJungBokYn;
	}
	public void setCatJungBokYn(String catJungBokYn) {
		this.catJungBokYn = catJungBokYn;
	}
	public String getResultJungBokYn() {
		return resultJungBokYn;
	}
	public void setResultJungBokYn(String resultJungBokYn) {
		this.resultJungBokYn = resultJungBokYn;
	}
	
}
