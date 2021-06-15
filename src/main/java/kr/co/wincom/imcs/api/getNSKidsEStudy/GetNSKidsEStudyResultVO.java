package kr.co.wincom.imcs.api.getNSKidsEStudy;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsEStudyResultVO extends StatVO implements Serializable
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
