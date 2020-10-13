package com.zhy.proxy.statics;

import com.zhy.proxy.Message;
import com.zhy.proxy.Wash;

public class Alvin implements Message, Wash {
    @Override
    public void message() {
        System.out.println("精通各种按摩手法");
    }

    @Override
    public void wash() {

    }
}
