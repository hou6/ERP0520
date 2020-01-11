package com.houliu.bus.cache;

import com.houliu.bus.entity.Customer;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Provider;
import com.houliu.bus.mapper.CustomerMapper;
import com.houliu.bus.mapper.GoodsMapper;
import com.houliu.bus.mapper.ProviderMapper;
import com.houliu.sys.common.SpringUtil;
import com.houliu.sys.entity.Dept;
import com.houliu.sys.entity.User;
import com.houliu.sys.mapper.DeptMapper;
import com.houliu.sys.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houliu
 * @create 2020-01-11 23:08
 */
public class CachePool {

    public static volatile Map<String ,Object> CACHE_CONTAINER = new HashMap<>();

    /**
     * 根据key删除缓存
     * @param key
     */
    public static void removeCacheByKey(String key){
        if (CACHE_CONTAINER.containsKey(key)){
            CACHE_CONTAINER.remove(key);
        }
    }

    /**
     * 清空所有缓存
     */
    public static void removeAll(){
        CACHE_CONTAINER.clear();
    }

    public static void syncData(){
        //同步部门数据
        DeptMapper deptMapper = SpringUtil.getBean(DeptMapper.class);
        List<Dept> deptList = deptMapper.selectList(null);
        deptList.forEach(dept -> CACHE_CONTAINER.put("dept:" + dept.getId(),dept));
        //同步用户数据
        UserMapper userMapper = SpringUtil.getBean(UserMapper.class);
        List<User> userList = userMapper.selectList(null);
        userList.forEach(user -> CACHE_CONTAINER.put("user:" + user.getId(),user));
        //同步客户数据
        CustomerMapper customerMapper = SpringUtil.getBean(CustomerMapper.class);
        List<Customer> customerList = customerMapper.selectList(null);
        customerList.forEach(customer -> CACHE_CONTAINER.put("customer:" + customer.getId(),customer));
        //同步供应商数据
        ProviderMapper providerMapper = SpringUtil.getBean(ProviderMapper.class);
        List<Provider> providerList = providerMapper.selectList(null);
        providerList.forEach(provider -> CACHE_CONTAINER.put("provider:" + provider.getId(),provider));
        //同步商品数据
        GoodsMapper goodsMapper = SpringUtil.getBean(GoodsMapper.class);
        List<Goods> goodsList = goodsMapper.selectList(null);
        goodsList.forEach(goods -> CACHE_CONTAINER.put("goods:" + goods.getId(),goods));
    }

}
