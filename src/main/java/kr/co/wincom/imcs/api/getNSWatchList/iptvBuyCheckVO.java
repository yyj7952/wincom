package kr.co.wincom.imcs.api.getNSWatchList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class iptvBuyCheckVO extends NoSqlLoggingVO implements Serializable {

	private String buyYn   = "";
	private String buyDate = "";
	private String expiredDate = "";
	private String subscYn = "";

	public String getBuyYn() {
		return buyYn;
	}
	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getSubscYn() {
		return subscYn;
	}
	public void setSubscYn(String subscYn) {
		this.subscYn = subscYn;
	}
	
}
