package kr.co.wincom.imcs.api.getNSLastWatch;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSLastWatchResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = -6177931241980278384L;
	
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
