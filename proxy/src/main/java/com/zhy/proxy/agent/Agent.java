package com.zhy.proxy.agent;

import com.zhy.proxy.Message;

/**
 * 代理对象：马杀鸡经纪人
 */
public class Agent implements Message {
    public Agent(Message message) {
        this.message = message;
    }

    private final Message message;

    //....前置处理
    public void before() {
        System.out.println("一条龙服务,包君满意");
    }

    //....后置处理
    public void after() {
        System.out.println("满意度调查");
    }

    @Override
    public void message() {
        before();
        message.message();
        after();
    }
}
