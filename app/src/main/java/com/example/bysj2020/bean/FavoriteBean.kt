package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/04/12
 *    Desc   : 收藏bean
 */

data class FavoriteBean(
    val current: Int, //当前页
    val records: List<FavoriteRecord>,
    val size: Int, //每页数量
    val total: Int //总条数
)

data class FavoriteRecord(
    val avatar: String, //收藏图
    val businessId: Int, //内容id
    val content: String, //内容
    val name: String, //收藏名字
    val recommend: String, //推荐语
    val type: String, //收藏类型
    val viewTime: String //显示时间
)