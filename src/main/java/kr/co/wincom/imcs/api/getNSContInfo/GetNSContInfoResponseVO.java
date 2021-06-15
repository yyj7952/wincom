package kr.co.wincom.imcs.api.getNSContInfo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSContInfoResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSContInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType		   = "ALB"; //결과값 구분
	private String albumId             = ""; //앨범 ID
	private String albumName           = ""; //앨범 제목
	private String seriesNo            = "";
	private String onairDate           = ""; //방영일/ 공연일시
	private String releaseDate         = ""; //개봉일
	private String rearHd              = ""; //Real HD (HEVC) 여부
	private String runtime             = ""; //상영시간 (hhmmss)
	private String prInfo              = ""; //나이제한
	private String genreLarge          = ""; //장르(1레벨)
	private String genreMid            = ""; //장르(2레벨)
	private String genreSmall          = ""; //장르(3레벨)
	private String genreUxten          = "";
	private String genreInfo           = ""; //대장르 카테고리명
	private String terrCh              = ""; //지상파채널
	private String terrEdDate          = ""; //유료종료일
	private String producer            = "";
	private String actorsDisplay       = "";
	private String serviceIcon         = "";
	private String serviceIconHdtv     = "";
	private String serviceIconUflix    = "";
	private String mycutYn             = ""; //MY CUT 가능여부
	private String datafreeBillFlag    = ""; //데이터Free 유/무료 여부
	private String smiLanguage         = ""; //자막에 대한 제공언어
	private String cpProperty          = ""; //CP별 속성값
	private String cpPropertyUfx       = ""; //UFLIX CP별 속성값
	private String synopsis            = ""; //영화해설(시놉시스)
	private String pointWatcha         = ""; //평점(왓챠)
	private String pointCntWatcha      = ""; //별점 평가자 수
	private String commentCnt          = ""; //왓챠별점 댓글 개수
	private String musicAlbumType      = ""; //콘서트영상, 숏클립 구분
	private String omniviewYn          = ""; //옴니뷰 서비스 가능 여부
	private String presentYn           = ""; //선물가능여부
	private String presentPrice        = ""; //선물가격정보
	private String is51Ch              = "";
	private String serviceFlag         = "";
	private String serviceDate         = "";
	private String previewYn           = ""; //예고편 여부
	private String prevAlbumId         = ""; //예고편 앨범ID
	private String linkUrl             = ""; //콘서트 링크 URL
	private String vrType              = "";
	private String seasonYn            = ""; //시즌 여부
	private String downloadYn          = ""; //다운로드여부
	
	private String isNew               = ""; //신규 카테고리 등록 여부
	private String isUpdate            = ""; //카테고리의 등록업데이트 여부 콘텐츠 일경우 N
	private String director            = ""; //감독
	private String actor               = ""; //출연
	private String seriesDesc          = ""; //회차 설명
	private String reservedDate        = ""; //일반구매 전환일
	private String serviceGb           = ""; //서비스 구분 (service_icon)
	private String imgUrl              = ""; //포스터 URL
	private String imgFileName         = ""; //포스터 이미지 파일명
	private String stillUrl            = ""; //스틸이미지 URL
	private String stillFileName       = ""; //스틸이미지 파일명
	private String point               = ""; //U+ 별점
	private String addImgUrl           = ""; //추가 이미지 URL
	private String addImgFileName      = ""; //추가 이미지 파일명
	private String qsheetYn            = ""; //큐시트 정보 여부
	private String musicOnairStatus    = ""; //공연 전/중/후 정보
	
	//golf2.0 추가 응답 파라미터
	private String subTitle            = "";
	private String year                = "";
	private String nfcCode             = "";
	private String player              = "";
	private String studio              = "";
	private String vodType             = "";
	// 2019.06.07 5G 2차 고음질 어셋 추가
	private String hqAudioYn            = "";
	// 2019.08.20 - 모바일tv 기능개선
	private String promotionCopy        = "";
	
	// 2020.03.25 - 모바일 아이들나라
	private String kidsGrade			= "";
	private String cineId				= "";
	private String cine_avg_point		= "";
	private String cine_count			= "";
	private String directorDisplay			= "";
	
	// 2020.04.29 - 아이돌 라이브 2020 1Q 속성 정보 제공 (뱃지)
	private String mainGrpType			= "";
	private String subGrpType			= "";
	
	// 2021.04.07 - 아이돌 라이브 유료콘서트
	private String livePpvYn			= "N";
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRuntime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPrInfo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSynopsis(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIsNew(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIsUpdate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProducer(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getActor(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOnairDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getReleaseDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesDesc(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getTerrCh(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getTerrEdDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getReservedDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getServiceGb(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPreviewYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPrevAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getGenreLarge(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getGenreMid(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getGenreInfo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSmiLanguage(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getServiceFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPointWatcha(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPointCntWatcha(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPoint(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCommentCnt(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCpProperty(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCpPropertyUfx(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRearHd(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPresentYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPresentPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getDownloadYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMycutYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getDatafreeBillFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeasonYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAddImgUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAddImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getQsheetYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMusicAlbumType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMusicOnairStatus(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOmniviewYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLinkUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVrType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getGenreUxten(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubTitle(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getYear(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNfcCode(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPlayer(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStudio(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getHqAudioYn(), "N")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPromotionCopy(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getKidsGrade(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCineId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCine_avg_point(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCine_count(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMainGrpType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubGrpType().replaceAll("\\\\b", ImcsConstants.ARRSEP), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getDirectorDisplay(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLivePpvYn(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getOnairDate() {
		return onairDate;
	}

	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSeriesDesc() {
		return seriesDesc;
	}

	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}

	public String getTerrCh() {
		return terrCh;
	}

	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}

	public String getTerrEdDate() {
		return terrEdDate;
	}

	public void setTerrEdDate(String terrEdDate) {
		this.terrEdDate = terrEdDate;
	}

	public String getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}

	public String getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getStillUrl() {
		return stillUrl;
	}

	public void setStillUrl(String stillUrl) {
		this.stillUrl = stillUrl;
	}

	public String getStillFileName() {
		return stillFileName;
	}

	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getPreviewYn() {
		return previewYn;
	}

	public void setPreviewYn(String previewYn) {
		this.previewYn = previewYn;
	}

	public String getPrevAlbumId() {
		return prevAlbumId;
	}

	public void setPrevAlbumId(String prevAlbumId) {
		this.prevAlbumId = prevAlbumId;
	}

	public String getGenreLarge() {
		return genreLarge;
	}

	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}

	public String getGenreMid() {
		return genreMid;
	}

	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getSmiLanguage() {
		return smiLanguage;
	}

	public void setSmiLanguage(String smiLanguage) {
		this.smiLanguage = smiLanguage;
	}

	public String getPointWatcha() {
		return pointWatcha;
	}

	public void setPointWatcha(String pointWatcha) {
		this.pointWatcha = pointWatcha;
	}

	public String getPointCntWatcha() {
		return pointCntWatcha;
	}

	public void setPointCntWatcha(String pointCntWatcha) {
		this.pointCntWatcha = pointCntWatcha;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getCommentCnt() {
		return commentCnt;
	}

	public void setCommentCnt(String commentCnt) {
		this.commentCnt = commentCnt;
	}

	public String getCpProperty() {
		return cpProperty;
	}

	public void setCpProperty(String cpProperty) {
		this.cpProperty = cpProperty;
	}

	public String getCpPropertyUfx() {
		return cpPropertyUfx;
	}

	public void setCpPropertyUfx(String cpPropertyUfx) {
		this.cpPropertyUfx = cpPropertyUfx;
	}

	public String getRearHd() {
		return rearHd;
	}

	public void setRearHd(String rearHd) {
		this.rearHd = rearHd;
	}

	public String getPresentYn() {
		return presentYn;
	}

	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	public String getDownloadYn() {
		return downloadYn;
	}

	public void setDownloadYn(String downloadYn) {
		this.downloadYn = downloadYn;
	}

	public String getMycutYn() {
		return mycutYn;
	}

	public void setMycutYn(String mycutYn) {
		this.mycutYn = mycutYn;
	}

	public String getDatafreeBillFlag() {
		return datafreeBillFlag;
	}

	public void setDatafreeBillFlag(String datafreeBillFlag) {
		this.datafreeBillFlag = datafreeBillFlag;
	}

	public String getSeasonYn() {
		return seasonYn;
	}

	public void setSeasonYn(String seasonYn) {
		this.seasonYn = seasonYn;
	}

	public String getAddImgUrl() {
		return addImgUrl;
	}

	public void setAddImgUrl(String addImgUrl) {
		this.addImgUrl = addImgUrl;
	}

	public String getAddImgFileName() {
		return addImgFileName;
	}

	public void setAddImgFileName(String addImgFileName) {
		this.addImgFileName = addImgFileName;
	}

	public String getQsheetYn() {
		return qsheetYn;
	}

	public void setQsheetYn(String qsheetYn) {
		this.qsheetYn = qsheetYn;
	}

	public String getMusicAlbumType() {
		return musicAlbumType;
	}

	public void setMusicAlbumType(String musicAlbumType) {
		this.musicAlbumType = musicAlbumType;
	}

	public String getMusicOnairStatus() {
		return musicOnairStatus;
	}

	public void setMusicOnairStatus(String musicOnairStatus) {
		this.musicOnairStatus = musicOnairStatus;
	}

	public String getOmniviewYn() {
		return omniviewYn;
	}

	public void setOmniviewYn(String omniviewYn) {
		this.omniviewYn = omniviewYn;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getGenreSmall() {
		return genreSmall;
	}

	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}

	public String getGenreUxten() {
		return genreUxten;
	}

	public void setGenreUxten(String genreUxten) {
		this.genreUxten = genreUxten;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getActorsDisplay() {
		return actorsDisplay;
	}

	public void setActorsDisplay(String actorsDisplay) {
		this.actorsDisplay = actorsDisplay;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public String getServiceIconHdtv() {
		return serviceIconHdtv;
	}

	public void setServiceIconHdtv(String serviceIconHdtv) {
		this.serviceIconHdtv = serviceIconHdtv;
	}

	public String getServiceIconUflix() {
		return serviceIconUflix;
	}

	public void setServiceIconUflix(String serviceIconUflix) {
		this.serviceIconUflix = serviceIconUflix;
	}

	public String getIs51Ch() {
		return is51Ch;
	}

	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
	}

	public String getServiceFlag() {
		return serviceFlag;
	}

	public void setServiceFlag(String serviceFlag) {
		this.serviceFlag = serviceFlag;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getVrType() {
		return vrType;
	}

	public void setVrType(String vrType) {
		this.vrType = vrType;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getNfcCode() {
		return nfcCode;
	}

	public void setNfcCode(String nfcCode) {
		this.nfcCode = nfcCode;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getVodType() {
		return vodType;
	}

	public void setVodType(String vodType) {
		this.vodType = vodType;
	}

	public String getHqAudioYn() {
		return hqAudioYn;
	}

	public void setHqAudioYn(String hqAudioYn) {
		this.hqAudioYn = hqAudioYn;
	}

	public String getPromotionCopy() {
		return promotionCopy;
	}

	public void setPromotionCopy(String promotionCopy) {
		this.promotionCopy = promotionCopy;
	}
	public String getKidsGrade() {
		return kidsGrade;
	}
	public void setKidsGrade(String kidsGrade) {
		this.kidsGrade = kidsGrade;
	}
	public String getCineId() {
		return cineId;
	}
	public void setCineId(String cineId) {
		this.cineId = cineId;
	}
	public String getCine_avg_point() {
		return cine_avg_point;
	}
	public void setCine_avg_point(String cine_avg_point) {
		this.cine_avg_point = cine_avg_point;
	}
	public String getCine_count() {
		return cine_count;
	}
	public void setCine_count(String cine_count) {
		this.cine_count = cine_count;
	}

	public String getDirectorDisplay() {
		return directorDisplay;
	}

	public void setDirectorDisplay(String directorDisplay) {
		this.directorDisplay = directorDisplay;
	}

	public String getMainGrpType() {
		return mainGrpType;
	}

	public void setMainGrpType(String mainGrpType) {
		this.mainGrpType = mainGrpType;
	}

	public String getSubGrpType() {
		return subGrpType;
	}

	public void setSubGrpType(String subGrpType) {
		this.subGrpType = subGrpType;
	}

	public String getLivePpvYn() {
		return livePpvYn;
	}

	public void setLivePpvYn(String livePpvYn) {
		this.livePpvYn = livePpvYn;
	}
	
}
