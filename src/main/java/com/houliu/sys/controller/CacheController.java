package com.houliu.sys.controller;

import com.houliu.bus.cache.CachePool;
import com.houliu.sys.common.Cachebean;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author houliu
 * @create 2020-01-12 00:04    缓存管理控制器
 */

@RestController
@RequestMapping("cache")
public class CacheController {

    public static volatile Map<String,Object> CAHCE_CONTAINER = CachePool.CACHE_CONTAINER;

    /**
     * 查询所有缓存
     */
    @RequestMapping("loadAllCache")
    public DataGridView loadAllCache(){
        List<Cachebean> list = new ArrayList<>();
        Set<Map.Entry<String, Object>> entrySet = CAHCE_CONTAINER.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            list.add(new Cachebean(entry.getKey(),entry.getValue()));
        }
        return new DataGridView(list);
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    @RequestMapping("deleteCache")
    public ResultObj deleteCache(String key){
        CachePool.removeCacheByKey(key);
        return ResultObj.DELETE_OK;
    }

    /**
     * 清空所有缓存
     * @return
     */
    @RequestMapping("removeAllCache")
    public ResultObj removeAllCache(){
        CachePool.removeAll();
        return ResultObj.DELETE_OK;
    }

    /**
     * 同步缓存
     * @return
     */
    @RequestMapping("syncCache")
    public ResultObj syncCache(){
        CachePool.syncData();
        return ResultObj.SYNCCACHE_SUCCESS;
    }

}
