package com.px.aj.appuser.service.impl;

import java.util.List;

import com.px.aj.appuser.domain.AjAppUser;
import com.px.aj.appuser.mapper.AjAppUserMapper;
import com.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.aj.appuser.service.IAjAppUserService;

/**
 * app用户Service业务层处理
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class AjAppUserServiceImpl implements IAjAppUserService 
{
    @Autowired
    private AjAppUserMapper ajAppUserMapper;

    /**
     * 查询app用户
     * 
     * @param id app用户主键
     * @return app用户
     */
    @Override
    public AjAppUser selectAjAppUserById(Long id)
    {
        return ajAppUserMapper.selectAjAppUserById(id);
    }

    /**
     * 查询app用户列表
     * 
     * @param ajAppUser app用户
     * @return app用户
     */
    @Override
    public List<AjAppUser> selectAjAppUserList(AjAppUser ajAppUser)
    {
        return ajAppUserMapper.selectAjAppUserList(ajAppUser);
    }

    /**
     * 新增app用户
     * 
     * @param ajAppUser app用户
     * @return 结果
     */
    @Override
    public int insertAjAppUser(AjAppUser ajAppUser)
    {
        ajAppUser.setCreateTime(DateUtils.getNowDate());
        return ajAppUserMapper.insertAjAppUser(ajAppUser);
    }

    /**
     * 修改app用户
     * 
     * @param ajAppUser app用户
     * @return 结果
     */
    @Override
    public int updateAjAppUser(AjAppUser ajAppUser)
    {
        ajAppUser.setUpdateTime(DateUtils.getNowDate());
        return ajAppUserMapper.updateAjAppUser(ajAppUser);
    }

    /**
     * 批量删除app用户
     * 
     * @param ids 需要删除的app用户主键
     * @return 结果
     */
    @Override
    public int deleteAjAppUserByIds(Long[] ids)
    {
        return ajAppUserMapper.deleteAjAppUserByIds(ids);
    }

    /**
     * 删除app用户信息
     * 
     * @param id app用户主键
     * @return 结果
     */
    @Override
    public int deleteAjAppUserById(Long id)
    {
        return ajAppUserMapper.deleteAjAppUserById(id);
    }

    @Override
    public AjAppUser selectAjAppUserByPhone(String phone) {
        return ajAppUserMapper.selectAjAppUserByPhone(phone);
    }

    @Override
    public AjAppUser getWorkerInfoByUserId(Long id) {
        return ajAppUserMapper.getWorkerInfoByUserId(id);
    }

    @Override
    public AjAppUser getCompanyInfoByUserId(Long id) {
        return ajAppUserMapper.getCompanyInfoByUserId(id);
    }

    @Override
    public AjAppUser getAjAppUserInfoByH5OpenId(String openId) {
        return ajAppUserMapper.getAjAppUserInfoByH5OpenId(openId);
    }


}
