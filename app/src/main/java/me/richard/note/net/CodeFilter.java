package me.richard.note.net;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.richard.note.net.entity.BaseEntity;
import me.richard.note.util.LogUtils;

/**
 * By Richard on 2018/1/3.
 */

public class CodeFilter {
    public static final int Error_1000 = -1000;//未知错误[{0}]！
    public static final int Error_1001 = -1001;//验证码缺失！
    public static final int Error_1002 = -1002;//验证码[{0}]错误！
    public static final int Error_1003 = -1003;//验证码发送失败！
    public static final int Error_1004 = -1004;//参数[{0}]错误！
    public static final int Error_1005 = -1005;//参数[{0}]缺失！
    public static final int Error_1006 = -1006;//无权限[{0}]！
    public static final int Error_1007 = -1007;//登陆过期，请重新登陆！
    public static final int Error_1008 = -1008;//已在其他设备登陆，请退出或重新登陆！
    public static final int Error_1009 = -1009;//尚未登陆，请登陆！
    public static final int Error_1010 = -1010;//过期的请求，拒绝访问！
    public static final int Error_1011 = -1011;//已使用的请求，拒绝访问！
    public static final int Error_1012 = -1012;//非法的请求，拒绝访问！
    public static final int Error_1013 = -1013;//登陆异常！
    public static final int Error_1014 = -1014;//业务异常！
    public static final int Error_1015 = -1015;//无权限，用户状态[{0}]！
    public static final int Error_1016 = -1016;//尚未申请临时身份或临时身份已过期！
    public static final int Error_1022 = -1022;//手机号已被使用
    public static final int Error_1101 = -1101;//用户[{0}]尚未注册或者密码错误！
    public static final int Error_1102 = -1102;//用户[{0}]已被暂停使用！
    public static final int Error_1103 = -1103;//该微信尚未绑定手机号！
    public static final int Error_1104 = -1104;//该QQ尚未绑定手机号！
    public static final int Error_1105 = -1105;//无效登录！
    public static final int Error_1201 = -1201;//该手机号[{0}]已注册！
    public static final int Error_1202 = -1202;//该手机号[{0}]已被暂停使用！
    public static final int Error_1203 = -1203;//该手机号[{0}]已与其他微信绑定！
    public static final int Error_1204 = -1204;//该手机号[{0}]已与其他QQ绑定！
    public static final int Error_1205 = -1205;//该微信[{0}]已被其他手机号绑定！
    public static final int Error_1206 = -1206;//该QQ[{0}]已被其他手机号绑定！
    public static final int Error_1207 = -1207;//无效注册！
    public static final int Error_1208 = -1208;//微信授权无效！
    public static final int Error_1209 = -1209;//QQ授权无效！
    public static final int Error_1210 = -1210;//邀请码[{0}]不存在！
    public static final int Error_1211 = -1211;//邀请人[{0}]已被封号！

    public static final int Error_1021 = -1021;//对象已经删除
    private static final String TAG = "CodeFilter";

    /**
     * @param code
     * @param entity
     */
    public static void doFilter(int code, BaseEntity entity) {
        try {
            switch (code) {
                default:
                    break ;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "BaseEntity transformation wrong");
        }
    }


}
