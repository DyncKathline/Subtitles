package org.dync.subtitleconverter;

import android.widget.TextView;

import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

/**
 * 字幕控制接口
 */

public interface ISubtitleControl
{
    /**
     * 设置数据
     *
     * @param model
     */
    void setData(TimedTextObject model);

    /**
     * 设置字幕
     *
     * @param view
     * @param item
     */
    void setItemSubtitle(TextView view, String item);

    /**
     * 设置显示的语言
     *
     * @param type
     */
    void setLanguage(int type);

    /**
     * 开始
     */
    void setStart();

    /**
     * 暂停
     */
    void setPause();

    /**
     * 定位设置字幕，单位毫秒
     *
     * @param position
     */
    void seekTo(long position);

    /**
     * 停止
     */
    void setStop();

    /**
     * 后台播放
     * 
     * @param pb
     */
    void setPlayOnBackground(boolean pb);
}
