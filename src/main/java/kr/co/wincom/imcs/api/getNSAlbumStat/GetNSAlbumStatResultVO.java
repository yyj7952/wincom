package kr.co.wincom.imcs.api.getNSAlbumStat;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSAlbumStatResultVO extends StatVO implements Serializable
{
	private static final long serialVersionUID = -2359946362796239828L;
	
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
