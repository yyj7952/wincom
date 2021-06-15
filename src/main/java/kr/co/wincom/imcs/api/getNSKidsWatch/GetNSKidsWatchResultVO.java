package kr.co.wincom.imcs.api.getNSKidsWatch;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsWatchResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = 4365941680770503798L;
	
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
