package com.genersoft.iot.vmp.storager.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import com.genersoft.iot.vmp.gb28181.bean.MobilePosition;
/*import com.genersoft.iot.vmp.storager.dao.DeviceChannelMapper;
import com.genersoft.iot.vmp.storager.dao.DeviceMapper;
import com.genersoft.iot.vmp.storager.dao.DeviceMobilePositionMapper;*/
import com.genersoft.iot.vmp.storager.repository.IDeviceChannelRepository;
 import com.genersoft.iot.vmp.storager.repository.IDeviceRepository;
import com.genersoft.iot.vmp.storager.repository.IMobilePositionRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;

/**    
 * @Description:视频设备数据存储-jdbc实现
 * @author: swwheihei
 * @date:   2020年5月6日 下午2:31:42
 */
@SuppressWarnings("rawtypes")
@Component
public class VideoManagerStoragerImpl implements IVideoManagerStorager {

/*	@Autowired
    private DeviceMapper deviceMapper;
	@Autowired
	private DeviceChannelMapper deviceChannelMapper;
	@Autowired
	private DeviceMobilePositionMapper deviceMobilePositionMapper;*/

	@Autowired
	private IDeviceRepository deviceMapper;
	@Autowired
	private IDeviceChannelRepository deviceChannelMapper;

	@Autowired
	private IMobilePositionRepository deviceMobilePositionMapper;


	/**
	 * 根据设备ID判断设备是否存在
	 *
	 * @param deviceId 设备ID
	 * @return true:存在  false：不存在
	 */
	@Override
	public boolean exists(String deviceId) {
		Device re = deviceMapper.getByDeviceId(deviceId).orElse(null);

		return re != null;
	}

	/**
	 * 视频设备创建
	 *
	 * @param device 设备对象
	 * @return true：创建成功  false：创建失败
	 */
	@Override
	public synchronized boolean create(Device device) {
		device = deviceMapper.save(device);

		return device.getDeviceId() != null? true : false;
	}



	/**
	 * 视频设备更新
	 *
	 * @param device 设备对象
	 * @return true：更新成功  false：更新失败
	 */
	@Override
	public synchronized boolean updateDevice(Device device) {

		Device deviceByDeviceId = deviceMapper.findByDeviceId(device.getDeviceId()).orElse(null);
		if (deviceByDeviceId == null) {
			deviceMapper.save(device);
			return device.getDeviceId() != null? true : false;

		}else {
			device.setUpdate_time(LocalDateTime.now());
			deviceMapper.save(device);
			return device.getDeviceId() != null? true : false;

		}
	}

	@Override
	public synchronized void updateChannel(String deviceId, DeviceChannel channel) {
		String channelId = channel.getChannelId();
		channel.setDeviceId(deviceId);
		DeviceChannel deviceChannel = deviceChannelMapper.queryChannel(deviceId, channelId).orElse(null);;

		if (deviceChannel == null) {
			deviceChannelMapper.save(channel);
		}else {
			deviceChannelMapper.save(channel);
		}
	}

	@Override
	public void startPlay(String deviceId, String channelId, String streamId) {
		deviceChannelMapper.startPlay(deviceId, channelId, streamId);
	}

	@Override
	public void stopPlay(String deviceId, String channelId) {
		deviceChannelMapper.stopPlay(deviceId, channelId);
	}

	/**
	 * 获取设备
	 *
	 * @param deviceId 设备ID
	 * @return Device 设备对象
	 */
	@Override
	public Device queryVideoDevice(String deviceId) {
		return deviceMapper.findByDeviceId(deviceId).orElse(null);
	}

	@Override
	public PageInfo queryChannelsByDeviceId(String deviceId, String query, Boolean hasSubChannel, Boolean online, int page, int count) {
		// 获取到所有正在播放的流
		PageHelper.startPage(page, count);
		List<DeviceChannel> all = deviceChannelMapper.queryChannelsByDeviceId(deviceId, null, query, hasSubChannel, online);
		return new PageInfo<>(all);
	}

