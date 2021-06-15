package kr.co.wincom.imcs.api.getNSMnuListDtl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


@SuppressWarnings("serial")
public class GetNSMnuListDtlResultVO extends StatVO implements Serializable {
    
	private String rcvBuf = "";
    
    public String getRcvBuf() {return rcvBuf; };
	public void setRcvBuf(String rcvBuf ) { this.rcvBuf = rcvBuf; };
	
	@Override
	public String toString() {
		return rcvBuf.toString();
    }
}


