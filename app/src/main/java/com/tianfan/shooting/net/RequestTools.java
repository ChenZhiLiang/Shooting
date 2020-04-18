package com.tianfan.shooting.net;

/**
 * @program: HttpDemo
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-29 12:47
 **/
public class RequestTools {
    private static ReQCallBack callBack;
    public static ReQCallBack doAction() {
//        if (callBack == null) {
//            synchronized (RequestTools.class) {
//                if (callBack == null) {
//                    callBack = new ReQCallBack();
//                }
//            }
//        }
        return  new ReQCallBack();
    }
}
