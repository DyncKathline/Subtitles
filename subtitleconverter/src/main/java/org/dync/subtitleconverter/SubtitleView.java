package org.dync.subtitleconverter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.dync.subtitleconverter.subtitleFile.Caption;
import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

import java.util.TreeMap;

/**
 * @date 2017/9/20
 * @Auther jixiongxu
 * @descraptio 显示字幕的图层
 */
public class SubtitleView extends LinearLayout implements ISubtitleControl, SubtitleClickListener {
    private final String TAG = getClass().getSimpleName();
    /**
     * 只显示中文
     */
    public final static int LANGUAGE_TYPE_CHINA = 0;

    /**
     * 只显示英文
     */
    public final static int LANGUAGE_TYPE_ENGLISH = LANGUAGE_TYPE_CHINA + 1;

    /**
     * 双语显示
     */
    public final static int LANGUAGE_TYPE_BOTH = LANGUAGE_TYPE_ENGLISH + 1;

    /**
     * 不显示字幕
     */
    public final static int LANGUAGE_TYPE_NONE = LANGUAGE_TYPE_BOTH + 1;

    /**
     * 更新UI
     */
    private static int UPDATE_SUBTITLE = LANGUAGE_TYPE_NONE + 1;

    /**
     * 中文字幕
     */
    private SubtitleTextView subChina;

    /**
     * 英文字幕
     */
    private SubtitleTextView subEnglish;

    /**
     * 当前显示节点
     */
    private View subTitleView;

    /**
     * 字幕数据
     */
    private TimedTextObject model = null;

    /**
     * 单条字幕数据
     */
    private Caption caption = null;

    /**
     * 后台播放
     */
    private boolean palyOnBackground = false;

    private Context context;

    public SubtitleView(Context context) {
        super(context);
        init(context);
    }

    public SubtitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public SubtitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SubtitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        subTitleView = View.inflate(context, R.layout.subtitleview, null);
        subChina = subTitleView.findViewById(R.id.subTitleChina);
        subEnglish = subTitleView.findViewById(R.id.subTitleenglish);
        subChina.setSubtitleOnTouchListener(this);
        subEnglish.setSubtitleOnTouchListener(this);
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.BOTTOM);
        this.addView(subTitleView);
    }

    @Override
    public void setItemSubtitleChina(String item) {
//        subChina.setText(item);
        subChina.setText(Html.fromHtml(item));
    }

    @Override
    public void setItemSubtitleEnglish(String item) {
        subEnglish.setText(item);
//        subEnglish.setText(Html.fromHtml(item));
    }

    @Override
    public void seekTo(int position) {
        if (model != null && !model.captions.isEmpty()) {
            caption = searchSub(model.captions, position);
        }
        if (caption != null) {
            setItemSubtitleChina(caption.content);
            setItemSubtitleEnglish(caption.content);
        } else {
            setItemSubtitleChina("");
            setItemSubtitleEnglish("");
        }
    }

    @Override
    public void setData(TimedTextObject model) {
        if (model == null || model.captions.size() <= 0) {
            Log.e(TAG, "subtitle data is empty");
            return;
        }
        this.model = model;
    }

    @Override
    public void setLanguage(int type) {
        if (type == LANGUAGE_TYPE_CHINA) {
            subChina.setVisibility(View.VISIBLE);
            subEnglish.setVisibility(View.GONE);
        } else if (type == LANGUAGE_TYPE_ENGLISH) {
            subChina.setVisibility(View.GONE);
            subEnglish.setVisibility(View.VISIBLE);
        } else if (type == LANGUAGE_TYPE_BOTH) {
            subChina.setVisibility(View.VISIBLE);
            subEnglish.setVisibility(View.VISIBLE);
        } else {
            subChina.setVisibility(View.GONE);
            subEnglish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged:" + hasWindowFocus);
    }

    @Override
    public void setPause(boolean pause) {

    }

    @Override
    public void setStart(boolean start) {

    }

    @Override
    public void setStop(boolean stop) {

    }

    @Override
    public void setPlayOnBackground(boolean pb) {
        this.palyOnBackground = pb;
    }

    /**
     * 采用二分法去查找当前应该播放的字幕
     *
     * @param list 全部字幕
     * @param key  播放的时间点
     * @return
     */
    public Caption searchSub(TreeMap<Integer, Caption> list, int key) {
//        int start = 0;
//        int end = list.size() - 1;
//        while (start <= end) {
//            int middle = (start + end) / 2;
//            Caption caption = list.get(middle);
//            if (key < caption.start.getMseconds()) {
//                if (key > caption.end.getMseconds()) {
//                    return caption;
//                }
//                end = middle - 1;
//            } else if (key > caption.end.getMseconds()) {
//                if (key < caption.start.getMseconds()) {
//                    return caption;
//                }
//                start = middle + 1;
//            } else if (key >= caption.start.getMseconds() && key <= caption.end.getMseconds()) {
//                return caption;
//            }
//        }
        for(Integer key1 : list.keySet()) {
            Caption caption = list.get(key1);
            Log.d(TAG, "position: " + key + ", start: " + caption.start.getMseconds() + ",end: " + caption.end.getMseconds());
            if (key >= caption.start.getMseconds() && key <= caption.end.getMseconds()) {
                Log.d(TAG, caption.toString());
                return caption;
            }
        }
        return null;
    }

    @Override
    public void ClickDown() {
//        Toast.makeText(context, "ClickDown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ClickUp() {
//        Toast.makeText(context, "ClickUp", Toast.LENGTH_SHORT).show();
    }
}
