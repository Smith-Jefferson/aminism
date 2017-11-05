package com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by ygxie on 2017/11/4.
 */
@Aspect // 声明切面类
@Component // 声明是一个 JavaBean
public class LogAop {
    @Pointcut("execution(* com.ctrip.flight.backendservice.backofficetool.aminism.spider.*.*(..))")
    private void pointcut(){}
    // ( 方法执行前的方法 ) execution 是切点表达式
    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        CLogManager.info(joinPoint.getSignature().getName(),joinPoint.getTarget().getClass().getName()+"的" + joinPoint.getSignature().getName() + "执行了,参数："+ Arrays.toString(joinPoint.getArgs()));
    }

    // ( 方法正确执行后的方法 ) 参数(切点表达式, 返回值变量)
    @AfterReturning(value = "pointcut()", returning="result")
    public void returning(JoinPoint join, String result){
        CLogManager.info(join.getSignature().getName(),"方法正常结束了,方法的返回值:" + result);
    }

    // ( 方法抛出异常时执行的方法 ) 参数(切点表达式, 抛出的异常变量)
    @AfterThrowing(value = "pointcut()", throwing="ex")
    public void throwing(JoinPoint join, Exception ex){
        CLogManager.error(join.getSignature().getName(),ex);
    }
}
