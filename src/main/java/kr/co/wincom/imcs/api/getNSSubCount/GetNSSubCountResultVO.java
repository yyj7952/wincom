package kr.co.wincom.imcs.api.getNSSubCount;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSSubCountResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = -218615691135448639L;
	
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
