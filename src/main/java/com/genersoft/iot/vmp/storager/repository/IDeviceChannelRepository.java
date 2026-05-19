package com.genersoft.iot.vmp.storager.repository;

import com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannelDefinition;
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
public interface IDeviceChannelRepository  extends JpaRepository<DeviceChannelDefinition,Long>, JpaSpecificationExecutor<DeviceChannelDefinition> {
   // int deleteByDeviceId(String device_id);
   @Transactional
   @Modifying
   @Query("""
       delete from DeviceChannelDefinition
       where device_id = :deviceId
       """)
   int deleteByDeviceId(  @Param("deviceId") String deviceId );

    @Query("""
    SELECT dc
    FROM DeviceChannelDefinition dc
    WHERE dc.device_id = :device_id
    AND (:parentChannelId IS NULL OR dc.parentId = :parentChannelId)
    AND (:online IS NULL OR dc.status = CASE WHEN :online = true THEN 1 ELSE 0 END)
    AND (
        :query IS NULL OR
        dc.channel_id LIKE %:query% OR
        dc.name LIKE %:query%
    )
    AND (
        (:hasSubChannel IS NULL) OR
        (:hasSubChannel = true AND
            (SELECT COUNT(c) FROM DeviceChannelDefinition c WHERE c.parentId = dc.channel_id) > 0
        )
        OR
        (:hasSubChannel = false AND
            (SELECT COUNT(c) FROM DeviceChannelDefinition c WHERE c.parentId = dc.channel_id) = 0
        )
    )
    ORDER BY dc.channel_id ASC
    """)
    List<DeviceChannelDefinition> queryChannelsByDeviceId(
            @Param("device_id") String device_id,
            @Param("parentChannelId") String parentChannelId,
            @Param("query") String query,
            @Param("hasSubChannel") Boolean hasSubChannel,
            @Param("online") Boolean online
    );

    @Query("""
SELECT dc 
FROM DeviceChannelDefinition dc 
WHERE dc.device_id = :device_id 
AND dc.channel_id = :channel_id
""")
    Optional<DeviceChannelDefinition> queryChannel(
            @Param("device_id") String device_id,
            @Param("channel_id") String channel_id
    );
  //  Optional<DeviceChannelDefinition> findByDeviceIdAndChannelId(String device_id, String channel_id);


    @Query("""
       from DeviceChannelDefinition
       where device_id = :deviceId
       and channel_id = :channelId
       """)
    Optional<DeviceChannelDefinition> findByDeviceIdAndChannelId(@Param("deviceId") String deviceId, @Param("channelId") String channelId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE DeviceChannelDefinition dc
    SET dc.streamId = :streamId
    WHERE dc.device_id = :device_id
    AND dc.channel_id = :channel_id
    """)
    int startPlay(
            @Param("device_id") String device_id,
            @Param("channel_id") String channel_id,
            @Param("streamId") String streamId
    );


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
    UPDATE DeviceChannelDefinition dc
    SET dc.streamId = null
    WHERE dc.device_id = :device_id
    AND dc.channel_id = :channel_id
    """)
    int stopPlay(
            @Param("device_id") String device_id,
            @Param("channel_id") String channel_id
    );
}
