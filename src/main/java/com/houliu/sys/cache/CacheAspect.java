package com.houliu.sys.cache;

import com.houliu.sys.entity.Dept;
import com.houliu.sys.entity.User;
import com.houliu.sys.vo.DeptVo;
import com.houliu.sys.vo.UserVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author houliu
 * @create 2020-01-03 16:53
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {
    //声明一个缓存容器
    private static final Map<String,Object> CACHE_CONTAINER = new ConcurrentHashMap<>();
    /**
     * 声明部门的切面表达式
     */
    private static final String POINTCUT_DEPT_GET = "execution(* com.houliu.sys.service.impl.DeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_SAVE= "execution(* com.houliu.sys.service.impl.DeptServiceImpl.save(..))";
    private static final String POINTCUT_DEPT_UPDATE = "execution(* com.houliu.sys.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_DELETE = "execution(* com.houliu.sys.service.impl.DeptServiceImpl.removeById(..))";
    //声明缓存前缀
    private static final String CACHE_DEPT_PREFIX = "dept:";
    //日志出处
    private Log log = LogFactory.getLog(CacheAspect.class);

    /**
     * 添加切入
     */
    @Around(value = POINTCUT_DEPT_SAVE)
    public Object cacheDeptAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Dept dept = (Dept) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+dept.getId(),dept);
        }
        return res;
    }
    /**
     * 查询切入
     */
    @Around(value = POINTCUT_DEPT_GET)
    public Object cacheDeptGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + id);
        if (res1 != null){
            log.info("从缓存中得到数据：" + CACHE_DEPT_PREFIX + id);
            return res1;
        }else {
            Dept res2 = (Dept) joinPoint.proceed();
            //放入缓存
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX + res2.getId(),res2);
            log.info("从缓存中未得到数据，将从数据库查询，并放入缓存："+ CACHE_DEPT_PREFIX + res2.getId());
            return res2;
        }
    }

    /**
     * 更新切入
     */
    @Around(value = POINTCUT_DEPT_UPDATE)
    public Object cacheDeptUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        DeptVo deptVo = (DeptVo) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Dept dept = (Dept) CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + deptVo.getId());  //从缓存中取
            if (null == dept){
                dept = new Dept();
                BeanUtils.copyProperties(deptVo,dept);
                log.info("部门对象缓存已更新"+ CACHE_DEPT_PREFIX + deptVo.getId());
                CACHE_CONTAINER.put(CACHE_DEPT_PREFIX + dept.getId(),dept);
            }
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_DEPT_DELETE)
    public Object cacheDeptDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_DEPT_PREFIX+id);
            log.info("部门对象缓存已删除"+ CACHE_DEPT_PREFIX+id);
        }
        return isSuccess;
    }

    /**
     * 声明用户的切面表达式
     */
    private static final String POINTCUT_USER_GET = "execution(* com.houliu.sys.service.impl.UserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_SAVE= "execution(* com.houliu.sys.service.impl.UserServiceImpl.save(..))";
    private static final String POINTCUT_USER_UPDATE = "execution(* com.houliu.sys.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_DELETE = "execution(* com.houliu.sys.service.impl.UserServiceImpl.removeById(..))";

    private static final String CACHE_USER_PREFIX = "user:";

    /**
     * 添加切入
     */
    @Around(value = POINTCUT_USER_SAVE)
    public Object cacheUserAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        User user = (User) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_USER_PREFIX+user.getId(),user);
        }
        return res;
    }
    /**
     * 查询切入
     */
    @Around(value = POINTCUT_USER_GET)
    public Object cacheUserGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_USER_PREFIX + id);
        if (res1 != null){
            log.info("从缓存中得到数据：" + CACHE_USER_PREFIX + id);
            return res1;
        }else {
            log.info("从缓存中未得到数据，将从数据库查询，并放入缓存：");
            User res2 = (User) joinPoint.proceed();
            //放入缓存
            CACHE_CONTAINER.put(CACHE_USER_PREFIX + res2.getId(),res2);
            return res2;
        }
    }

    /**
     * 更新切入
     */
    @Around(value = POINTCUT_USER_UPDATE)
    public Object cacheUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        UserVo userVo = (UserVo) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            User user = (User) CACHE_CONTAINER.get(CACHE_USER_PREFIX + userVo.getId());  //从缓存中取
            if (null == user){
                user = new User();
                BeanUtils.copyProperties(userVo,user);
                log.info("用户对象缓存已更新"+ CACHE_USER_PREFIX + userVo.getId());
                CACHE_CONTAINER.put(CACHE_USER_PREFIX + user.getId(),user);
            }
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_USER_DELETE)
    public Object cacheUserDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_USER_PREFIX+id);
            log.info("用户对象缓存已删除"+ CACHE_USER_PREFIX+id);
        }
        return isSuccess;
    }
}
