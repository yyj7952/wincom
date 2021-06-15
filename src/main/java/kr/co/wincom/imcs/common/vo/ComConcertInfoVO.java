package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComConcertInfoVO implements Serializable
{
	
    String resultType 		= "LIVE";
    String concertDate 		= "";
    String concertBgnTime 	= "";
    String concertEndTime 	= "";
    String guideText 		= "";
    
	String cpnCancelUrl		= "";
	
	String performDate		= "";
  	String performTime		= "";
  	String performEndTime	= "";
  	String performEndDate	= "";
	
    
	public String getCpnCancelUrl() {
		return cpnCancelUrl;
	}


	public void setCpnCancelUrl(String cpnCancelUrl) {
		this.cpnCancelUrl = cpnCancelUrl;
	}


	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getConcertDate() {
		return concertDate;
	}


	public void setConcertDate(String concertDate) {
		this.concertDate = concertDate;
	}


	public String getConcertBgnTime() {
		return concertBgnTime;
	}


	public void setConcertBgnTime(String concertBgnTime) {
		this.concertBgnTime = concertBgnTime;
	}


	public String getConcertEndTime() {
		return concertEndTime;
	}


	public void setConcertEndTime(String concertEndTime) {
		this.concertEndTime = concertEndTime;
	}


	public String getGuideText() {
		return guideText;
	}


	public void setGuideText(String guideText) {
		this.guideText = guideText;
	}


	public String getPerformDate() {
		return performDate;
	}


	public void setPerformDate(String performDate) {
		this.performDate = performDate;
	}


	public String getPerformTime() {
		return performTime;
	}


	public void setPerformTime(String performTime) {
		this.performTime = performTime;
	}


	public String getPerformEndTime() {
		return performEndTime;
	}


	public void setPerformEndTime(String performEndTime) {
		this.performEndTime = performEndTime;
	}


	public String getPerformEndDate() {
		return performEndDate;
	}


	public void setPerformEndDate(String performEndDate) {
		this.performEndDate = performEndDate;
	}
	
	
}
