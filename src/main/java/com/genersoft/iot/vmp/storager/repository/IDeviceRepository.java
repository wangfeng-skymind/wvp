package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: WangFeng
 * @date: 2026/4/23
 * @description: TODO
 */
@Repository
public interface IDeviceRepository extends JpaRepository<Device,Long>, JpaSpecificationExecutor<Device> {

    // 根据订单号查询
    Optional<Device> findByDeviceId(String orderNo);
    Optional<Device> getByDeviceId(String orderNo);

    int deleteByDeviceId(String deviceId);
    List<Device> findAllByDeviceId(String deviceId);



/*    @Query("""
    SELECT new com.genersoft.iot.vmp.gb28181.bean.Device(
        de,
        (SELECT COUNT(dc) FROM channelCount dc WHERE dc.deviceId = de.deviceId)
    )
    FROM Device de
    """)
    List<Device> getDevices();*/
}