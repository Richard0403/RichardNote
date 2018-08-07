package me.richard.note.constants;


import me.richard.note.BuildConfig;

/**
 * Created by James
 * Date 2017/12/4.
 * description
 */
public class Config {

    public static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public class API{

        /** 登录 **/
        public static final String LOGIN = "auth/register";
        /** 获取版本号**/
        public static final String VERSION = "version/getVersion";
    }
}
