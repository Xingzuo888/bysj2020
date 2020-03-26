package com.example.bysj2020.bean

import com.stx.xhb.androidx.entity.SimpleBannerInfo

/**
 *    Author : wxz
 *    Time   : 2020/03/05
 *    Desc   :
 */
class XBannerBean(title: String, url: Any) : SimpleBannerInfo() {
    var title = title
    var url = url
    override fun getXBannerUrl(): Any {
        return url
    }

    override fun getXBannerTitle(): String {
        return title
    }
}