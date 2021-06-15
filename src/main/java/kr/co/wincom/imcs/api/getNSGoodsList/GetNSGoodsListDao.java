package kr.co.wincom.imcs.api.getNSGoodsList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSGoodsListDao
{
	public HashMap<String, String> getUserInfo(GetNSGoodsListRequestVO paramVO);
	
	public List<GoodsListReqTypeCat_VO> listReqTypeCategory(GetNSGoodsListRequestVO paramVO);
	
	public List<GoodsListReqTypeCat_VO> listReqTypeGoodsId(GetNSGoodsListRequestVO paramVO);
	
	public List<GoodsListReqTypeCat_VO> listRelaAlbums(GetNSGoodsListRequestVO paramVO);
	
	public List<GoodsListReqTypeCat_VO> listImagesRequestType_0(GetNSGoodsListRequestVO paramVO);
	
	public List<GoodsListReqTypeCat_VO> listImagesRequestType_1(GetNSGoodsListRequestVO paramVO);
}
