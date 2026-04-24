package com.genersoft.iot.vmp.gb28181.bean;


//import gov.nist.javax.sip.header.SIPDate;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

/**    
 * @Description:设备录像信息bean 
 * @author: swwheihei
 * @date:   2020年5月8日 下午2:05:56     
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "record_info")
@ApiModel(description = "record_info表")
public class RecordInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long record_info_id;
	private String deviceId;
	
	private String name;
	
	private int sumNum;

	@Transient
	private List<RecordItem> recordList;

/*
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSumNum() {
		return sumNum;
	}

	public void setSumNum(int sumNum) {
		this.sumNum = sumNum;
	}

	public List<RecordItem> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RecordItem> recordList) {
		this.recordList = recordList;
	}
*/

}
