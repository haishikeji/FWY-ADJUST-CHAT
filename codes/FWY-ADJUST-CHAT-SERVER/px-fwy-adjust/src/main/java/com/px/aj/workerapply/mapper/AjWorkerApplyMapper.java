package com.px.aj.workerapply.mapper;

import java.util.List;

import com.px.aj.workerapply.domain.AjMyApplyResultModel;
import com.px.aj.workerapply.domain.AjWorkerApply;
import org.apache.ibatis.annotations.Param;

/**
 * 劳动者提交的申请Mapper接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface AjWorkerApplyMapper 
{
    /**
     * 查询劳动者提交的申请
     * 
     * @param id 劳动者提交的申请主键
     * @return 劳动者提交的申请
     */
    public AjWorkerApply selectAjWorkerApplyById(Long id);

    /**
     * 查询劳动者提交的申请列表
     * 
     * @param ajWorkerApply 劳动者提交的申请
     * @return 劳动者提交的申请集合
     */
    public List<AjWorkerApply> selectAjWorkerApplyList(AjWorkerApply ajWorkerApply);

    /**
     * 新增劳动者提交的申请
     * 
     * @param ajWorkerApply 劳动者提交的申请
     * @return 结果
     */
    public int insertAjWorkerApply(AjWorkerApply ajWorkerApply);

    /**
     * 修改劳动者提交的申请
     * 
     * @param ajWorkerApply 劳动者提交的申请
     * @return 结果
     */
    public int updateAjWorkerApply(AjWorkerApply ajWorkerApply);

    /**
     * 删除劳动者提交的申请
     * 
     * @param id 劳动者提交的申请主键
     * @return 结果
     */
    public int deleteAjWorkerApplyById(Long id);

    /**
     * 批量删除劳动者提交的申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAjWorkerApplyByIds(Long[] ids);

    public List<AjMyApplyResultModel> selectAjWorkerApplyNumGroupByYear(AjWorkerApply ajWorkerApply);

    public List<AjWorkerApply> selectAjWorkerApplyListByYearAndUserId(@Param("year") String year,@Param("userId") Long userId);
}
