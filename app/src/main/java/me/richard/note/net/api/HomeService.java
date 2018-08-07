package me.richard.note.net.api;


import me.richard.note.constants.Config;
import me.richard.note.net.entity.BaseEntity;
import me.richard.note.net.entity.BookInfoEntity;
import me.richard.note.net.entity.SignInEntity;
import me.richard.note.net.entity.VersionEntity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * By Richard on 2017/12/26.
 */

public interface HomeService {
    @POST(Config.API.LOGIN)
    Observable<SignInEntity> signIn(@Body RequestBody requestBody);

    @POST(Config.API.VERSION)
    Observable<VersionEntity> getVersion(@Body RequestBody requestBody);
}
