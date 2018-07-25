package me.richard.note.net;

import android.provider.Settings;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.richard.note.PalmApp;
import me.richard.note.constants.AppConst;
import me.richard.note.util.LogUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 45820 on 2016/11/5.
 *
 * @Update 修改了一些 Request 参数 By James
 * @date 2017/12/13
 */
public class RetroManager {

    private static volatile RetroManager instance = null;
    private static final String algorithm = "MD5";
    public static final String secret = "fc803792672f9c04c1f5ed3df2cce3b2";

    private static final int DEFAULT_READ_TIMEOUT = 50 * 1000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 50 * 1000;
    private static final int DEFAULT_WRITE_TIMEOUT = 50 * 1000;

    private Retrofit retrofit;
    private Gson gson;
    private OkHttpClient client;

//    private Class<?> mClazz;
//
//    private String mMethodName;


    private RetroManager() {
        initGson();
        initOkHttpClient();
        initRetrofit();
    }

    public static RetroManager getInstance() {
        if (instance == null) {
            synchronized (RetroManager.class) {
                if (instance == null) {
                    instance = new RetroManager();
                }
            }
        }
        return instance;
    }

    private void initRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConst.getAPIUrl())
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.i("RequestInterceptor", "message====" + message);
        }
    });

    private void initOkHttpClient() {
        if (client == null) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(new RequestInterceptor())
                    .addInterceptor(logInterceptor)
                    .addNetworkInterceptor(new NetworkInterceptor())
                    .build();
        }
    }

    public class RequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            final long time = System.currentTimeMillis();

//            final String api = "/"+getApi();
            String api = "";
            try {
                List<String> pathSegments = new ArrayList<>();
                pathSegments.addAll(original.url().pathSegments());
                pathSegments.remove(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (String path : pathSegments) {
                    stringBuilder.append("/").append(path);
                }
                api = stringBuilder.toString();
            } catch (Exception e) {
                LogUtils.i("API 解析失败");
            }

            String token = "";
            try {
//                token = AppCache.getUserEntity().getToken().getToken();
            } catch (Exception e) {
                LogUtils.i("用户未登录");
            }

            Map<String, Object> requestMap = new HashMap<>();
            try {
                Request copy = original.newBuilder().build();
                Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                String requestJson = buffer.readUtf8();
                requestMap.clear();
                requestMap.putAll(gson.fromJson(requestJson, HashMap.class));
//                requestMap.putAll(JSON.parseObject(requestJson));
            } catch (final Exception e) {
                LogUtils.i("RequestBody解析错误");
            }

            Request request = original.newBuilder()
                    .addHeader("datetime", time + "")
                    .addHeader("api", api)
                    .addHeader("apptype", "android")
                    .addHeader("authorization", token)
                    .addHeader("device", Settings.Secure.getString(PalmApp.getContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                    .method(original.method(), original.body())
                    .build();

            LogUtils.i("RequestInterceptor", String.format("发送请求 %s ",
                    request.body()));
            return chain.proceed(request);
        }
    }



    public class NetworkInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() != 200) {
                // TODO 返回状态码不为200时处理
                LogUtils.i("response status is not 200");
            }
            return response;
        }
    }

//    private String getApi() {
//        String api = "";
//        Method[] methods = mClazz.getMethods();
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(POST.class) && method.getName().equals(mMethodName)) {
//                POST post = method.getAnnotation(POST.class);
//                api = post.value();
//            }
//        }
//        return api;
//    }

    private void initGson() {
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public <T> T createService(Class<T> clazz, String methodName) {
        return retrofit.create(clazz);
    }

    private String getURLEnCode(String target) {

        if (TextUtils.isEmpty(target)) {
            return "";
        }

        try {
            return URLEncoder.encode(target, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }

}
