package com.houliu.bus.cache;

import com.houliu.bus.entity.Customer;
import com.houliu.bus.entity.Provider;
import com.houliu.sys.cache.CacheAspect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author houliu
 * @create 2020-01-08 19:59
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class BusinessCacheAspect {

    //日志出处
    private Log log = LogFactory.getLog(CacheAspect.class);

    //声明一个缓存容器
    private static final Map<String,Object> CACHE_CONTAINER = new ConcurrentHashMap<>();

    public static Map<String, Object> getCacheContainer() {
        return CACHE_CONTAINER;
    }

    /**
     * 声明客户的切面表达式
     */
    private static final String POINTCUT_CUSTOMER_GET = "execution(* com.houliu.bus.service.impl.CustomerServiceImpl.getById(..))";
    private static final String POINTCUT_CUSTOMER_SAVE= "execution(* com.houliu.bus.service.impl.CustomerServiceImpl.save(..))";
    private static final String POINTCUT_CUSTOMER_UPDATE = "execution(* com.houliu.bus.service.impl.CustomerServiceImpl.updateById(..))";
    private static final String POINTCUT_CUSTOMER_DELETE = "execution(* com.houliu.bus.service.impl.CustomerServiceImpl.removeById(..))";
    private static final String POINTCUT_CUSTOMER_BATCHDELETE = "execution(* com.houliu.bus.service.impl.CustomerServiceImpl.removeByIds(..))";
    //声明缓存前缀
    private static final String CACHE_CUSTOMER_PREFIX = "customer:";


    /**
     * 添加切入
     */
    @Around(value = POINTCUT_CUSTOMER_SAVE)
    public Object cacheCustomerAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Customer dept = (Customer) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX+dept.getId(),dept);
        }
        return res;
    }
    /**
     * 查询切入
     */
    @Around(value = POINTCUT_CUSTOMER_GET)
    public Object cacheCustomerGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_CUSTOMER_PREFIX + id);
        if (res1 != null){
            log.info("从缓存中得到数据：" + CACHE_CUSTOMER_PREFIX + id);
            return res1;
        }else {
            Customer res2 = (Customer) joinPoint.proceed();
            //放入缓存
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX + res2.getId(),res2);
            log.info("从缓存中未得到数据，将从数据库查询，并放入缓存："+ CACHE_CUSTOMER_PREFIX + res2.getId());
            return res2;
        }
    }

    /**
     * 更新切入
     */
    @Around(value = POINTCUT_CUSTOMER_UPDATE)
    public Object cacheCustomerUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Customer customerVo = (Customer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Customer customer = (Customer) CACHE_CONTAINER.get(CACHE_CUSTOMER_PREFIX + customerVo.getId());  //从缓存中取
            if (null == customer){
                customer = new Customer();
                BeanUtils.copyProperties(customerVo,customer);
                log.info("客户对象缓存已更新"+ CACHE_CUSTOMER_PREFIX + customer.getId());
                CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX + customer.getId(),customer);
            }
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_CUSTOMER_DELETE)
    public Object cacheCustomerDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_CUSTOMER_PREFIX+id);
            log.info("客户对象缓存已删除"+ CACHE_CUSTOMER_PREFIX+id);
        }
        return isSuccess;
    }

    /**
     * 批量删除切入
     */
    @Around(value = POINTCUT_CUSTOMER_BATCHDELETE)
    public Object cacheCustomerBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            for (Serializable id : idList) {
                //删除缓存
                CACHE_CONTAINER.remove(CACHE_CUSTOMER_PREFIX+id);
                log.info("客户对象缓存已删除"+ CACHE_CUSTOMER_PREFIX+id);
            }
        }
        return isSuccess;
    }

    //=========================================================================

    /**
     * 声明客户的切面表达式
     */
    private static final String POINTCUT_PROVIDER_GET = "execution(* com.houliu.bus.service.impl.ProviderServiceImpl.getById(..))";
    private static final String POINTCUT_PROVIDER_SAVE= "execution(* com.houliu.bus.service.impl.ProviderServiceImpl.save(..))";
    private static final String POINTCUT_PROVIDER_UPDATE = "execution(* com.houliu.bus.service.impl.ProviderServiceImpl.updateById(..))";
    private static final String POINTCUT_PROVIDER_DELETE = "execution(* com.houliu.bus.service.impl.ProviderServiceImpl.removeById(..))";
    private static final String POINTCUT_PROVIDER_BATCHDELETE = "execution(* com.houliu.bus.service.impl.ProviderServiceImpl.removeByIds(..))";
    //声明缓存前缀
    private static final String CACHE_PROVIDER_PREFIX = "provider:";


    /**
     * 添加切入
     */
    @Around(value = POINTCUT_PROVIDER_SAVE)
    public Object cacheProviderAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Provider provider = (Provider) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX+provider.getId(),provider);
        }
        return res;
    }
    /**
     * 查询切入
     */
    @Around(value = POINTCUT_PROVIDER_GET)
    public Object cacheProviderGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_PROVIDER_PREFIX + id);
        if (res1 != null){
            log.info("从缓存中得到数据：" + CACHE_PROVIDER_PREFIX + id);
            return res1;
        }else {
            Provider res2 = (Provider) joinPoint.proceed();
            //放入缓存
            CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX + res2.getId(),res2);
            log.info("从缓存中未得到数据，将从数据库查询，并放入缓存："+ CACHE_PROVIDER_PREFIX + res2.getId());
            return res2;
        }
    }

    /**
     * 更新切入
     */
    @Around(value = POINTCUT_PROVIDER_UPDATE)
    public Object cacheProviderUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Provider providerVo = (Provider) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Provider provider = (Provider) CACHE_CONTAINER.get(CACHE_PROVIDER_PREFIX + providerVo.getId());  //从缓存中取
            if (null == provider){
                provider = new Provider();
                BeanUtils.copyProperties(providerVo,provider);
                log.info("供应商对象缓存已更新"+ CACHE_PROVIDER_PREFIX + providerVo.getId());
                CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX + provider.getId(),provider);
            }
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_PROVIDER_DELETE)
    public Object cacheProviderDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_PROVIDER_PREFIX+id);
            log.info("供应商对象缓存已删除"+ CACHE_PROVIDER_PREFIX+id);
        }
        return isSuccess;
    }

    /**
     * 批量删除切入
     */
    @Around(value = POINTCUT_PROVIDER_BATCHDELETE)
    public Object cacheProviderBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            for (Serializable id : idList) {
                //删除缓存
                CACHE_CONTAINER.remove(CACHE_PROVIDER_PREFIX+id);
                log.info("供应商对象缓存已删除"+ CACHE_PROVIDER_PREFIX+id);
            }
        }
        return isSuccess;
    }


}
