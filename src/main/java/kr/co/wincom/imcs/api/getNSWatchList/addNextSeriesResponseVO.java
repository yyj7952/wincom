package kr.co.wincom.imcs.api.getNSWatchList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class addNextSeriesResponseVO extends NoSqlLoggingVO implements Serializable {
	private String albumId = ""; 
	private String albumName = "";  
	private String seriesDesc = "";
	private String prInfo = "";
	private String runtime = "";
	private String nscreenYn = "";
	private String cpNscreenYn = "";
	private String seriesNo = "";
	private String productType = "";
	private String posterP = "";
	private String stillFileName = "";
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getSeriesDesc() {
		return seriesDesc;
	}
	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getNscreenYn() {
		return nscreenYn;
	}
	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}
	public String getCpNscreenYn() {
		return cpNscreenYn;
	}
	public void setCpNscreenYn(String cpNscreenYn) {
		this.cpNscreenYn = cpNscreenYn;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPosterP() {
		return posterP;
	}
	public void setPosterP(String posterP) {
		this.posterP = posterP;
	}
	public String getStillFileName() {
		return stillFileName;
	}
	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}
	
	
	
}
