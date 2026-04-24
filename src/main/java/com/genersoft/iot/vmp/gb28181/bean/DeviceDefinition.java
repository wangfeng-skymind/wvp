package com.genersoft.iot.vmp.gb28181.bean;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wangfeng
 * @date 2025/5/18
 * @description TODO
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "device_definition")
@ApiModel(description = "设备表")
public class DeviceDefinition  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long device_id;
    @Column(length = 100)
    private String device_name;
    private Long tenant_id;
    private String tenant_id_code;;

    private Long shop_id;
    private String shop_name;

    @ApiModelProperty(name = "序列号")
    @Column(length = 100,unique = true) // 设置字段唯一
    private String code;  // 设备编号
    @ApiModelProperty(name = "通道号")
    private String channel;
    @ApiModelProperty(name = "接入口")
    private String access_port;
    @ApiModelProperty(name = "nvr序列号")
    private String nvr_sn;
    @ApiModelProperty(name = "坐标号[左上角，右下角];coords=[{id:'',xy:''},{id:'',xy:[1,2,3,4]}]")
    @Column(length = 2048)
    private String coords;


    @Column( length = 45)
    private String ip_address;
    private Long install_location_dictionary_id;
    private Integer install_location;
    private String install_location_desc;
    @ApiModelProperty(name = "摄像头用途")
    private Long usage_dictionary_id;
    private Integer usage_type;
    private String usage_type_desc;

    private Long type_dictionary_id;
    private Integer type;
    private String type_desc;
    private Integer type_count;
    @ApiModelProperty(name = "设备描述")
    private String description;
    private String images;
    private String label_num;

    @Transient
    private String streamId;
    @Transient
    private String startTime;
    @Transient
    private String endTime;
    @CreationTimestamp
    @Column(name="create_time")
    private LocalDateTime createTime;//create_time;  // 创建时间
    @UpdateTimestamp
    private LocalDateTime update_time;

    private Integer status;
    private String status_desc;
    private String TEST_wf;

}
