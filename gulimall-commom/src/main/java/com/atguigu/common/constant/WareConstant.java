package com.atguigu.common.constant;

import sun.management.snmp.jvmmib.EnumJvmMemManagerState;

/**
 * @author WangTianShun
 * @date 2020/10/16 22:25
 */
public class WareConstant {
    public enum PurchaseStatusEnum{
        CREATED(0,"新建"),
        ASSIIGNED(1,"已分配"),
        RECEIVE(2,"以领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private String msg;

        PurchaseStatusEnum(int code,String msg){
            this.code = code;
            this.msg  = msg;
        }
        public int getCode(){
            return code;
        }
        public String msg(){
            return msg;
        }
    }

    public enum PurchaseDetailStatusEnum{
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");
        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public int getCode(){
            return code;
        }
        public String getMsg(){
            return msg;
        }
    }
}
