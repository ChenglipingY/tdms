package stu.clp.proxy;

import com.alibaba.fastjson.JSONObject;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @ClassName ProxyTest
 * @Description 111
 * @Email 15998994107@163.com
 * @Data 2020/7/18
 */
public class ProxyTest {

    public static void main(String[] args) {
//        final IUserService userService = new UserService();
//        IUserService userServer = (IUserService) getProxy(userService);
//
//        userServer.getUser("11");
//        userService.getUser("abc");
        // jdk8及之前：
        //  System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        // jdk8之后：
        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        // -----------  cglib 动态代理 测试
        // 设置 class文件缓存目录，如果不研究动态类的源码可以不设置
        //System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\cglib_classes");
        //  System.out.println( );
        System.out.println(JSONObject.toJSON(System.getProperties().getProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY)));
        //用于创建代理对象的增强器，可以对目标对象进行扩展
        IUserService userService = (IUserService) getCglibProxy();
        userService.getUser("cglib ");
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {

        }
        for (String s : list) {

        }

        if (list == null) {

        }

    }

    static class ProxyInvocationHander implements InvocationHandler {
        private Object target;

        ProxyInvocationHander(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("代理拦截方法 开始 >>>");
            System.out.println("开始执行 目标方法：begin");
            method.invoke(target, args);
            System.out.println("执行 目标方法介绍：end ");
            System.out.println("拦截方法结束 >>>");
            // return  可以返回目标方法返回的对象
            return null;
        }
    }

    public static Object getProxy(Object target) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new ProxyInvocationHander(target));
    }

    // ---------------   cglib  动态代理  --------------------
    // 实现拦截器
    private static class Usernterceptor implements MethodInterceptor {
        /**
         * obj：cglib生成的代理对象
         * method：被代理对象方法
         * args：方法入参
         * methodProxy: 代理方法
         */
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("被代理的方法为： " + method.getName());
            System.out.println("代理方法为" + methodProxy.getSuperName());
            System.out.println("代理开始");
            Object result = methodProxy.invokeSuper(o, objects);
            System.out.println(result);
            System.out.println("代理结束");

            return null;
        }
    }

    public static Object getCglibProxy() {
        Enhancer enhancer = new Enhancer();
        //将目标对象设置为父类
        enhancer.setSuperclass(UserService.class);
        //设置目标拦截器
        enhancer.setCallback(new Usernterceptor());
        // 创建代理对象
        //   UserService userService1 = (UserService)enhancer.create();
        //  userService1.getUser("cglib 动态代理");
        // 通过代理对象调用目标方法  test add proxy git
        // master add some variable << master
        return enhancer.create();
    }
}
