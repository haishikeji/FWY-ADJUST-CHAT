package com.px.aj.chatgptsetting.service;

import java.util.List;

import com.px.aj.chatgptsetting.domain.AjChatgptSetting;

/**
 * chatgpt设置Service接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface IAjChatgptSettingService 
{
    /**
     * 查询chatgpt设置
     * 
     * @param id chatgpt设置主键
     * @return chatgpt设置
     */
    public AjChatgptSetting selectAjChatgptSettingById(Long id);

    /**
     * 查询chatgpt设置列表
     * 
     * @param ajChatgptSetting chatgpt设置
     * @return chatgpt设置集合
     */
    public List<AjChatgptSetting> selectAjChatgptSettingList(AjChatgptSetting ajChatgptSetting);

    /**
     * 新增chatgpt设置
     * 
     * @param ajChatgptSetting chatgpt设置
     * @return 结果
     */
    public int insertAjChatgptSetting(AjChatgptSetting ajChatgptSetting);

    /**
     * 修改chatgpt设置
     * 
     * @param ajChatgptSetting chatgpt设置
     * @return 结果
     */
    public int updateAjChatgptSetting(AjChatgptSetting ajChatgptSetting);

    /**
     * 批量删除chatgpt设置
     * 
     * @param ids 需要删除的chatgpt设置主键集合
     * @return 结果
     */
    public int deleteAjChatgptSettingByIds(Long[] ids);

    /**
     * 删除chatgpt设置信息
     * 
     * @param id chatgpt设置主键
     * @return 结果
     */
    public int deleteAjChatgptSettingById(Long id);

    // 获取正在使用的key
    public String getKey();
}
