package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/04/04
 *    Desc   : 酒店详情bean
 */

data class HotelDetailsBean(
    val address: String, //地址
    val debutYear: String, //开业时间
    val decorateDate: String, //装修时间
    val facilityInfoList: List<FacilityInfo>, //设施项目
    val hotelPolicyInfo: HotelPolicyInfo, //酒店设施
    val hotelRoomInfoList: List<HotelRoomInfo>, //酒店房间
    val hotelServiceInfoList: List<HotelServiceInfo>, //酒店服务
    val img: List<Img>, //图片
    val isSave: Boolean,//是否收藏
    val name: String, //酒店名
    val overview: String, //酒店介绍
    val policy: String, //入住政策
    val roomCount: Int, //房间数量
    val searchNum: Int, //搜索次数
    val star: Int, //分数
    val starName: String, //星级名称
    val tag: String, //酒店标签
    val tel: String //联系电话
)

data class FacilityInfo(
    val code: String,
    val name: String, //名字
    val status: String, //状态
    val typeCode: String,
    val typeName: String //类型名
)

data class HotelPolicyInfo(
    val arrivalDeparture: String, //入住离开时间
    val cancel: String, //取消
    val checkInTime: String, //入住相关
    val checkOutTime: String, //退房相关
    val children: String, //儿童
    val depositPrepaid: String, //定金支付
    val pet: String, //宠物
    val requirements: String //需知
)

data class HotelRoomInfo(
    val id: Int, //房间id
    val img: String, //图片
    val price: Int, //价格 分单位
    val roomSize: String, //房间大小
    val roomTag: String, //房间标签
    val roomType: String //房间类型
)

data class HotelServiceInfo(
    val code: String,
    val name: String,
    val status: String, //状态
    val typeCode: String,
    val typeName: String
)

data class Img(
    val imgName: String, //图片名
    val imgPath: String //图片地址
)

data class HotelPolicyBean(
    val arrivalDeparture: String,
    val cancel: String,
    val checkInTime: String,
    val checkOutTime: String,
    val children: String,
    val depositPrepaid: String,
    val pet: String,
    val requirements: String
)

data class HotelRoomInfos(
    val id: Int, //房间id
    val img: String, //图片
    var num: Int = 0,
    val price: Int, //价格 分单位
    val roomSize: String, //房间大小
    val roomTag: String, //房间标签
    val roomType: String //房间类型
)