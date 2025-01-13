package com.px.aj.feedback.domain;

import com.px.aj.appuser.domain.AjAppUser;
import com.px.aj.feedbackimg.domain.AjFeedbackImg;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 帮助反馈对象 aj_feedback
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class AjFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** aj_app_user_id 提交人user_id */
    @Excel(name = "aj_app_user_id 提交人user_id")
    private Long userId;

    /** 分类描述(直接存汉字) */
    @Excel(name = "分类描述(直接存汉字)")
    private String typeName;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("typeName", getTypeName())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }


    @Getter
    @Setter
    private List<AjFeedbackImg> imgList;
    @Getter
    @Setter
    private AjAppUser appUser;
    @Getter
    @Setter
    private String userName;
}
