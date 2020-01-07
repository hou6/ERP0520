package com.houliu.sys.common;

/**
 * @author houliu
 * @create 2019-12-30 14:36
 */
public class Constast {

    /**
     * 状态吗   200成功    -1失败
     */
    public static final Integer OK = 200;
    public static final Integer ERROR = -1;

    /**
     * 菜单类型
     */
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_PERMISSION = "permission";

    /**
     * 可用类型  1可用    0不可用
     */
    public static final Integer AVALIABLE_TRUE = 1;
    public static final Integer AVALIABLE_FALSE = 0;

    /**
     * 用户类型   0超级管理员   1普通用户
     */
    public static final Integer USER_TYPE_SUPER = 0;
    public static final Integer USER_TYPE_NORMAL = 1;

    /**
     * 展开类型，true展开   false不展开
     */
    public static final Integer OPEN_TRUE = 1;
    public static final Integer OPEN_FALSE = 0;

    /**
     * 用户默认密码
     */
    public static final String USER_DEFAULT_PWD = "123456";

}
