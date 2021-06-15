package kr.co.wincom.imcs.api.getNSSVODInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSSVODInfoResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSVODInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String subYn			= "";		// 가입여부
    private String subProdId		= "";		// 상품코드
    private String subProdName		= "";		// 상품명
    private String subProdPrice		= "";		// 상품가격
    private String uCubeProdId		= "";		// U-Cube 상품코드
    private String subProdDesc		= "";		// 소속상품 상세 설명
    
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String catName			= "";	
	private String imcsCreateDate	= "";
    
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.subYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uCubeProdId)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
	
    
	public String getSubYn() {
		return subYn;
	}
	public void setSubYn(String subYn) {
		this.subYn = subYn;
	}
	public String getSubProdId() {
		return subProdId;
	}
	public void setSubProdId(String subProdId) {
		this.subProdId = subProdId;
	}
	public String getSubProdName() {
		return subProdName;
	}
	public void setSubProdName(String subProdName) {
		this.subProdName = subProdName;
	}
	public String getSubProdPrice() {
		return subProdPrice;
	}
	public void setSubProdPrice(String subProdPrice) {
		this.subProdPrice = subProdPrice;
	}
	public String getSubProdDesc() {
		return subProdDesc;
	}
	public void setSubProdDesc(String subProdDesc) {
		this.subProdDesc = subProdDesc;
	}
	public String getuCubeProdId() {
		return uCubeProdId;
	}
	public void setuCubeProdId(String uCubeProdId) {
		this.uCubeProdId = uCubeProdId;
	}


	public String getCatName() {
		return catName;
	}


	public void setCatName(String catName) {
		this.catName = catName;
	}


	public String getImcsCreateDate() {
		return imcsCreateDate;
	}


	public void setImcsCreateDate(String imcsCreateDate) {
		this.imcsCreateDate = imcsCreateDate;
	}
    
	
    
    
    
}
