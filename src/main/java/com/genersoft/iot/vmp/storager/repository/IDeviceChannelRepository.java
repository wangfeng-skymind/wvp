package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import org.apache.ibatis.annotations.Select;
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
public interface IDeviceChannelRepository  extends JpaRepository<DeviceChannel,Long>, JpaSpecificationExecutor<DeviceChannel> {
    int deleteByDeviceId(String deviceId);


    @Query("""
    SELECT dc
    FROM DeviceChannel dc
    WHERE dc.deviceId = :deviceId
    AND (:parentChannelId IS NULL OR dc.parentId = :parentChannelId)
    AND (:online IS NULL OR dc.status = CASE WHEN :online = true THEN 1 ELSE 0 END)
    AND (
        :query IS NULL OR
        dc.channelId LIKE %:query% OR
        dc.name LIKE %:query%
    )
    AND (
        (:hasSubChannel IS NULL) OR
        (:hasSubChannel = true AND
            (SELECT COUNT(c) FROM DeviceChannel c WHERE c.parentId = dc.channelId) > 0
        )
        OR
        (:hasSubChannel = false AND
            (SELECT COUNT(c) FROM DeviceChannel c WHERE c.parentId = dc.channelId) = 0
        )
    )
    ORDER BY dc.channelId ASC
    """)
    List<DeviceChannel> queryChannelsByDeviceId(
            @Param("deviceId") String deviceId,
            @Param("parentChannelId") String parentChannelId,
            @Param("query") String query,
            @Param("hasSubChannel") Boolean hasSubChannel,
            @Param("online") Boolean online
    );

    @Query("""
SELECT dc 
FROM DeviceChannel dc 
WHERE dc.deviceId = :deviceId 
AND dc.channelId = :channelId
""")
    Optional<DeviceChannel> queryChannel(
            @Param("deviceId") String deviceId,
            @Param("channelId") String channelId
    );
    Optional<DeviceChannel> findByDeviceIdAndChannelId(String deviceId, String channelId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE DeviceChannel dc
    SET dc.streamId = :streamId
    WHERE dc.deviceId = :deviceId
    AND dc.channelId = :channelId
    """)
    int startPlay(
            @Param("deviceId") String deviceId,
            @Param("channelId") String channelId,
            @Param("streamId") String streamId
    );


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
    UPDATE DeviceChannel dc
    SET dc.streamId = null
    WHERE dc.deviceId = :deviceId
    AND dc.channelId = :channelId
    """)
    int stopPlay(
            @Param("deviceId") String deviceId,
            @Param("channelId") String channelId
    );
}
