package com.genersoft.iot.vmp.vmanager.screenshot;

import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.gb28181.bean.DeviceRemoteDefinition;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannelDefinition;
import com.genersoft.iot.vmp.gb28181.transmit.callback.DeferredResultHolder;
import com.genersoft.iot.vmp.gb28181.transmit.callback.RequestMessage;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.media.zlm.ZLMRESTfulUtils;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.message.Response;
import java.util.List;
import java.util.UUID;

/**
 * @author wangfeng
 * @date 2025/9/8
 * @description TODO
 */
@Component
public class ScheduleSnapshotStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleSnapshotStartupListener.class);
    @Autowired
    private IVideoManagerStorager storager;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;


    @Autowired
    private DeferredResultHolder resultHolder;
    @Autowired
    private ZLMRESTfulUtils zlmresTfulUtils;

    @Autowired
    private SIPCommander cmder;



    public static long k = 0;
    public void exec() throws Exception {
        while(true) {
            Thread.sleep(30000);
            logger.info("循环=====================================" + k++);

            List<DeviceRemoteDefinition> devices = storager.queryVideoDeviceList();
            for(int i = 0;devices!= null &&  i < devices.size(); i++) {

                DeviceRemoteDefinition nvrDevice = devices.get(i);
                int online = nvrDevice.getOnline();
                logger.info("设备id===DeviceID=="+nvrDevice.getDeviceId()+"==online==="+online);

                if(online >= 1) {
                    List<DeviceChannelDefinition> channels = storager.queryChannelsByDeviceId(nvrDevice.getDeviceId());
                    for (int j = 0; channels != null && j < channels.size(); j++) {
                        DeviceChannelDefinition channel = channels.get(j);
                        String channelId = channel.getChannelId();
                        logger.info("==通道===channelId==="+channelId);
                        String uploadUrl = "http://zhijianyunjian.com/device-service/api/video/nvr_upload";
                        boolean sucsess = cmder.screenshotByeCmd(nvrDevice, channelId, uploadUrl);
                        if (sucsess) {
                            JSONObject json = new JSONObject();
                            json.put("DeviceID", nvrDevice.getDeviceId());
                            json.put("ChannelID", channelId);
                            json.put("Result", "OK");

                            logger.info("====8=====" + json.toJSONString());

                        } else {
                            logger.info("强制关键帧API调用失败！");
                        }
                    }
                }

            }
        }



    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("SpringBoot 已完全启动，执行任务ScheduleSnapshotStartupListener中的方法...");
    /*    new Thread(() -> {
            try {
                exec();
            } catch (Exception e) {
                logger.info("异常：===" + e.getMessage());
            }
        }).start();*/

    }
}
