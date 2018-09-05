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
import android.widget.TextView;

import org.dync.subtitleconverter.subtitleFile.Caption;
import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 显示字幕的图层
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
     * 中文字幕
     */
    private SubtitleTextView subChinaTwo;

    /**
     * 英文字幕
     */
    private SubtitleTextView subEnglishTwo;

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
        subEnglish = subTitleView.findViewById(R.id.subTitleEnglish);
        subChinaTwo = subTitleView.findViewById(R.id.subTitleChinaTwo);
        subEnglishTwo = subTitleView.findViewById(R.id.subTitleEnglishTwo);
        subChina.setSubtitleOnTouchListener(this);
        subEnglish.setSubtitleOnTouchListener(this);
        subChinaTwo.setSubtitleOnTouchListener(this);
        subEnglishTwo.setSubtitleOnTouchListener(this);
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.BOTTOM);
        this.addView(subTitleView);
    }

    @Override
    public void setItemSubtitle(TextView view, String item) {

//        view.setText(item);
        view.setText(Html.fromHtml(item));
    }

    @Override
    public void seekTo(long position) {
        if(palyOnBackground) {
            return;
        }
        if (model != null && !model.captions.isEmpty()) {
            List<Caption> captions = searchSub(model.captions, position);
//            for (Caption caption : captions) {
//                Log.d(TAG, "seekTo: " + caption.toString());
//            }
            if(captions.size() > 1) {//之多同时显示两个字幕，如需同时显示两个以上的只需重新自定义view增加即可
                caption = captions.get(0);
                if (caption != null) {
                    setItemSubtitle(subChina, caption.content);
                    setItemSubtitle(subEnglish, caption.content);
                } else {
                    setItemSubtitle(subChina, "");
                    setItemSubtitle(subEnglish, "");
                }
                caption = captions.get(1);
                if (caption != null) {
                    setItemSubtitle(subChinaTwo, caption.content);
                    setItemSubtitle(subEnglishTwo, caption.content);
                } else {
                    setItemSubtitle(subChinaTwo, "");
                    setItemSubtitle(subEnglishTwo, "");
                }
            }else {
                caption = captions.get(0);
                if (caption != null) {
                    setItemSubtitle(subChina, caption.content);
                    setItemSubtitle(subEnglish, caption.content);
                } else {
                    setItemSubtitle(subChina, "");
                    setItemSubtitle(subEnglish, "");
                }
            }

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
            subChinaTwo.setVisibility(View.VISIBLE);
            subEnglishTwo.setVisibility(View.GONE);
        } else if (type == LANGUAGE_TYPE_ENGLISH) {
            subChina.setVisibility(View.GONE);
            subEnglish.setVisibility(View.VISIBLE);
            subChinaTwo.setVisibility(View.GONE);
            subEnglishTwo.setVisibility(View.VISIBLE);
        } else if (type == LANGUAGE_TYPE_BOTH) {
            subChina.setVisibility(View.VISIBLE);
            subEnglish.setVisibility(View.VISIBLE);
            subChinaTwo.setVisibility(View.VISIBLE);
            subEnglishTwo.setVisibility(View.VISIBLE);
        } else {
            subChina.setVisibility(View.GONE);
            subEnglish.setVisibility(View.GONE);
            subChinaTwo.setVisibility(View.GONE);
            subEnglishTwo.setVisibility(View.GONE);
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
    public List<Caption> searchSub(TreeMap<Integer, Caption> list, long key) {
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
        List<Caption> captions = new ArrayList<>();
        boolean hasMore = false;
        for(Integer key1 : list.keySet()) {
            Caption caption = list.get(key1);
//            Log.d(TAG, "position: " + key + ", start: " + caption.start.getMseconds() + ",end: " + caption.end.getMseconds());
            if (key >= caption.start.getMseconds() && key <= caption.end.getMseconds()) {
//                Log.d(TAG, caption.toString());
                hasMore = true;
//                return caption;
                captions.add(caption);
            }else if (hasMore){
                break;
            }
        }
        return captions;
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
