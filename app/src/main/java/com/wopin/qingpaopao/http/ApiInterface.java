package com.wopin.qingpaopao.http;


import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.request.BodyProfilesBean;
import com.wopin.qingpaopao.bean.request.CupColorReq;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.request.GetWechatPayReq;
import com.wopin.qingpaopao.bean.request.LocalBean;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.NewBlogReq;
import com.wopin.qingpaopao.bean.request.PaySuccessChangeOrderReq;
import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.request.SendCommentReq;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.BlogPostRsp;
import com.wopin.qingpaopao.bean.response.CheckNewMessageRsp;
import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalMoneyRsp;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalPeopleRsp;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.bean.response.FollowListRsp;
import com.wopin.qingpaopao.bean.response.GetWechatPayResponse;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderListResponse;
import com.wopin.qingpaopao.bean.response.OrderOneResponse;
import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.bean.response.ThirdBindRsp;
import com.wopin.qingpaopao.bean.response.UploadImageRsp;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<NormalRsp> changeIcon(@Body RequestBody changeIconBody);

    @POST("users/changeUserName")
    Observable<NormalRsp> changeUsername(@Body ThirdReq loginReq);

    @GET("blog/hotPosts")
    Observable<ExploreListRsp> listHotExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/posts")
    Observable<ExploreListRsp> listNewlyExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/myPosts")
    Observable<ExploreListRsp> listMyExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/followPosts")
    Observable<ExploreListRsp> listFollowExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/colletionPosts")
    Observable<ExploreListRsp> listColletionExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/historyPosts")
    Observable<ExploreListRsp> listHistoryExplores(@Query("page") int page, @Query("num") int num);

    @GET("blog/likedPosts")
    Observable<ExploreListRsp> listMyLikeExplores(@Query("page") int page, @Query("num") int num);

    @POST("blog/searchPosts")
    Observable<ExploreListRsp> searchExplores(@Body RequestBody requestBody);

    @GET("blog/postComments/{id}")
    Observable<CommentRsp> getComments(@Path("id") String exploreid);

    @POST("blog/deletePost")
    Observable<NormalRsp> deleteMyBlog(@Body RequestBody requestBody);

    @POST("blog/newComment")
    Observable<NormalRsp> sendComment(@Body SendCommentReq sendCommentReq);

    @POST("blog/newPost")
    Observable<NormalRsp> newBlog(@Body NewBlogReq newBlogReq);

    @GET("blog/post/{id}")
    Observable<BlogPostRsp> getBlogPost(@Path("id") String exploreid);

    @POST("blog/follow")
    Observable<NormalRsp> followAuthor(@Body RequestBody requestBody);

    @POST("blog/unFollow")
    Observable<NormalRsp> unFollowAuthor(@Body RequestBody requestBody);

    @GET("blog/myFollowList")
    Observable<FollowListRsp> getMyFollowList();

    @GET("blog/fansList")
    Observable<FollowListRsp> getMyFansList();

    @POST("blog/likeComment")
    Observable<NormalRsp> likeBlogComment(@Body RequestBody requestBody);

    @POST("blog/unLikeComment")
    Observable<NormalRsp> unlikeBlogComment(@Body RequestBody requestBody);

    @POST("blog/collect")
    Observable<NormalRsp> collectBlogPost(@Body RequestBody requestBody);

    @POST("blog/unCollect")
    Observable<NormalRsp> uncollectBlogPost(@Body RequestBody requestBody);

    @POST("blog/like")
    Observable<NormalRsp> likeBlogPost(@Body RequestBody requestBody);

    @POST("blog/unLike")
    Observable<NormalRsp> unlikeBlogPost(@Body RequestBody requestBody);


    @POST("users/drink")
    Observable<NormalRsp> drink(@Body RequestBody requestBody);

    @POST("users/getDrinkList")
    Observable<DrinkListTotalRsp> getDrinkList();

    @POST("users/getTodayDrinkList")
    Observable<DrinkListTodayRsp> getTodayDrinkList();

    @POST("users/addOrUpdateACup")
    Observable<NormalRsp> addOrUpdateACup(@Body CupUpdateReq cupUpdateReq);

    @POST("users/updateCupColor")
    Observable<NormalRsp> updateCupColor(@Body CupColorReq cupUpdateReq);

    @POST("users/deleteACup")
    Observable<NormalRsp> deleteACup(@Body CupUpdateReq cupUpdateReq);

    @GET("users/cupList")
    Observable<CupListRsp> getCupList();

    @POST("users/attendance")
    Observable<NormalRsp> attendance();

    //    @POST("http://172.16.0.123/wopin_wifi")
    @POST("http://172.16.0.1/wopin_wifi")
    Observable<String> getWifiList(@Body RequestBody requestBody);

    //    @POST("http://172.16.0.123/wopin_wifi")
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

    @POST("users/delAddress")
    Observable<NormalRsp> deleteAddress(@Body AddressBean addressBean);

    @POST("goods/payMentScores")
    Observable<NormalRsp> payMentScores(@Body PaymentBean paymentBean);

    @POST("goods/payMentExchange")
    Observable<OrderOneResponse> payMentExchange(@Body PaymentBean paymentBean);

    @POST("goods/payMentCrowdfunding")
    Observable<OrderOneResponse> payMentCrowdfunding(@Body PaymentBean paymentBean);

    @GET("goods/scoresOrderList")
    Observable<OrderListResponse> getScoresOrder();

    @GET("goods/exchangeOrderList")
    Observable<ResponseBody> getExchangeOrder();

    @GET("goods/crowdfundingOrderList")
    Observable<OrderListResponse> getCrowdfundingOrder();

    @POST("goods/exchangeOrderUpdate")
    Observable<NormalRsp> exchangeOrderUpdate(@Body TrackingNumberSettingBean trackingNumberSettingBean);

    @POST("goods/orderStatusUpdate")
    Observable<NormalRsp> paySuccessChangeOrderStatus(@Body PaySuccessChangeOrderReq paySuccessChangeOrderReq);

    @POST("goods/deleteOrder")
    Observable<NormalRsp> deleteOrder(@Body RequestBody requestBody);

    @POST("users/updateBodyProfiles")
    Observable<LoginRsp> updateBodyProfiles(@Body BodyProfilesBean bodyProfilesBean);

    @Multipart
    @Headers({"mimetype:image/jpeg"})
    @POST("uploadImage")
    Observable<UploadImageRsp> uploadPicture(@Part MultipartBody.Part file);

    @POST("getWeChatPaySign")
    Observable<GetWechatPayResponse> getWeChatPaySign(@Body GetWechatPayReq getWechatPayReq);

    @POST("local")
    Observable<NormalRsp> sendLocal(@Body LocalBean localBean);

    @GET("blog/checkNewMessage")
    Observable<CheckNewMessageRsp> checkNewMessage();

    @GET("blog/sysMessage")
    Observable<SystemMessageRsp> getSystemMessage();

    @GET("blog/newBlogMessage")
    Observable<NewCommentRsp> getNewBlogMessage();

    @GET("blog/myComments")
    Observable<MyCommentsRsp> getMyComments(@Query("page") int page, @Query("num") int num);

    @POST("blog/deleteComment")
    Observable<NormalRsp> deleteComment(@Body RequestBody requestBody);
}
