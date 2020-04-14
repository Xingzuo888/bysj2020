package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/04/05
 *    Desc   : 景点详情bean
 */

data class SceneDetailsBean(
    val address: String, //地址
    val bookNotice: List<BookNotice>, //预订须知
    val city: String, //城市
    val imgRespList: List<ImgResp>, //图片
    val isSave: Boolean, //是否收藏
    val name: String, //景点名
    val openTime: String, //营业时间
    val overview: String, //景点简略介绍
    val price: Int, //门票（以分为单位。如 1元 = 100分）
    val ranking: Int, //景点排名
    val recommend: String, //推荐
    val rule: String, //入景规则
    val sceneTickInfoList: List<SceneTickInfo>, //门票
    val star: Int, //景点星级
    val tag: String, //景点标签（多个标签 , 分割）
    val theme: String, //主题
    val trafficBus: String //交通信息
)

data class BookNotice(
    val key: String,
    val name: String,
    val value: String
)

data class ImgResp(
    val imgName: String,
    val imgPath: String
)

data class SceneTickInfo(
    val id: Int,
    val name: String,
    val notice: String, //注意事项
    val price: Int,
    val tag: String
)

data class SceneTickInfos(
    val id: Int,
    val name: String,
    var num: Int = 0,
    val notice: String, //注意事项
    val price: Int,
    val tag: String
)