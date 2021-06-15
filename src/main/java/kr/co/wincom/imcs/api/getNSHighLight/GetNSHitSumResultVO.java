package kr.co.wincom.imcs.api.getNSHighLight;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetNSHitSumResultVO implements Serializable {

	private String album_id = "";
	private String scene_type = "";
	private String tot_cnt = "";
	
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getScene_type() {
		return scene_type;
	}
	public void setScene_type(String scene_type) {
		this.scene_type = scene_type;
	}
	public String getTot_cnt() {
		return tot_cnt;
	}
	public void setTot_cnt(String tot_cnt) {
		this.tot_cnt = tot_cnt;
	}

}
