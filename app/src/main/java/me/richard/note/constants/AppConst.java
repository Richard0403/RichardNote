package me.richard.note.constants;

/**
 * Created by James
 * Date 2017/12/7.
 * description
 */
public class AppConst {


    public static String getBaseUrl() {
        if (Config.IS_DEBUG) {
            return BASE_URL_TEST;
        }
        return BASE_URL;
    }

    public static String getAPIUrl(){
        return getBaseUrl()+"";
    }
    /**
     * 内部测试服务器地址
     */
    private static final String BASE_URL_TEST = "http://192.168.0.108:8080/";
    private static final String TEST_SERVER = "http://u.reader.myxiaoyou.net/";
    /**
     * 正式线上服务器地址
     * TODO 上线测试前请务必确认并填写
     */
    private static final String BASE_URL = "https://u.reader.myxiaoyou.top/";

    /**
     * 请求头数据传输格式
     */
    public static final String MEDIA_TYPE_FORMAT_JSON = "application/json; charset=utf-8";
    public static final String MEDIA_TYPE_FORMAT_IMG = "image/* ; charset=utf-8";
    public static final String MEDIA_TYPE_FORMAT_MP3 = "audio/mp3; charset=utf-8";

    /*知心天气 key*/
    public static final String WEATHER_KEY = "yejsxnymhwym0tgc";
    public static final String WEATHER_URL = "https://api.seniverse.com/";

}
