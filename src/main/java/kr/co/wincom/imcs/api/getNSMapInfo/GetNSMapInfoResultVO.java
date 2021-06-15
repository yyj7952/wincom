package kr.co.wincom.imcs.api.getNSMapInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSMapInfoResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = -1536001177236343037L;
	
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
