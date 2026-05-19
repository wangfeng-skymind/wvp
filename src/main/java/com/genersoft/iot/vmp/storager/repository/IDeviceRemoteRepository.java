package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
  //  Optional<DeviceRemoteDefinition> findByDevice_id(String orderNo);
   // Optional<DeviceRemoteDefinition> getByDevice_id(String orderNo);

 //   int deleteByDevice_id(String deviceId);
 //   List<DeviceRemoteDefinition> findAllByDevice_id(String deviceId);

    @Query("from DeviceRemoteDefinition where device_id = :deviceId")
    Optional<DeviceRemoteDefinition> findByDevice_id( @Param("deviceId") String deviceId);

    @Query("from DeviceRemoteDefinition where device_id = :deviceId")
    Optional<DeviceRemoteDefinition> getByDevice_id( @Param("deviceId") String deviceId);
    @Transactional
    @Modifying
    @Query("delete from DeviceRemoteDefinition where device_id = :deviceId")
    int deleteByDevice_id(@Param("deviceId") String deviceId);
    @Query("from DeviceRemoteDefinition where device_id = :deviceId")
    List<DeviceRemoteDefinition> findAllByDevice_id(  @Param("deviceId") String deviceId);

/*    @Query("""
    SELECT new com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition(
        de,
        (SELECT COUNT(dc) FROM channelCount dc WHERE dc.deviceId = de.deviceId)
    )
    FROM DeviceRemoteDefinition de
    """)
    List<DeviceRemoteDefinition> getDevices();*/
}