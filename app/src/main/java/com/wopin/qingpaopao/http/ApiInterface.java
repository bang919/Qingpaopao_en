package com.wopin.qingpaopao.http;


import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.ThirdBindRsp;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/17.
 */

public interface ApiInterface {

    @Streaming
    @GET
        //下载文件
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    @POST("users/login")
    Observable<LoginRsp> login(@Body LoginReq loginReq);

    @POST("users/logout")
    Observable<NormalRsp> logout();

    @POST("users/sendVerifyCode")
    Observable<NormalRsp> sendVerifyCode(@Body LoginReq loginReq);

    @POST("users/register")
    Observable<NormalRsp> register(@Body LoginReq loginReq);

    @POST("users/changePassword")
    Observable<NormalRsp> changePassword(@Body LoginReq loginReq);

    @POST("users/changePhone")
    Observable<NormalRsp> changePhone(@Body LoginReq loginReq);

    @POST("users/thirdRegister")
    Observable<NormalRsp> thirdRegister(@Body ThirdReq loginReq);

    @POST("users/thirdLogin")
    Observable<LoginRsp> thirdLogin(@Body ThirdReq loginReq);

    @POST("users/getThirdBinding")
    Observable<ThirdBindRsp> getThirdBinding();

    @POST("users/thirdBinding")
    Observable<NormalRsp> thirdBinding(@Body ThirdReq loginReq);

    @POST("users/changeIcon")
    Observable<NormalRsp> changeIcon(@Body ThirdReq loginReq);

    @POST("users/changeUserName")
    Observable<NormalRsp> changeUsername(@Body ThirdReq loginReq);

    @GET("https://public-api.wordpress.com/rest/v1.1/sites/wifi.h2popo.com/posts/?page=1&number=100&fields=ID,title,date,featured_image")
    Observable<ExploreListRsp> listExplores();

    @POST("users/drinklist")
    Observable<NormalRsp> getDrinkList();

    @POST("users/addOrUpdateACup")
    Observable<NormalRsp> addOrUpdateACup(@Body CupUpdateReq cupUpdateReq);

    @POST("users/deleteACup")
    Observable<NormalRsp> deleteACup(@Body CupUpdateReq cupUpdateReq);

    @GET("users/cupList")
    Observable<CupListRsp> getCupList();

    @GET("users/attendance")
    Observable<NormalRsp> attendance();

    @POST("http://172.16.0.123/wopin_wifi")
    Observable<String> getWifiList(@Body RequestBody requestBody);

    @POST("http://172.16.0.123/wopin_wifi")
    Observable<String> sendWifiConfigToCup(@Body RequestBody requestBody);
}