	@Override
	public List<DeviceChannel> queryChannelsByDeviceId(String deviceId) {
		return deviceChannelMapper.queryChannelsByDeviceId(deviceId, null,null, null, null);
	}
	@Override
	public PageInfo<DeviceChannel> querySubChannels(String deviceId, String parentChannelId, String query, Boolean hasSubChannel, String online, int page, int count) {
		PageHelper.startPage(page, count);
		List<DeviceChannel> all = deviceChannelMapper.queryChannelsByDeviceId(deviceId, parentChannelId, null, null, null);
		return new PageInfo<>(all);
	}

	@Override
	public DeviceChannel queryChannel(String deviceId, String channelId) {
		return deviceChannelMapper.queryChannel(deviceId, channelId).orElse(null);
	}


	/**
	 * 获取多个设备
	 *
	 * @param page 当前页数
	 * @param count 每页数量
	 * @return PageInfo<Device> 分页设备对象数组
	 */
	@Override
	public PageInfo<Device> queryVideoDeviceList(int page, int count) {
		PageHelper.startPage(page, count);
		List<Device> all = deviceMapper.findAll();
		System.out.println(page + ">>>>"+count+">>>设备数量===" + all != null ? all.size() : 0);
		return new PageInfo<>(all);
	}

	/**
	 * 获取多个设备
	 *
	 * @return List<Device> 设备对象数组
	 */
	@Override
	public List<Device> queryVideoDeviceList() {

		//List<Device> deviceList =  deviceMapper.getDevices();
		List<Device> deviceList =  deviceMapper.findAll();
		if (deviceList != null && deviceList.size() > 0) {

		}

		return deviceList;
	}

	/**
	 * 删除设备
	 *
	 * @param deviceId 设备ID
	 * @return true：删除成功  false：删除失败
	 */
	@Override
	public boolean delete(String deviceId) {
		int result = deviceMapper.deleteByDeviceId(deviceId);

		return result > 0;
	}

	/**
	 * 更新设备在线
	 *
	 * @param deviceId 设备ID
	 * @return true：更新成功  false：更新失败
	 */
	@Override
	public synchronized boolean online(String deviceId) {
		Device device = deviceMapper.getByDeviceId(deviceId).orElse(null);;
		if (device == null) {
			return false;
		}
		device.setOnline(1);
		System.out.println("更新设备在线");
		device =  deviceMapper.save(device);
		return device.getDeviceId() != null? true : false;
	}

	/**
	 * 更新设备离线
	 *
	 * @param deviceId 设备ID
	 * @return true：更新成功  false：更新失败
	 */
	@Override
	public synchronized boolean outline(String deviceId) {
		Device device = deviceMapper.findByDeviceId(deviceId).orElse(null);;
		if(device == null){
			return true;
		}
		device.setOnline(0);
		System.out.println("更新设备离线");
		Device re =  deviceMapper.save(device);
		return re != null;
	}

	/**
	 * 清空通道
	 * @param deviceId
	 */
	@Override
	public void cleanChannelsForDevice(String deviceId) {
		deviceChannelMapper.deleteByDeviceId(deviceId);
	}

	/**
	 * 添加Mobile Position设备移动位置
	 * @param MobilePosition
	 */
	@Override
	public synchronized boolean insertMobilePosition(MobilePosition mobilePosition) {
		MobilePosition re =  deviceMobilePositionMapper.save(mobilePosition);
		return re != null;

	}

	/**
	 * 查询移动位置轨迹
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 */
	@Override
	public synchronized List<MobilePosition> queryMobilePositions(String deviceId, String startTime, String endTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime start = startTime == null ? null : LocalDateTime.parse(startTime, formatter);
		LocalDateTime end = endTime == null ? null : LocalDateTime.parse(endTime, formatter);

		return deviceMobilePositionMapper.queryPositionByDeviceIdAndTime(deviceId, start, end);
	}

	/**
	 * 查询最新移动位置
	 * @param deviceId
	 */
	@Override
	public MobilePosition queryLatestPosition(String deviceId) {
		return deviceMobilePositionMapper.queryTopByDeviceIdOrderByTimeDesc(deviceId).orElse(null);
	}

	/**
	 * 删除指定设备的所有移动位置
	 * @param deviceId
	 */
	public int clearMobilePositionsByDeviceId(String deviceId) {
		return deviceMobilePositionMapper.deleteByDeviceId(deviceId);
	}
}
