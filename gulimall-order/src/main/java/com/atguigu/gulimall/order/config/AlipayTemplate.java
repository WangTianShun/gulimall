package com.atguigu.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000116660265";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBXtewHgA65S2bjIRJ6tB8IB/hCKSyKEAaxw1VepyPcbXXlBaFVFWHf4KWH7i3C6VIQkOYsI2mxF4kSYDLNwnhzY0RS4OZEhjD6viK7OF0br2MBr9hZSvpWNvCi1ryfLMpVL7JjfPzZ/BhIVb6+0bVqx7GuZ5WLSsXNNmdoXjw7FbMwfly2jsRMst49I5O5m1+3hFN6igYLCEe4w/SY3IRECYioDPnlM+MwuE+UrpBw7ZPZoS5s3+OhQdyjgvrfzbuuP5yxKzMfbecRNeDfV3QFRzvQOawrTu0ZQTj27FSaIvpCXxf/tvC4xCyleYhA8y3V/DMiYfdm2INToLWRpfFAgMBAAECggEAY5KmHE3bC3mdkt2ibGopMYBgSQooCV/lhzax0preqaFxvo2ij9iLRBEmZ3ne6DQ0cNtfv8QUIsl9VBym3sJr/roA29DtZwGAwjq2NeeA1+bsSTxgTQxbsezMqH4t7/l+xE8P0X5Kx4u2fmv00JRavtZIh9gSQG3ffqsU27iOp4+Hl5ik9Aa5QMYDCrQy6No2HN8AHxmycpLfxWeDrpSncrOyEO8YsUxeHdWSmFSfHWIXdhpVqJqVJwInKoxhUEHCvBrSyXAbFDS1joo78O1+bTciJprfIjmwBcjKaiBZEGQK/ecUYdom03PFXKoeKbK0Vr5fQQ2BGY0PdJwPG+nfeQKBgQDDnlD7zVM/GuW/uxk596ZfYkSKRUbAdrH9lysOuXInJocdMHICK8WaVJnzqjcTQmVwaii+k/Thmr/FS51Ubm/cM8PQz5qdxPvlCii8OxNBGBpX6oGsEVi4RvNAEe73jVUFoGNsJ1+4Tr4DiLBZjRlKvAJYQo+dPrn7yFPHpAzE/wKBgQCpTaRg++CVlspnFI3YIGmqlTMYlo9xB07dznhD5VjV3dJD5988uXBjE8hqxcHI3nR3pq+r4kjgBb1zs8Hfuyn+bKaPVRgR3qx0c3oyeGhpA8DqkNNg7Idn1ORQSk2Vav9Eim6mzk4TMelDHst/1Ho+3F9XEphUvIIfuguC1PXPOwKBgAIM885f7aJ4trXW0UHhTbYsQomshjxYQi9lWUczPHYamkn9CwTozo56mo6KVz91b7jI9BpjVKrUw3PfAgwziCqnxEK5GmbS/Mz+2UR33hDNnuETDT7vg52S6NPgHEAuFBkWAFTO6Nr2wFkrIqYdq4k0BVZFhFnioXJ5PB+YoaWjAoGBAINolFICrsrtA6UGuyIp5lpU/Bf1pNiiR4C6JXbtqQo25mF2tDs55BXC4Sie3k6++79zTZ8oQFBBpE8OE0lIyMuGIbHW5pvYLhslFo/eDYyVMUPfrXHppR/eimgXhCiZqBDXMhOexdL4RommeWV60nfALPA0qq/6sTVW/mBKU5DJAoGAOx7I3Lv5L3ht8wPrVzgp4Ch1p9lM1hB261OLp4ByfuBwzG0lKKF29+P9vgJOUsQ8oXH+6icl9eh2ptSJRKM8y4jWbMBHhr5FTTgTBbkyByEngevztQvlW5igmQkM1+X0x3vlB7G8CSMH7lRDWLtJJNcGrSaRSQT4TfNHsWTuIIU=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzvZZhHU/1oZBNRXbgtYnuDDw9STNJxVgKAegUWY1ZHvcA37uGtdYjNbHXpMYP5ODcd2J73zPYSqc3d2CIwEFLFRSM2FYpqo1+m1NW77X3i0wTAAuQ6nbSBqPtuHpAeyF4rULMw4UqLGtsI+YYrJ+nev71QioaP5Mc/Ra48CajdFFFrcpA63LrIHSGdtTzdTHcS056f3AOpc3l3+dT1hOaPthWouzT6E6LrSwsNd+60Jc6gugmjUXEWMOt+HB7cpEd9GMbD9lwm1yJKA7WIsqM7seXq1CkK8IMktVHEPu5Vc6t4RfVUt2xouDwXGpdS1iPIc9wHtOHCsAq/vV26S8EwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url = "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
