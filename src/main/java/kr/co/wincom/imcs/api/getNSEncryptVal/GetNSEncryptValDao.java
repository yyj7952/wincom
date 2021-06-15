package kr.co.wincom.imcs.api.getNSEncryptVal;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSEncryptValDao
{
	
	// 시청 여부 조회
	public List<String> getEnctyptKey();

}
