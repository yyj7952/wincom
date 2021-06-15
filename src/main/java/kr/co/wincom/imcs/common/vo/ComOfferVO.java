package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComOfferVO implements Serializable {
	private String offerSeq		= "";
	private String offerCode	= "";
	private String offerName	= "";
	private String alwnceUnt	= "";
	private String alwnceAmnt	= "";
	private String applStartYmd	= "";
	private String applEndYmd	= "";
	private String alwnceValue	= "";
	
	public String getOfferSeq() {
		return offerSeq;
	}
	public void setOfferSeq(String offerSeq) {
		this.offerSeq = offerSeq;
	}
	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getAlwnceUnt() {
		return alwnceUnt;
	}
	public void setAlwnceUnt(String alwnceUnt) {
		this.alwnceUnt = alwnceUnt;
	}
	public String getAlwnceAmnt() {
		return alwnceAmnt;
	}
	public void setAlwnceAmnt(String alwnceAmnt) {
		this.alwnceAmnt = alwnceAmnt;
	}
	public String getApplStartYmd() {
		return applStartYmd;
	}
	public void setApplStartYmd(String applStartYmd) {
		this.applStartYmd = applStartYmd;
	}
	public String getApplEndYmd() {
		return applEndYmd;
	}
	public void setApplEndYmd(String applEndYmd) {
		this.applEndYmd = applEndYmd;
	}
	public String getAlwnceValue() {
		return alwnceValue;
	}
	public void setAlwnceValue(String alwnceValue) {
		this.alwnceValue = alwnceValue;
	}
	 
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.offerSeq)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.offerCode)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.offerName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.alwnceUnt)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.alwnceAmnt)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.applStartYmd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.applEndYmd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.alwnceValue)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
}
