package com.example.bysj2020.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.activity.Home
import com.example.bysj2020.utils.SpUtil
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton

/**
 *    Author : wxz
 *    Time   : 2020/02/18
 *    Desc   : 引导页
 */
class Guide : Fragment() {
    protected var mActivity:Activity?=null
    private var top_iv:ImageView?=null
    private var guide_title:TextView?=null
    private var guide_content:TextView?=null
    private var guide_login: QMUIRoundButton?=null
    private var guide_jump: TextView?=null
    private var position:Int=0
    private val topImg = listOf(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3)
    private val title = listOf("山水之间 灵动之美", "汇天地之灵气 成非人之造化", "鬼斧神工 吾等何处不可去")

    companion object {
        fun newInstance(position: Int): Guide {
            val fragment = Guide()
            val args = Bundle()
            args.putInt("position",position)
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position=arguments!!.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_guide,container,false)
        top_iv=view.findViewById(R.id.top_iv)
        guide_title=view.findViewById(R.id.guide_title)
        guide_content=view.findViewById(R.id.guide_content)
        guide_login=view.findViewById(R.id.guide_login)
        guide_jump=view.findViewById(R.id.guide_jump)
        top_iv?.setImageResource(topImg[position])
        guide_title?.text=title[position]
        if (position == 2) {
            guide_login?.visibility = View.VISIBLE
            guide_jump?.visibility = View.VISIBLE
        } else {
            guide_login?.visibility=View.GONE
            guide_jump?.visibility=View.GONE
        }
        guide_login?.setOnClickListener {
            SpUtil.Save(activity,"isFirstLaunch",false)
            ///todo 跳转

            activity?.finish()
        }
        guide_jump?.setOnClickListener{
            SpUtil.Save(activity,"isFirstLaunch",false)
            startActivity(Intent(activity,Home::class.java))
            activity?.finish()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity=context as Activity?
    }

    override fun onDetach() {
        super.onDetach()
        mActivity=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}