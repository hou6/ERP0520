package com.houliu.sys.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.IdUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author houliu
 * @create 2020-01-09 01:30        文件上传下载工具类
 */
public class AppFileUtils {

    public static String UPLOAD_PATH = "/Users/资料整理/uploadFile";  //默认值

    static {
        //读取配置文件的存储地址
        InputStream inputStream = AppFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String property = properties.getProperty("filepath");
        if (null != properties){
            UPLOAD_PATH = property;
        }

    }

    /**
     * 根据文件老名字得到新的文件名
     * @param oldName
     * @return
     */
    public static String createNewFileName(String oldName) {
//        String stuff = StringUtils.substringAfterLast(oldName, ".");
        String stuff = oldName.substring(oldName.lastIndexOf("."), oldName.length());
        return IdUtil.simpleUUID().toUpperCase() + stuff;
    }

    /**
     * 图片下载,死代码
     * @param path
     * @return
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        File file = new File(UPLOAD_PATH, path);
        if (file.exists()) {
            //将下载的文件封装byte
            byte[] bytes = null;
            try {
                bytes = FileUtil.readBytes(file);
            } catch (IORuntimeException e) {
                e.printStackTrace();
            }
            //创建封装响应头信息的对象
            HttpHeaders httpHeaders = new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //创建ResponseEntity对象
            ResponseEntity<Object> responseEntity = new ResponseEntity<>(bytes, httpHeaders, HttpStatus.CREATED);
            return responseEntity;
        }
        return null;
    }

    /**
     * 根据路径改名字，去掉_temp
     * @param goodsimg
     * @return
     */
    public static String renameFile(String goodsimg) {
        File file = new File(UPLOAD_PATH, goodsimg);
        String replace = goodsimg.replace("_temp", "");
        if (file.exists()){
            file.renameTo(new File(UPLOAD_PATH,replace));
        }
        return replace;
    }

    /**
     * 根据老路径删除图片
     * @param oldPath
     */
    public static void removeFileByPath(String oldPath) {
        //图片的路径不是默认图片的路径
        if (!oldPath.equals(Constast.DEFAULT_IMG)){
            File file = new File(UPLOAD_PATH,oldPath);
            if (file.exists()){
                file.delete();
            }
        }
    }

}

