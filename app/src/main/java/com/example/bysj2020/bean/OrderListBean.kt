package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/04/13
 *    Desc   : 订单列表bean
 */

data class OrderListBean(
    val current: Int, //当前页
    val lastPage: Boolean, //是否最后一页
    val records: List<OrderListRecord>,
    val size: Int,
    val total: Int
)

data class OrderListRecord(
    val img: String,
    val name: String,
    val orderId: Int,
    val status: String, //状态
    val totalAmount: Int, //总金额
    val typeName: String //订单类型名
)