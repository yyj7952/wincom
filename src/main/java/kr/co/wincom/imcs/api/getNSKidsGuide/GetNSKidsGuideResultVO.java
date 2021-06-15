package kr.co.wincom.imcs.api.getNSKidsGuide;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsGuideResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = -4462331080251270508L;
	
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
