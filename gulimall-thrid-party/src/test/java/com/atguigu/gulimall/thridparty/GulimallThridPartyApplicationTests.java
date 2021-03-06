package com.atguigu.gulimall.thridparty;

import com.aliyun.oss.OSSClient;
import com.atguigu.gulimall.thridparty.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;


@SpringBootTest
class GulimallThridPartyApplicationTests {
    @Autowired
    OSSClient ossClient;

    @Autowired
    SmsComponent smsComponent;

    @Test
    public void testsendSmsCode(){
        smsComponent.sendSmsCode("18896736055","563540");

    }

    
    @Test
    public void sendSms() {
        String host = "https://fsmsn.market.alicloudapi.com";  // 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/fsms132";  // 【2】后缀
        String appcode = "51d31e027fbf47a2b15a569cf88ddbb4"; // 【3】开通服务后 买家中心-查看AppCode
        String param = "123456";  // 【4】请求参数，详见文档描述
        String phone = "18896736055";  //  【4】请求参数，详见文档描述
        String sign = "175622";   //  【4】请求参数，详见文档描述
        String skin = "1";  //  【4】请求参数，详见文档描述
        String urlSend = host + path + "?param=" + param +"&phone="+phone +"&sign="+sign +"&skin="+skin;   // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的json:");
                System.out.print(json);
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                }
            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
        }
    }

    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }



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
