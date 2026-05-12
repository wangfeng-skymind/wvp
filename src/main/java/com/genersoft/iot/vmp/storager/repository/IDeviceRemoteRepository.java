package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: WangFeng
 * @date: 2026/4/23
 * @description: TODO
 */
@Repository
public interface IDeviceRemoteRepository extends JpaRepository<DeviceRemoteDefinition,Long>, JpaSpecificationExecutor<DeviceRemoteDefinition> {

    // 根据订单号查询
    Optional<DeviceRemoteDefinition> findByDeviceId(String orderNo);
    Optional<DeviceRemoteDefinition> getByDeviceId(String orderNo);

    int deleteByDeviceId(String deviceId);
    List<DeviceRemoteDefinition> findAllByDeviceId(String deviceId);



/*    @Query("""
    SELECT new com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition(
        de,
        (SELECT COUNT(dc) FROM channelCount dc WHERE dc.deviceId = de.deviceId)
    )
    FROM DeviceRemoteDefinition de
    """)
    List<DeviceRemoteDefinition> getDevices();*/
}