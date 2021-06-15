package kr.co.wincom.imcs.api.authorizeVView;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class AuthorizeVViewResultVO extends StatVO implements Serializable {

	private List<AuthorizeVViewResponseVO> list;
	private List<AuthorizePlayIpVO> m3u8list;
	
	public List<AuthorizeVViewResponseVO> getList() {
		return list;
	}

	public void setList(List<AuthorizeVViewResponseVO> list) {
		this.list = list;
	}

	public List<AuthorizePlayIpVO> getM3u8list() {
		return m3u8list;
	}

	public void setM3u8list(List<AuthorizePlayIpVO> m3u8list) {
		this.m3u8list = m3u8list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(AuthorizeVViewResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			for(AuthorizePlayIpVO m3u8vo : this.getM3u8list()) {
				if(m3u8vo.getM3u8Type().equals("H") || m3u8vo.getM3u8Type().equals("L")) continue;
					
				record.append(m3u8vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
