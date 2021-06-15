package kr.co.wincom.imcs.api.getNSKidsList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsListResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = 2716572225701333903L;
	
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
