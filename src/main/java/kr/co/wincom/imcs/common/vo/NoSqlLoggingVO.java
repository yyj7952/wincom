package kr.co.wincom.imcs.common.vo;

public abstract class NoSqlLoggingVO {
	
	/**
	 * NoSQL호출통계 수집
	 */
	private int[] nosqlCacheTypeCnt;
	
	public int[] getNosqlCacheTypeCnt() {
		return nosqlCacheTypeCnt;
	}

	public void setNosqlCacheTypeCnt(int[] nosqlCacheTypeCnt) {
		this.nosqlCacheTypeCnt = nosqlCacheTypeCnt;
	}

}
