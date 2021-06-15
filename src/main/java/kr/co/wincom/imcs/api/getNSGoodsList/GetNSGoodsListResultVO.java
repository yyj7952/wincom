package kr.co.wincom.imcs.api.getNSGoodsList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSGoodsListResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = 1671060510460311878L;
	
	private String result = "";

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString()
	{
		return this.result;
	}
}
