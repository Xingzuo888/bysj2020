package com.example.bysj2020.event

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 信息事件
 */

/**
 * 0 保存个人信息
 * 1 修改手机号
 * 2 刷新收藏列表
 * 3 刷新景点订单列表
 * 4 刷新酒店订单列表
 */
class UserInfoEvent(val type: Int)