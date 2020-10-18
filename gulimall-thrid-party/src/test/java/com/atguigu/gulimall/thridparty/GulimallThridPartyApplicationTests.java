package com.atguigu.gulimall.thridparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThridPartyApplicationTests {
    @Autowired
    OSSClient ossClient;

    //1、引入oss-starter
    //2、配置key,endpoint相关信息即可
    //3、使用OSSClient 进行相关操作
    @Test
    void contextLoads() {
    }
    @Test
    public void testUpload() throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-shanghai.aliyuncs.com";
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI4FzhuX1rFQt2yWDmgwsn";
//        String accessKeySecret = "z3ZFoNHoCaG1V1qmQ32Dm4OTLNP5fA";

//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\56354\\Pictures\\Saved Pictures\\1.png");
        ossClient.putObject("wts-gulimall", "1.png", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        System.out.println("上传完成。。。");
    }
}
