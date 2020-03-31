package com.example.bysj2020.event

/**
 *    Author : wxz
 *    Time   : 2020/03/30
 *    Desc   : 定位事件
 */

/**
 * 0 获取详细地址详细
 * 1 获取国家
 * 2 获取省份
 * 3 获取城市
 * 4 获取区县
 *
 */
class LocationEvent(val code: Int, val str: String)