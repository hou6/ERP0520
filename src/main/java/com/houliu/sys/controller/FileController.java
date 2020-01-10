package com.houliu.sys.controller;

import cn.hutool.core.date.DateUtil;
import com.houliu.sys.common.AppFileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author houliu
 * @create 2020-01-09 01:13            文件上传
 */
@RestController
@RequestMapping("file")
public class FileController {

    /**
     * 文件上传
     */
    @RequestMapping("uploadFile")
    public Map<String, Object> uploadFile(MultipartFile mf){
        //1，得到文件名
        String oldName = mf.getOriginalFilename();
        //2，根据文件名得到新文件名
        String newName = AppFileUtils.createNewFileName(oldName);
        //3，得到当前日期的字符串
        String dirName = DateUtil.format(new Date(), "yyyy-MM-dd");
        //4,构造文件夹
        File dirFile = new File(AppFileUtils.UPLOAD_PATH, dirName);
        //5，判断文件是否存在
        if (!dirFile.exists()){
            dirFile.mkdir();
        }
        //6,构造文件对象
        File file = new File(dirFile, newName+"_temp");
        //7,把mf里的文件信息写入file
        try {
            mf.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //8,存入map
        Map<String, Object> map = new HashMap<>();
        map.put("path",dirName + "/" + newName+"_temp");
        return map;
    }

    /**
     * 图片下载
     */

    @RequestMapping("showImageByPath")
    public ResponseEntity<Object> showImageByPath(String path){
        return AppFileUtils.createResponseEntity(path);
    }

}
