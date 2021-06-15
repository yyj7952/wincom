package kr.co.wincom.imcs.api.getNSMapInfo;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMapInfoDao
{
	public ArrayList<HashMap<String, String>> listMultiAlbumInfo(GetNSMapInfoRequestVO paramVO);
}
