package org.dync.subtitleconverter;

import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

/**
 * @Auther jixiongxu
 * @dat 2017/9/21
 * @descraption 字幕控制接口
 */

public interface ISubtitleControl
{
    /**
     * 设置中文字幕
     * 
     * @param item
     */
    void setItemSubtitleChina(String item);

    /**
     * 设置英文字幕
     * 
     * @param item
     */
    void setItemSubtitleEnglish(String item);

    /**
     * 定位设置字幕，单位毫秒
     * 
     * @param position
     */
    void seekTo(int position);

    /**
     * 设置数据
     * 
     * @param model
     */
    void setData(TimedTextObject model);

    /**
     * 设置显示的语言
     * 
     * @param type
     */
    void setLanguage(int type);

    /**
     * 暂停
     * 
     * @param pause
     */
    void setPause(boolean pause);

    /**
     * 开始
     * 
     * @param start
     */
    void setStart(boolean start);

    /**
     * 停止
     * 
     * @param stop
     */
    void setStop(boolean stop);

    /**
     * 后台播放
     * 
     * @param pb
     */
    void setPlayOnBackground(boolean pb);
}
