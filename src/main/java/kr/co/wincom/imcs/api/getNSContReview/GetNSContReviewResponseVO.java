package kr.co.wincom.imcs.api.getNSContReview;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSContReviewResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType			= "REVW"; //결과값 구분
	private String name					= ""; //댓글 작성자 명
	private String content				= ""; //댓글
	private String rating				= ""; //작성자 평점
	private String writeDt				= ""; //댓글 작성일
	private String reviewFlag			= ""; //리뷰구분
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getContent(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRating(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getWriteDt(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getReviewFlag(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getWriteDt() {
		return writeDt;
	}

	public void setWriteDt(String writeDt) {
		this.writeDt = writeDt;
	}

	public String getReviewFlag() {
		return reviewFlag;
	}

	public void setReviewFlag(String reviewFlag) {
		this.reviewFlag = reviewFlag;
	}
    
    

}
