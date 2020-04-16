package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/03/28
 *    Desc   : 搜索列表景点bean
 */

/**
 * 景点
 */
data class SearchListSceneBean(
    val current: Int, //当前页
    val lastPage: Boolean, //是否最后一页
    val records: List<SceneRecord>, //返回数据
    val size: Int, //每页数量
    val total: Int //总条数
)

data class SceneRecord(
    val address: String, //地址
    val sceneId: Int, //景点id
    val name: String, //景点名
    val overview: String, //景点简略介绍
    val price: Int, //门票（以分为单位。如 1元 = 100分）
    val ranking: Int, //景点排名
    val reviewNum: Int, //评论数
    val smallImg: String, //小图
    val star: Int, ///景点星级
    val tag: String, //景点标签（多个标签 , 分割）
    val theme: String //主题
)

/**
 * 酒店
 */
data class SearchListHotelBean(
    val current: Int, //当前页
    val lastPage: Boolean, //是否最后一页
    val records: List<HotelRecord>, //返回数据
    val size: Int, //每页数量
    val total: Int //总条数
)

data class HotelRecord(
    val address: String, //地址
    val hotelId: Int, //酒店id
    val img: String, //酒店图
    val name: String, //酒店名
    val price: Int, //酒店价格
    val star: Int, //评分
    val tag: String //酒店标签
)