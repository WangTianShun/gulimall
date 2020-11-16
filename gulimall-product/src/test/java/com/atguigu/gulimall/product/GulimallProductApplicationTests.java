package com.atguigu.gulimall.product;


import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.SkuSaleAttrValueDao;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Test
    public void test(){
//        List<SpuItemAttrGroupVo> attrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(225L, 2L);
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(2L);
        System.out.println(saleAttrsBySpuId);
    }

    @Test
    void contextLoads() {
       /* BrandEntity brandEntity = new BrandEntity();
        brandEntity.setDescript("化为");
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功");*/
    }
    @Test
    public void testStringRedisTemplate(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //保存
        ops.set("hello","world_"+ UUID.randomUUID().toString());
        //查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是："+hello);
    }

    @Test
    public void testStringRedisson(){
        System.out.println(redissonClient);
    }
}
