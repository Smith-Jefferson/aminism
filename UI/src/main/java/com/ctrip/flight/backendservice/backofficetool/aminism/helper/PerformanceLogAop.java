package com.ctrip.flight.backendservice.backofficetool.aminism.helper;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.CLogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by ygxie on 2017/11/4.
 */
@Aspect // 声明切面类
@Component // 声明是一个 JavaBean
public class PerformanceLogAop {
    @Pointcut("execution(* com.ctrip.flight.backendservice.backofficetool.aminism.controller.*.*(..))")
    private void pointcut(){}
    // 环绕通知
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint join) {
        long start = System.currentTimeMillis();
        CLogManager.info(join.getSignature().getName(), "调用 " + join.getTarget() + " 的 " + join.getSignature().getName() + " 方法。方法入参：" + Arrays.toString(join.getArgs()));
        try {
            Object result = join.proceed();
            CLogManager.info(join.getSignature().getName(), "调用 " + join.getTarget() + " 的 " + join.getSignature().getName() + " 方法。方法返回值：" + result);
            return result;
        }
        catch (Throwable e) {
            CLogManager.error(join.getSignature().getName(), e);
        }
        finally {
            long end = System.currentTimeMillis();
            CLogManager.info(join.getSignature().getName(),"运行时间（ms）："+(end-start));
        }
        return new Object();
    }

}
