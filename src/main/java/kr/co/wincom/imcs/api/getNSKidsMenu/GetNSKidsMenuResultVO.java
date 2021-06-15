package kr.co.wincom.imcs.api.getNSKidsMenu;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSKidsMenuResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = 6299262835967959542L;

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
