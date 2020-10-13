package com.zhy.proxy;

import com.zhy.proxy.agent.Agent;
import com.zhy.proxy.statics.Alvin;
import com.zhy.proxy.statics.Tom;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        //静态代码
        //真实对象
        Message tomMsg = new Tom();
        //代理对象
        Agent agent = new Agent(tomMsg);
        //调用
        agent.message();

        final Alvin alvin = new Alvin();
        //动态代理
        Object proxy = Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[]{Message.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return method.invoke(alvin, objects);
                    }
                });
        Message msg = (Message) proxy;
        msg.message();
        proxy();
    }

    private static void proxy() throws Exception {
        String name = Message.class.getName() + "$Proxy0";
        //生成代理指定接口的Class数据
        byte[] bytes = ProxyGenerator.generateProxyClass(name, new Class[]{Message.class});
        FileOutputStream fos = new FileOutputStream("proxy/" + name + ".class");
        fos.write(bytes);
        fos.close();
    }
}
