package kr.co.wincom.imcs.common.nosql;

/**
 * 
 * <pre>
 * kr.co.wincom.curation.client.model.request
 *	|_ RedisRequest.java
 * </pre>
 *
 * @Company : Medialog	
 * @Author : medialog
 * @Date : 2015. 7. 9. 오전 10:01:30
 * @Description : NoSQL Redis Queue 데이터 
 *
 */
public class RedisRequest {
	private String table_name;
	private String pk_values;
	private String ver_unit;

	public String getVer_unit() {
		return ver_unit;
	}

	public void setVer_unit(String ver_unit) {
		this.ver_unit = ver_unit;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getPk_values() {
		return pk_values;
	}

	public void setPk_values(String pk_values) {
		this.pk_values = pk_values;
	}

	public RedisRequest(String table_name, String ver_unit, String pk_values) {
		super();
		this.table_name = table_name;
		this.ver_unit = ver_unit;
		this.pk_values = pk_values;
	}

	@Override
	public String toString() {
		return "{'table_name':'" + table_name + "','pk_values':'" + pk_values + "','ver_unit':'" + ver_unit + "'}";
	}

}
