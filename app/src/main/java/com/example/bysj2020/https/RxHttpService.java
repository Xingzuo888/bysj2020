package com.example.bysj2020.https;

import com.example.bysj2020.bean.FAddressBean;
import com.example.bysj2020.bean.FHomeBean;
import com.example.bysj2020.bean.FHomeSceneBean;
import com.example.bysj2020.bean.FavoriteBean;
import com.example.bysj2020.bean.HotelDetailsBean;
import com.example.bysj2020.bean.ImgCodeBean;
import com.example.bysj2020.bean.LoginBean;
import com.example.bysj2020.bean.OrderDetailsBean;
import com.example.bysj2020.bean.OrderListBean;
import com.example.bysj2020.bean.PayInfoBean;
import com.example.bysj2020.bean.SceneDetailsBean;
import com.example.bysj2020.bean.SearchListHotelBean;
import com.example.bysj2020.bean.SearchListSceneBean;
import com.example.bysj2020.bean.SearchRecommendBean;
import com.example.bysj2020.bean.UserInfoBean;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RxHttpService {

    /*----------关于登录接口----------*/

    //获取登录短信验证码
    @POST("sendLoginSms")
    Observable<HttpBean<String>> sendLoginSms(@Body JsonObject body);

    //短信验证码登录
    @POST("loginPhoneCode")
    Observable<HttpBean<LoginBean>> loginPhoneCode(@Body JsonObject body);

    //密码登录
    @POST("loginAccount")
    Observable<HttpBean<LoginBean>> loginAccount(@Body JsonObject body);

    //获取图片验证码
    @POST("verifyCode")
    Observable<HttpBean<ImgCodeBean>> verifyCode(@Body JsonObject body);

    //退出登录
    @POST("loginOut")
    Observable<HttpBean<String>> loginOut(@Body JsonObject body);


    /*----------关于注册接口----------*/

    //获取注册短信验证码
    @POST("sendRegsSms")
    Observable<HttpBean<String>> sendRegsSms(@Body JsonObject body);

    //注册
    @POST("regsMobileCode")
    Observable<HttpBean<String>> regsMobileCode(@Body JsonObject body);

    /*----------关于密码接口----------*/

    //获取忘记密码短信验证码
    @POST("passwordOpt/sendSmsForgetPassword")
    Observable<HttpBean<String>> sendSmsForgetPassword(@Body JsonObject body);

    //获取修改密码短信验证码
    @POST("passwordOpt/sendSmsModifyPassword")
    Observable<HttpBean<String>> sendSmsModifyPassword(@Body JsonObject body);

    //忘记密码
    @POST("passwordOpt/forgetPassword")
    Observable<HttpBean<String>> forgetPassword(@Body JsonObject body);

    //修改密码
    @POST("passwordOpt/modifyPassword")
    Observable<HttpBean<String>> modifyPassword(@Body JsonObject body);


    /*----------关于页面初始化接口----------*/

    //获取个人信息
    @GET("index/basicUserInfo")
    Observable<HttpBean<UserInfoBean>> basicUserInfo(@QueryMap Map<String, Objects> map);

    //获取首页信息
    @GET("index/homeIndex")
    Observable<HttpBean<FHomeBean>> homeIndex(@QueryMap Map<String, Objects> map);

    //获取首页景点信息
    @GET("index/homeIndexScene")
    Observable<HttpBean<FHomeSceneBean>> homeIndexScene(@QueryMap Map<String, Objects> map);

    //获取搜索推荐信息
    @GET("index/searchRecommend")
    Observable<HttpBean<SearchRecommendBean>> searchRecommend(@QueryMap Map<String, Objects> map);

    //获取目的地信息
    @GET("index/mdd")
    Observable<HttpBean<FAddressBean>> mdd(@QueryMap Map<String, Objects> map);


    /*----------关于用户----------*/

    //获取更改手机号验证码
    @POST("axUser/modifyBindPhoneSendSms")
    Observable<HttpBean<String>> modifyBindPhoneSendSms(@Body JsonObject body);

    //更改手机号
    @POST("axUser/modifyBindPhone")
    Observable<HttpBean<String>> modifyBindPhone(@Body JsonObject body);

    //修改用户信息
    @POST("axUser/modifyUserInfo")
    Observable<HttpBean<String>> modifyUserInfo(@Body JsonObject body);

    /*----------景点-----------*/

    //获取搜索列表的景点
    @GET("scene/searchScene")
    Observable<HttpBean<SearchListSceneBean>> searchScene(@QueryMap Map<String, Objects> map);

    //获取景点详情
    @GET("scene/sceneDetail")
    Observable<HttpBean<SceneDetailsBean>> sceneDetail(@QueryMap Map<String, Objects> map);

    /*----------酒店-----------*/

    //获取搜索列表的酒店
    @GET("hotel/searchHotel")
    Observable<HttpBean<SearchListHotelBean>> searchHotel(@QueryMap Map<String, Objects> map);

    //获取酒店详情
    @GET("hotel/hotelDetail")
    Observable<HttpBean<HotelDetailsBean>> hotelDetail(@QueryMap Map<String, Objects> map);

    /*----------用户收藏-----------*/

    //收藏景点
    @POST("userSave/saveScene")
    Observable<HttpBean<String>> saveScene(@Body JsonObject body);

    //收藏酒店
    @POST("userSave/saveHotel")
    Observable<HttpBean<String>> saveHotel(@Body JsonObject body);

    //收藏列表
    @GET("userSave/saveList")
    Observable<HttpBean<FavoriteBean>> saveList(@QueryMap Map<String, Objects> map);

    /*----------支付接口-----------*/

    //创建景点门票订单
    @POST("pay/createSceneOrder")
    Observable<HttpBean<PayInfoBean>> createSceneOrder(@Body JsonObject body);

    //创建酒店房间订单
    @POST("pay/createHotelOrder")
    Observable<HttpBean<PayInfoBean>> createHotelOrder(@Body JsonObject body);

    //取消订单
    @POST("pay/cancelOrder")
    Observable<HttpBean<String>> cancelOrder(@Body JsonObject body);

    //继续支付订单
    @POST("pay/continueOrder")
    Observable<HttpBean<PayInfoBean>> continueOrder(@Body JsonObject body);

    //订单列表
    @GET("pay/orderList")
    Observable<HttpBean<OrderListBean>> orderList(@QueryMap Map<String, Objects> map);

    //获取订单详情
    @GET("pay/orderDetail")
    Observable<HttpBean<OrderDetailsBean>> orderDetail(@QueryMap Map<String, Objects> map);
}
