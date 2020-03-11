package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/03/09
 *    Desc   : 城市选择bean
 */

/**
 * 城市选择省份
 */
class AreaProvincesBean(
    var code: String?,
    var name: String?,
    var enName: String?,
    var citys: List<AreaCityBean>?
)

/**
 * 城市选择市或区
 */
class AreaCityBean {
    var code: String? = null
    var name: String? = null
    var enName: String? = null
    var showName: String? = null //显示的名字
    var isCheck: Boolean = false

    constructor(code: String?, name: String?, enName: String?, isCheck: Boolean = false) {
        this.code = code
        this.name = name
        this.enName = enName
        this.isCheck = isCheck
    }

    constructor(
        code: String?,
        name: String?,
        enName: String?,
        showName: String?,
        isCheck: Boolean = false
    ) {
        this.code = code
        this.name = name
        this.enName = enName
        this.showName = showName
        this.isCheck = isCheck
    }
}