package com.genersoft.iot.vmp.vmanager.screenshot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.transmit.callback.DeferredResultHolder;
import com.genersoft.iot.vmp.gb28181.transmit.callback.RequestMessage;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.media.zlm.ZLMRESTfulUtils;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;
import com.genersoft.iot.vmp.vmanager.play.PlayController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.message.Response;
import java.util.UUID;

/**
 * @author wangfeng
 * @date 2025/9/8
 * @description TODO
 */

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SnapshotController {

    private final static Logger logger = LoggerFactory.getLogger(SnapshotController.class);
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


    @GetMapping("/snap/{deviceId}/{channelId}")
    public DeferredResult<ResponseEntity<String>> play(@PathVariable String deviceId,
                                                       @PathVariable String channelId) {


        Device device = storager.queryVideoDevice(deviceId);
        StreamInfo streamInfo = redisCatchStorage.queryPlayByDevice(deviceId, channelId);

        UUID uuid = UUID.randomUUID();
        DeferredResult<ResponseEntity<String>> result = new DeferredResult<ResponseEntity<String>>();

        // 录像查询以channelId作为deviceId查询
        resultHolder.put(DeferredResultHolder.CALLBACK_CMD_PlAY + uuid, result);
        // 发送点播消息
        cmder.playStreamCmd(device, channelId, (JSONObject response) -> {
            logger.info("收到订阅消息： " + response.toJSONString());
           // playService.onPublishHandlerForPlay(response, deviceId, channelId, uuid.toString());
            logger.info("====2=====");

        }, event -> {
            RequestMessage msg = new RequestMessage();
            msg.setId(DeferredResultHolder.CALLBACK_CMD_PlAY + uuid);
            Response response = event.getResponse();
            msg.setData(String.format("点播失败， 错误码： %s, %s", response.getStatusCode(), response.getReasonPhrase()));
            resultHolder.invokeResult(msg);
            logger.info("====21=====");

        });
        logger.info("====6=====");

        // 超时处理
        result.onTimeout(()->{
            logger.info(String.format("设备点播超时，deviceId：%s ，channelId：%s", deviceId, channelId));
            // 释放rtpserver
            logger.info("====71=====");
            cmder.closeRTPServer(device, channelId);
            RequestMessage msg = new RequestMessage();
            msg.setId(DeferredResultHolder.CALLBACK_CMD_PlAY + uuid);
            msg.setData("Timeout");
            resultHolder.invokeResult(msg);
            logger.info("====72=====");

        });
        logger.info("====8=====");

        return result;
    }
}
