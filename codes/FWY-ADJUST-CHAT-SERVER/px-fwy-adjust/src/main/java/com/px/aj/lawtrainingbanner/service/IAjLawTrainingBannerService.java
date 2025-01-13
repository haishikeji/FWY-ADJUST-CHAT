package com.px.aj.lawtrainingbanner.service;

import java.util.List;

import com.px.aj.lawtrainingbanner.domain.AjLawTrainingBanner;

/**
 * 法务培训班轮播图Service接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface IAjLawTrainingBannerService 
{
    /**
     * 查询法务培训班轮播图
     * 
     * @param id 法务培训班轮播图主键
     * @return 法务培训班轮播图
     */
    public AjLawTrainingBanner selectAjLawTrainingBannerById(Long id);

    /**
     * 查询法务培训班轮播图列表
     * 
     * @param ajLawTrainingBanner 法务培训班轮播图
     * @return 法务培训班轮播图集合
     */
    public List<AjLawTrainingBanner> selectAjLawTrainingBannerList(AjLawTrainingBanner ajLawTrainingBanner);

    /**
     * 新增法务培训班轮播图
     * 
     * @param ajLawTrainingBanner 法务培训班轮播图
     * @return 结果
     */
    public int insertAjLawTrainingBanner(AjLawTrainingBanner ajLawTrainingBanner);

    /**
     * 修改法务培训班轮播图
     * 
     * @param ajLawTrainingBanner 法务培训班轮播图
     * @return 结果
     */
    public int updateAjLawTrainingBanner(AjLawTrainingBanner ajLawTrainingBanner);

    /**
     * 批量删除法务培训班轮播图
     * 
     * @param ids 需要删除的法务培训班轮播图主键集合
     * @return 结果
     */
    public int deleteAjLawTrainingBannerByIds(Long[] ids);

    /**
     * 删除法务培训班轮播图信息
     * 
     * @param id 法务培训班轮播图主键
     * @return 结果
     */
    public int deleteAjLawTrainingBannerById(Long id);
}
