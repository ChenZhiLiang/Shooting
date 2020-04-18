package com.tianfan.shooting.net;

/**
 * @program: HttpDemo
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-29 14:27
 **/
    public interface GetResult<T>{
        public void fail(String msg);
        public void ok(T result);
    }
