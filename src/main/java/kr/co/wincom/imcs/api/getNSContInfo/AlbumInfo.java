package kr.co.wincom.imcs.api.getNSContInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

public class AlbumInfo extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;
	
	private String resultType        = "";
	private String albumId           = "";
	private String albumName         = "";
	private String seriesNo          = "";
	private String onairDateTemp     = "";
	private String onairDate         = "";
	private String onairDateDisplay  = "";
	private String releaseDate       = "";
	private String realHd            = "";
	private String runtime           = "";
	private String prInfo            = "";
	private String genreLarge        = "";
	private String genreMid          = "";
	private String genreSmall        = "";
	private String genreInfo         = "";
	private String terrCh            = "";
	private String terrEdDate        = "";
	private String producer          = "";
	private String actorsDisplay     = "";
	private String serviceIcon       = "";
	private String serviceIconHdtv   = "";
	private String serviceIconUflix  = "";
	private String mycutYn           = "";
	private String datafreeBillYn    = "";
	private String smiLanguage       = "";
	private String cpProperty        = "";
	private String cpPropertyUfx    = "";
	private String synopsis          = "";
	private String watchaPoint       = "";
	private String watchaCount       = "";
	private String watchaComment     = "";
	private String commentCnt        = "";
	private String musicType         = "";
	private String omniviewYn        = "";
	private String presentYn         = "";
	private String presentPrice      = "";
	private String is51ch            = "";
	private String serviceFlag       = "";
	private String serviceDate       = "";
	private String previewYn         = "";
	private String prAlbumId         = "";
	private String linkUrl           = "";
	private String seasonYn          = "";
	private String downYn            = "";
	private String flag              = "";
	private String posterP           = "";
	private String subTitle            = "";
	private String year                = "";
	private String nfcCode             = "";
	private String player              = "";
	private String studio              = "";
	private String vodType             = "";
	private String vrType              = "";
	private String genreUxten          = "";
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
	
	private String payFlag				= "N";
	
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
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getOnairDateTemp() {
		return onairDateTemp;
	}
	public void setOnairDateTemp(String onairDateTemp) {
		this.onairDateTemp = onairDateTemp;
	}
	public String getOnairDate() {
		return onairDate;
	}
	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}
	
	public String getOnairDateDisplay() {
		return onairDateDisplay;
	}
	public void setOnairDateDisplay(String onairDateDisplay) {
		this.onairDateDisplay = onairDateDisplay;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getRealHd() {
		return realHd;
	}
	public void setRealHd(String realHd) {
		this.realHd = realHd;
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
	public String getGenreSmall() {
		return genreSmall;
	}
	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}
	public String getGenreInfo() {
		return genreInfo;
	}
	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
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
	public String getMycutYn() {
		return mycutYn;
	}
	public void setMycutYn(String mycutYn) {
		this.mycutYn = mycutYn;
	}
	public String getDatafreeBillYn() {
		return datafreeBillYn;
	}
	public void setDatafreeBillYn(String datafreeBillYn) {
		this.datafreeBillYn = datafreeBillYn;
	}
	public String getSmiLanguage() {
		return smiLanguage;
	}
	public void setSmiLanguage(String smiLanguage) {
		this.smiLanguage = smiLanguage;
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
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getWatchaPoint() {
		return watchaPoint;
	}
	public void setWatchaPoint(String watchaPoint) {
		this.watchaPoint = watchaPoint;
	}
	public String getWatchaCount() {
		return watchaCount;
	}
	public void setWatchaCount(String watchaCount) {
		this.watchaCount = watchaCount;
	}
	public String getWatchaComment() {
		return watchaComment;
	}
	public void setWatchaComment(String watchaComment) {
		this.watchaComment = watchaComment;
	}
	public String getMusicType() {
		return musicType;
	}
	public void setMusicType(String musicType) {
		this.musicType = musicType;
	}
	public String getOmniviewYn() {
		return omniviewYn;
	}
	public void setOmniviewYn(String omniviewYn) {
		this.omniviewYn = omniviewYn;
	}
	public String getCommentCnt() {
		return commentCnt;
	}
	public void setCommentCnt(String commentCnt) {
		this.commentCnt = commentCnt;
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
	public String getIs51ch() {
		return is51ch;
	}
	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
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
	public String getPreviewYn() {
		return previewYn;
	}
	public void setPreviewYn(String previewYn) {
		this.previewYn = previewYn;
	}
	public String getPrAlbumId() {
		return prAlbumId;
	}
	public void setPrAlbumId(String prAlbumId) {
		this.prAlbumId = prAlbumId;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getSeasonYn() {
		return seasonYn;
	}
	public void setSeasonYn(String seasonYn) {
		this.seasonYn = seasonYn;
	}
	public String getDownYn() {
		return downYn;
	}
	public void setDownYn(String downYn) {
		this.downYn = downYn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPosterP() {
		return posterP;
	}
	public void setPosterP(String posterP) {
		this.posterP = posterP;
	}
	public String getVodType() {
		return vodType;
	}
	public void setVodType(String vodType) {
		this.vodType = vodType;
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
	public String getVrType() {
		return vrType;
	}
	public void setVrType(String vrType) {
		this.vrType = vrType;
	}
	public String getGenreUxten() {
		return genreUxten;
	}
	public void setGenreUxten(String genreUxten) {
		this.genreUxten = genreUxten;
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
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	
	
}
