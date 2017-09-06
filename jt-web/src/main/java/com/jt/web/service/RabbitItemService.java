package com.jt.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.common.service.RedisService;

@Component
public class RabbitItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitItemService.class);

    @Autowired
    private RedisService redisService;

    public void updateItem(String redisKey) {
        LOGGER.info("接受到MQ的消息，内容为：{}", redisKey);
        
        //reids删除
        redisService.del(redisKey);
    }

}
