package me.richard.note.net;

import android.content.Intent;


import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.richard.note.constants.AppConst;
import me.richard.note.net.api.OtherService;
import me.richard.note.net.entity.BaseEntity;
import me.richard.note.net.entity.WeatherEntity;
import me.richard.note.util.LogUtils;
import me.richard.note.util.StringUtils;
import me.richard.note.util.ToastUtils;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 发起网络请求的类
 *
 * @param <T>
 */
public abstract class HttpRequest<T extends BaseEntity> {

    private static final int ERROR = 0x02;

    private final String TAG = getClass().getSimpleName();

    private List<Observable> lastObservables = new ArrayList<>();

    public HttpRequest() {
    }

    public String createJson() {
        return "{}";
    }


    /**
     * 获取正常返回，子类重写此方法
     *
     * @param t
     */
    protected void onSuccess(T t) {

    }

    /**
     * 默认弹出 Toast ，子类要处理请求失败，重写此方法
     *
     * @param code
     * @param msg
     */
    protected void onFail(int code, String msg, BaseEntity entity) {
        CodeFilter.doFilter(code, entity);
        if (code == CodeFilter.Error_1011) {
            //这种错误提示不给用户
            return;
        }
        ToastUtils.makeToast(msg);
    }

    /**
     * 发起一般的网络请求
     *
     * @param clazz
     * @param methodName
     */
    public synchronized void start(Class<?> clazz, String methodName) {

        String json = createJson();

        if (StringUtils.isEmpty(json)) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(AppConst.MEDIA_TYPE_FORMAT_JSON), json);

        Observable observable = null;

        try {
            Method method = clazz.getMethod(methodName, RequestBody.class);
            Object service = RetroManager.getInstance().createService(clazz, methodName);
            LogUtils.i("methodName", methodName);
            observable = (Observable) method.invoke(service, requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (observable == null) {
            throw new IllegalArgumentException("observable can not be null");
        }
        subscriber(observable, subscriber);
    }

    /**
     * 上传图片
     */
    public synchronized void startFile(Class<?> clazz, String methodName, String[] fileName, String[] filePath) {

        String json = createJson();
        if (StringUtils.isEmpty(json)) {
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileName.length; i++) {
            File file = new File(filePath[i]);
            RequestBody requestBody = RequestBody.create(MediaType.parse(AppConst.MEDIA_TYPE_FORMAT_IMG), file);
            builder.addFormDataPart(fileName[i], file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        Observable observable = null;
        try {
            Method method = clazz.getMethod(methodName, MultipartBody.class);
            Object service = RetroManager.getInstance().createService(clazz, methodName);
            observable = (Observable) method.invoke(service, multipartBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (observable == null) {
            throw new IllegalArgumentException("observable can not be null");
        }
        subscriber(observable, subscriber);
    }

    /**
     * 上传多图片
     */
    public synchronized void startMultiFile(Class<?> clazz, String methodName, String fileName, List<String> filePath) {

        String json = createJson();
        if (StringUtils.isEmpty(json)) {
            return;
        }

        List<MultipartBody.Part> parts = new ArrayList<>(filePath.size());
        for (String path : filePath) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse(AppConst.MEDIA_TYPE_FORMAT_IMG), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(fileName, file.getName(), requestBody);
            parts.add(part);
        }
        Observable observable = null;
        try {
            Method method = clazz.getMethod(methodName, List.class);
            Object service = RetroManager.getInstance().createService(clazz, methodName);
            observable = (Observable) method.invoke(service, parts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (observable == null) {
            throw new IllegalArgumentException("observable can not be null");
        }
        subscriber(observable, subscriber);
    }

    private Subscriber<T> subscriber = new Subscriber<T>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtils.e(TAG, "error=========>" + e.getMessage());
            e.printStackTrace();
//            TCAgent.onError(PalmApp.getContext(), e);
            if (e instanceof HttpException) {
                onFail(ERROR, "服务器错误", null);
            } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                onFail(ERROR, "连接失败，请检查网络", null);
            } else {
                onFail(ERROR, e.getMessage(), null);
            }
        }

        @Override
        public void onNext(T t) {
//            MyLog.i(TAG, "message:=========>" + JsonUtils.toJson(t));
            handleResult(t);
        }
    };

    private synchronized static <T> void subscriber(Observable<T> o, Subscriber<? super T> subscriber) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void handleResult(T t) {
        if (t.getCode() == 0) {
            onSuccess((T) t);
        } else {
            onFail(t.getCode(), t.getMessage(), t);
        }

//        if(t.getBonusListEvent() != null){
//            Intent intent = new Intent();
//            intent.putExtra(App.EXTRA_DATA, (Serializable) t.getBonusListEvent());
//            intent.putExtra(App.EXTRA_TYPE, App.LuckPackageReceiver.TYPE_OPEN_LIST);
//            intent.setAction(App.RED_LUCK_UP);
//            PalmApp.getContext().sendBroadcast(intent);
//        }else if (t.getBonus() != null){
//            Intent intent = new Intent();
//            intent.putExtra(App.EXTRA_DATA, (Serializable) t.getBonus());
//            intent.putExtra(App.EXTRA_TYPE, App.LuckPackageReceiver.TYPE_JUST_OPEN);
//            intent.setAction(App.RED_LUCK_UP);
//            PalmApp.getContext().sendBroadcast(intent);
//        }
    }

    /**
     * 要取消本次请求，调用此方法
     */
    public void cancel() {
        subscriber.unsubscribe();
    }


    public static void startGetMethod(String baseUrl, Map<String, Object> params, Class clazz, String methodName, retrofit2.Callback callback){
        Retrofit retrofit = new Retrofit.Builder()
                //配置BaseUrl
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        try {
            Method method = clazz.getMethod(methodName, Map.class);
            Object service = retrofit.create(clazz);
            LogUtils.i("methodName", methodName);
            Call call = (Call) method.invoke(service, params);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
