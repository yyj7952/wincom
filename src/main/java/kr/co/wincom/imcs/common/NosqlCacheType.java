package kr.co.wincom.imcs.common;

public enum NosqlCacheType {
	REDIS(0),
	HBASE(1),
	USERDB(2),
	REDIS_DB(3),
	HBASE_DB(4),
	HBASE_WR(5);

	int code;
	
	NosqlCacheType(int resourceCode) {
		this.code = resourceCode;
	}
	
	public int getResourceCode() {
		return code;
	}
}
