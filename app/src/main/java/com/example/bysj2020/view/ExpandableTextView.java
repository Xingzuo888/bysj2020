package com.example.bysj2020.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bysj2020.R;
import com.example.bysj2020.utils.LogUtil;


/**
 * 可展开/收起的文本控件
 */
public class ExpandableTextView extends LinearLayout {

    private final String TAG = "ExpandableTextView";

    /**
     * 显示内容的文本控件
     */
    private TextView mContentTextView;
    /**
     * 全文/收起 按钮
     */
    private TextView mExpansionButton;

    /**
     * 最大显示行数（默认 3 行）
     */
    private int mMaxLine = 3;
    /**
     * 显示的内容
     */
    private CharSequence mContent;

    /**
     * 当前是否已是 "全文" 状态
     */
    private boolean mIsExpansion;

    private int textSize = 16;

    private String open = "收起";
    private String close = "全文";


    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs);
        initView(context);
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxLine = array.getInteger(R.styleable.ExpandableTextView_mMaxLine, mMaxLine);
        mContent = array.getString(R.styleable.ExpandableTextView_text);


        array.recycle();
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.view_expandable, this);

        mContentTextView = findViewById(R.id.tv_content);
        mExpansionButton = findViewById(R.id.v_expansion);

        getTextSize(textSize);

        // 监听文本控件的布局绘制
        mContentTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mContentTextView.getWidth() == 0) {
                    return;
                }
                mContentTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 文本控件绘制成功后, 更新文本
                setText(mContent);
            }
        });

        // "全文/收起" 按钮点击监听
        mExpansionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpansionStatus();
            }
        });

        // 默认隐藏 "全文/收起" 按钮
        mExpansionButton.setVisibility(GONE);
    }

    /**
     * 切换 全文/收起 状态
     */
    private void toggleExpansionStatus() {
        // 切换状态
        mIsExpansion = !mIsExpansion;

        // 更新内容和切换按钮的显示
        if (mIsExpansion) {
            mExpansionButton.setText(open);                             // 全文状态, 按钮显示 "收起"
            mContentTextView.setMaxLines(Integer.MAX_VALUE);              // 全文状态, 行数设置为最大
        } else {
            mExpansionButton.setText(close);                             // 收起状态, 按钮显示 "全文"
            mContentTextView.setMaxLines(mMaxLine);                       // 收起状态, 最大显示指定的行数
        }
    }

    /**
     * 设置收起状态最大允许显示的行数（超过该行数显示 "全文" 按钮）
     *
     * @param maxLine
     */
    public void setMaxLine(int maxLine) {
        this.mMaxLine = maxLine;
        setText(mContent);                                      // 更新状态, 重新显示文本
    }

    /**
     * 设置展开后的文本
     *
     * @param open
     */
    public void setOpen(String open) {
        this.open = open;
        setText(mContent);                                      // 更新状态, 重新显示文本
    }

    /**
     * 设置展开前的文本
     *
     * @param close
     */
    public void setClose(String close) {
        this.close = close;
        setText(mContent);                                      // 更新状态, 重新显示文本
    }

    private void getTextSize(int textSize) {
        LogUtil.d(TAG, textSize + "");
        mContentTextView.setTextSize(textSize);
    }

    /**
     * 设置文本内容的大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        getTextSize(textSize);
    }

    /**
     * 设置需要显示的文本
     */
    public void setText(CharSequence text) {
        mContent = text;

        // 文本控件有宽度时（绘制成功后）才能获取到文本显示的所需要的行数,
        // 如果控件还没有被绘制, 等监听到绘制成功后再设置文本
        if (mContentTextView.getWidth() == 0) {
            return;
        }
        // 默认先设置最大行数为最大值（即不限制行数）
        mContentTextView.setMaxLines(Integer.MAX_VALUE);
        // 设置文本
        mContentTextView.setText(mContent);

        // 设置完文本后, 获取显示该文本所需要的行数
        int lineCount = mContentTextView.getLineCount();

        if (lineCount > mMaxLine) {
            // 行数超过显示, 显示 "全文" 按钮
            mExpansionButton.setVisibility(VISIBLE);
            mExpansionButton.setText(close);
            // 设置文本控件的最大允许显示行数
            mContentTextView.setMaxLines(mMaxLine);
            mIsExpansion = false;
        } else {
            // 行数没有超过限制, 不需要显示 "全文/收起" 按钮
            mExpansionButton.setVisibility(GONE);
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
