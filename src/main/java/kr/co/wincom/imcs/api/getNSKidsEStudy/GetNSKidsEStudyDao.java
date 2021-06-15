package kr.co.wincom.imcs.api.getNSKidsEStudy;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsEStudyDao
{
	public List<HashMap<String, String>> listLevelReadPersent(GetNSKidsEStudyRequestVO paramVO);
	
	public List<HashMap<String, String>> listCategoryReadPersent(GetNSKidsEStudyRequestVO paramVO);
	
	public List<HashMap<String, String>> listSuggestLevel(GetNSKidsEStudyRequestVO paramVO);
}
