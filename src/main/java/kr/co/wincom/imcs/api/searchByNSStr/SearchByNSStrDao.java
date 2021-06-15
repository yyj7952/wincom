package kr.co.wincom.imcs.api.searchByNSStr;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SearchByNSStrDao {
	
	public List<SearchByNSStrResponseVO> searchByNSStrList(SearchByNSStrRequestVO paramVO);
	
	public String getPosterImageFileName(String contsId);
		
}
