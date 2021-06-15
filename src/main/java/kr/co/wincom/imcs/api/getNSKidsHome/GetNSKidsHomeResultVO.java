package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsHomeResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = 4677098116525800899L;
	
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
