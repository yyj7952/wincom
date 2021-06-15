package kr.co.wincom.imcs.api.getNSHighLight;

import java.io.Serializable;

import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;

import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSHighLightResultVO extends StatVO implements Serializable {

	private String flag								= "";
	private String message							= "";
	private String totalCount						= "";
	private List<GetNSHighLightResponseVO> list;
	
	private static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
	    @Override
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getDeclaringClass().equals(StatVO.class);
	    }

	    @Override
	    public boolean shouldSkipClass(Class<?> clazz) {
	        return false;
	    }
	}).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public List<GetNSHighLightResponseVO> getList() {
		return list;
	}
	public void setList(List<GetNSHighLightResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
}
