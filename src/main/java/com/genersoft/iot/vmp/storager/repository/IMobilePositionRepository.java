package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition;
import com.genersoft.iot.vmp.gb28181.bean.MobilePosition;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author: WangFeng
 * @date: 2026/4/23
 * @description: TODO
 */
@Repository
public interface IMobilePositionRepository extends JpaRepository<MobilePosition,Long>, JpaSpecificationExecutor<MobilePosition> {

    int deleteByDeviceId(String deviceId);

    Optional<MobilePosition> queryTopByDeviceIdOrderByTimeDesc(String deviceId);

    Optional<MobilePosition> queryByDeviceIdOrderByTimeDesc(String deviceId);


    @Query("""
        SELECT m FROM MobilePosition m
        WHERE m.deviceId = :deviceId
        AND (:startTime IS NULL OR m.time >= :startTime)
        AND (:endTime IS NULL OR m.time <= :endTime)
        ORDER BY m.time ASC
    """)
    List<MobilePosition> queryPositionByDeviceIdAndTime(
            @Param("deviceId") String deviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
