package com.px.aj.feedbackimg.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.px.aj.feedbackimg.domain.AjFeedbackImg;
import com.px.aj.feedbackimg.service.IAjFeedbackImgService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 帮助反馈图片Controller
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/feedbackimg/feedbackimg")
public class AjFeedbackImgController extends BaseController
{
    @Autowired
    private IAjFeedbackImgService ajFeedbackImgService;

    /**
     * 查询帮助反馈图片列表
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:list')")
    @GetMapping("/list")
    public TableDataInfo list(AjFeedbackImg ajFeedbackImg)
    {
        startPage();
        List<AjFeedbackImg> list = ajFeedbackImgService.selectAjFeedbackImgList(ajFeedbackImg);
        return getDataTable(list);
    }

    /**
     * 导出帮助反馈图片列表
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:export')")
    @Log(title = "帮助反馈图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AjFeedbackImg ajFeedbackImg)
    {
        List<AjFeedbackImg> list = ajFeedbackImgService.selectAjFeedbackImgList(ajFeedbackImg);
        ExcelUtil<AjFeedbackImg> util = new ExcelUtil<AjFeedbackImg>(AjFeedbackImg.class);
        util.exportExcel(response, list, "帮助反馈图片数据");
    }

    /**
     * 获取帮助反馈图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(ajFeedbackImgService.selectAjFeedbackImgById(id));
    }

    /**
     * 新增帮助反馈图片
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:add')")
    @Log(title = "帮助反馈图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AjFeedbackImg ajFeedbackImg)
    {
        return toAjax(ajFeedbackImgService.insertAjFeedbackImg(ajFeedbackImg));
    }

    /**
     * 修改帮助反馈图片
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:edit')")
    @Log(title = "帮助反馈图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AjFeedbackImg ajFeedbackImg)
    {
        return toAjax(ajFeedbackImgService.updateAjFeedbackImg(ajFeedbackImg));
    }

    /**
     * 删除帮助反馈图片
     */
    @PreAuthorize("@ss.hasPermi('feedbackimg:feedbackimg:remove')")
    @Log(title = "帮助反馈图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ajFeedbackImgService.deleteAjFeedbackImgByIds(ids));
    }
}
