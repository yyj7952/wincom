package kr.co.wincom.imcs.api.authorizeNView;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class AuthorizeNViewResultVO extends StatVO implements Serializable {

	private List<AuthorizeNViewResponseVO> list;
	private List<AuthorizePlayIpVO> m3u8list;
	private ComCpnVO cpnInfoVO;
	
	public List<AuthorizeNViewResponseVO> getList() {
		return list;
	}

	public void setList(List<AuthorizeNViewResponseVO> list) {
		this.list = list;
	}
	

	public List<AuthorizePlayIpVO> getM3u8list() {
		return m3u8list;
	}

	public void setM3u8list(List<AuthorizePlayIpVO> m3u8list) {
		this.m3u8list = m3u8list;
	}
	
	public ComCpnVO getCpnInfoVO() {
		return cpnInfoVO;
	}

	public void setCpnInfoVO(ComCpnVO cpnInfoVO) {
		this.cpnInfoVO = cpnInfoVO;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(cpnInfoVO == null)	cpnInfoVO = new ComCpnVO();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(AuthorizeNViewResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			record.append(cpnInfoVO.getStmInfo()).append(ImcsConstants.ROWSEP);
			record.append(cpnInfoVO.getCpnInfo()).append(ImcsConstants.ROWSEP);
			record.append(cpnInfoVO.getUseCpnInfo()).append(ImcsConstants.ROWSEP);
			
			if(this.getM3u8list() != null && this.getM3u8list().size() > 0) {
				for(AuthorizePlayIpVO m3u8vo : this.getM3u8list()) {
					if(m3u8vo.getM3u8Type().equals("H") || m3u8vo.getM3u8Type().equals("L")) continue;
					
					record.append(m3u8vo);
					record.append(ImcsConstants.ROWSEP);
				}
			}
			sb.append(record.toString());
		}
		
		
		return sb.toString();
	}

	
}
