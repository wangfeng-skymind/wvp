package com.genersoft.iot.vmp.gb28181.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_remote_definition")
@ApiModel(description = "设备远程表")
@Data
public class DeviceRemoteDefinition {



	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long device_remote_id;



	/**
	 * 设备Id
	 */
	@Column(unique = true)
	private String device_id;

	/**
	 * 设备名
	 */
	private String name;
	
	/**
	 * 生产厂商
	 */
	private String manufacturer;
	
	/**
	 * 型号
	 */
	private String model;
	
	/**
	 * 固件版本
	 */
	private String firmware;

	/**
	 * 传输协议
	 * UDP/TCP
	 */
	private String transport;

	/**
	 * 数据流传输模式
	 * UDP:udp传输
	 * TCP-ACTIVE：tcp主动模式
	 * TCP-PASSIVE：tcp被动模式
	 */
	private String streamMode;
	/**
	 * wan地址_ip
	 */
	private String  local_ip;
	/**
	 * wan地址_ip
	 */
	private String  ip;

	/**
	 * wan地址_port
	 */
	private int port;

	/**
	 * wan地址
	 */
	private String  hostAddress;
	
	/**
	 * 在线
	 */
	private int online;


	/**
	 * 注册时间
	 */
	private Long registerTimeMillis;
	@CreationTimestamp
	@Column(name="create_time")
	private LocalDateTime createTime; // 创建时间
	@UpdateTimestamp
	private LocalDateTime update_time;
	/**
	 * 通道个数
	 */
	private int channelCount;
/*	@OneToMany(fetch = FetchType.EAGER) // 或 FetchType.EAGER 自动加载
	@JoinColumn(name = "device_id",referencedColumnName = "device_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})*/
	@Transient
	private List<DeviceChannelDefinition> channels;


}
