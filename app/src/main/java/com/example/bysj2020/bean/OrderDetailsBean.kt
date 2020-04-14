package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/04/13
 *    Desc   : 订单详情bean
 */

data class OrderDetailsBean(
    val bizNo: String, //订单编号
    val bookTime: String, //预定时间
    val img: String, //图片
    val name: String, //名字
    val orderId: Int, //订单id
    val realIdCard: String, //预定身份证
    val realName: String, //预定人
    val realPhone: String, //预定手机号
    val status: String, //状态  PAYING:支付中,SUCCESS:支付成功,FAIL:支付失败,REFUND:已取消,TIME_OUT:已过期,NOT_EXISTS:不存在
    val totalNum: Int, //数量
    val typeName: String, //类型名
    val validTime: Long //过期时间
)