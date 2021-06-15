package kr.co.wincom.imcs.common.nosql;

import kr.co.wincom.imcs.common.nosql.RedisRequest;

import org.springframework.stereotype.Repository;

@Repository
public interface NoSQLRedisDao {
	
	/**
	 * NoSQL Redis Queue Batch 테이블 데이터 삽입 
	 * @Author : medialog
	 * @Date : 2015. 7. 9. 오전 9:49:47
	 * @Description :
	 * @param request
	 * @return
	 */
	public int insertQueueRedis(RedisRequest request);
	
}
