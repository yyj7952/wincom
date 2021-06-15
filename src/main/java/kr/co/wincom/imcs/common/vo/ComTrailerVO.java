package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ComTrailerVO  implements Serializable {

    private String trailerUrl1 = "";
    private String trailerUrl2 = "";
    private String trailerUrl3 = "";
    private String trailerFileName1 = "";
    private String trailerFileSize1 = "";
    private String contentsId = "";
    private String contentsName = "";
    
	public String getTrailerUrl1() {
		return trailerUrl1;
	}
	public void setTrailerUrl1(String trailerUrl1) {
		this.trailerUrl1 = trailerUrl1;
	}
	public String getTrailerUrl2() {
		return trailerUrl2;
	}
	public void setTrailerUrl2(String trailerUrl2) {
		this.trailerUrl2 = trailerUrl2;
	}
	public String getTrailerUrl3() {
		return trailerUrl3;
	}
	public void setTrailerUrl3(String trailerUrl3) {
		this.trailerUrl3 = trailerUrl3;
	}
	public String getTrailerFileName1() {
		return trailerFileName1;
	}
	public void setTrailerFileName1(String trailerFileName1) {
		this.trailerFileName1 = trailerFileName1;
	}
	public String getTrailerFileSize1() {
		return trailerFileSize1;
	}
	public void setTrailerFileSize1(String trailerFileSize1) {
		this.trailerFileSize1 = trailerFileSize1;
	}
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}
	public String getContentsName() {
		return contentsName;
	}
	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}
    
}
