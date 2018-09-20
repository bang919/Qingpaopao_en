package com.wopin.qingpaopao.http;


import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalMoneyRsp;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalPeopleRsp;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.bean.response.ThirdBindRsp;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
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

    @GET("users/getUserData")
    Observable<LoginRsp> getUserData();

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

    @POST("users/drink")
    Observable<NormalRsp> drink(@Body RequestBody requestBody);

    @POST("users/getDrinkList")
    Observable<DrinkListTotalRsp> getDrinkList();

    @POST("users/getTodayDrinkList")
    Observable<DrinkListTodayRsp> getTodayDrinkList();

    @POST("users/addOrUpdateACup")
    Observable<NormalRsp> addOrUpdateACup(@Body CupUpdateReq cupUpdateReq);

    @POST("users/deleteACup")
    Observable<NormalRsp> deleteACup(@Body CupUpdateReq cupUpdateReq);

    @GET("users/cupList")
    Observable<CupListRsp> getCupList();

    @POST("users/attendance")
    Observable<NormalRsp> attendance();

    @POST("http://172.16.0.1/wopin_wifi")
    Observable<String> getWifiList(@Body RequestBody requestBody);

    @POST("http://172.16.0.1/wopin_wifi")
    Observable<String> sendWifiConfigToCup(@Body RequestBody requestBody);

    @GET("https://wifi.h2popo.com/wp-json/wc/v2/products/categories/{id}?consumer_key=ck_ba448661d94412faeaf3b8ab899b9294a967865c&consumer_secret=cs_45a5c655c2b07589e93a0466674af4afd4ef4abe")
    Observable<ProductBanner> getProductBanner(@Path("id") String id);

    @GET("https://wifi.h2popo.com/wp-json/wc/v2/products/?consumer_key=ck_ba448661d94412faeaf3b8ab899b9294a967865c&consumer_secret=cs_45a5c655c2b07589e93a0466674af4afd4ef4abe")
    Observable<ArrayList<ProductContent>> getProductContent(@Query("category") String id);

    @POST("goods/crowdfundingOrderTotalMoney")
    Observable<CrowdfundingOrderTotalMoneyRsp> crowdfundingOrderTotalMoney(@Body RequestBody requestBody);

    @POST("goods/crowdfundingOrderTotalPeople")
    Observable<CrowdfundingOrderTotalPeopleRsp> crowdfundingOrderTotalPeople(@Body RequestBody requestBody);

    @POST("users/addOrUpdateAddress")
    Observable<NormalRsp> addOrUpdateAddress(@Body AddressBean addressBean);

    @POST("users/setDefaultAddress")
    Observable<NormalRsp> setDefaultAddress(@Body AddressBean addressBean);

    @POST("goods/payMentScores")
    Observable<NormalRsp> payMentScores(@Body PaymentBean paymentBean);

    @POST("goods/payMentExchange")
    Observable<NormalRsp> payMentExchange(@Body PaymentBean paymentBean);

    @POST("goods/payMentCrowdfunding")
    Observable<NormalRsp> payMentCrowdfunding(@Body PaymentBean paymentBean);

    @GET("goods/scoresOrderList")
    Observable<OrderResponse> getScoresOrder();

    @GET("goods/exchangeOrderList")
    Observable<OrderResponse> getExchangeOrder();

    @GET("goods/crowdfundingOrderList")
    Observable<OrderResponse> getCrowdfundingOrder();

    @POST("goods/exchangeOrderUpdate")
    Observable<NormalRsp> exchangeOrderUpdate(@Body TrackingNumberSettingBean trackingNumberSettingBean);

    @POST("goods/deleteOrder")
    Observable<NormalRsp> deleteOrder(@Body RequestBody requestBody);
}
