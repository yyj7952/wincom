package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComWatchaVO implements Serializable {

    private String rating01 = "";
    private String rating02 = "";
    private String rating03 = "";
    private String rating04 = "";
    private String rating05 = "";
    private String rating06 = "";
    private String rating07 = "";
    private String rating08 = "";
    private String rating09 = "";
    private String rating10 = "";
    private String totRatingCount = "";
    private String commentCnt = "";
    private String pointWatcha = "";
    private String watchaUrl = "";
    
   
	public String getRating01() {
		return rating01;
	}
	public void setRating01(String rating01) {
		this.rating01 = rating01;
	}
	public String getRating02() {
		return rating02;
	}
	public void setRating02(String rating02) {
		this.rating02 = rating02;
	}
	public String getRating03() {
		return rating03;
	}
	public void setRating03(String rating03) {
		this.rating03 = rating03;
	}
	public String getRating04() {
		return rating04;
	}
	public void setRating04(String rating04) {
		this.rating04 = rating04;
	}
	public String getRating05() {
		return rating05;
	}
	public void setRating05(String rating05) {
		this.rating05 = rating05;
	}
	public String getRating06() {
		return rating06;
	}
	public void setRating06(String rating06) {
		this.rating06 = rating06;
	}
	public String getRating07() {
		return rating07;
	}
	public void setRating07(String rating07) {
		this.rating07 = rating07;
	}
	public String getRating08() {
		return rating08;
	}
	public void setRating08(String rating08) {
		this.rating08 = rating08;
	}
	public String getRating09() {
		return rating09;
	}
	public void setRating09(String rating09) {
		this.rating09 = rating09;
	}
	public String getRating10() {
		return rating10;
	}
	public void setRating10(String rating10) {
		this.rating10 = rating10;
	}
	public String getTotRatingCount() {
		return totRatingCount;
	}
	public void setTotRatingCount(String totRatingCount) {
		this.totRatingCount = totRatingCount;
	}
	public String getCommentCnt() {
		return commentCnt;
	}
	public void setCommentCnt(String commentCnt) {
		this.commentCnt = commentCnt;
	}
	public String getPointWatcha() {
		return pointWatcha;
	}
	public void setPointWatcha(String pointWatcha) {
		this.pointWatcha = pointWatcha;
	}
	public String getWatchaUrl() {
		return watchaUrl;
	}
	public void setWatchaUrl(String watchaUrl) {
		this.watchaUrl = watchaUrl;
	}
	

	public String toWatchaString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.pointWatcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.totRatingCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating01)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating02)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating03)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating04)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating05)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating06)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating07)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating08)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating09)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating10)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.commentCnt)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.watchaUrl)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
	    
}
