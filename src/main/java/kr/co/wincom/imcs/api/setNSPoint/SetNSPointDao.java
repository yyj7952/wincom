package kr.co.wincom.imcs.api.setNSPoint;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SetNSPointDao {
	
	// 평점 존재 여부 조회
	public List<Integer> pointChk(SetNSPointRequestVO paramVO);
	
	// 평점 정보 수정
	public Integer updatePoint(SetNSPointRequestVO paramVO);
	
	// 평점 정보 등록
	public Integer insertPoint(SetNSPointRequestVO paramVO);
	
	// 앨범 ID조회
	public String getAlbumId(SetNSPointRequestVO paramVO);
	
	// 앨범평점 정보 등록
	public Integer insertAlbPoint(SetNSPointRequestVO paramVO);
	
	// 앨범평점 정보 수정
	public Integer updateAlbPoint(SetNSPointRequestVO paramVO);
		
}
