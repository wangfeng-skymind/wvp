package com.genersoft.iot.vmp.gb28181.bean;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Host")
@ApiModel(description = "Host表")
public class Host {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long host_id;
	private String ip;
	private int port;
	private String address;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
