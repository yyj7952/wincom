package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComSeriesInfoVO extends NoSqlLoggingVO implements Serializable {

	private String categoryId      = "";
	private String categoryName    = "";
	private String seriesId        = "";
	private String seriesDispName = "";
	private String orderNo         = "";
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	public String getSeriesDispName() {
		return seriesDispName;
	}
	public void setSeriesDispName(String seriesDispName) {
		this.seriesDispName = seriesDispName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
