package com.atguigu.gulimall.seckill.scheduled;

import com.atguigu.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Description: 秒杀商品的定时上架：每天晚上3点上架最近三天的商品。当天00:00:00 - 23:59:59  明天00:00:00 - 23:59:59  后天00:00:00 - 23:59:59
 * @Author: WangTianShun
 * @Date: 2020/12/13 9:44
 * @Version 1.0
 */
@Slf4j
@Service
public class SeckillSkuScheduled {

    @Autowired
    SeckillService seckillService;

    //TODO 幂等性处理
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSeckillSkuLatest3Days(){
        //重复上架无需处理
        log.info("上架秒杀的信息......");
        seckillService.uploadSeckillSkuLatest3Days();
    }
}
