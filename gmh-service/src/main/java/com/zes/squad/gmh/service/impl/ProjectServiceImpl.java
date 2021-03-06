package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureAttributeExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.helper.ExcelHelper.generateNumericCell;
import static com.zes.squad.gmh.helper.ExcelHelper.generateStringCell;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.enums.TopTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.ProjectStockUnion;
import com.zes.squad.gmh.entity.union.ProjectUnion;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.mapper.ProjectMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectTypeMapper  projectTypeMapper;
    @Autowired
    private ProjectMapper      projectMapper;
    @Autowired
    private ProjectUnionMapper projectUnionMapper;
    @Autowired
    private ProjectStockMapper projectStockMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectTypePo createProjectType(ProjectTypePo po) {
        ProjectTypePo existingPo = projectTypeMapper.selectByTopTypeAndName(po.getTopType(), po.getName());
        ensureEntityNotExist(existingPo, "项目分类已存在");
        int record = projectTypeMapper.insert(po);
        ensureConditionSatisfied(record == 1, "项目分类添加失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjectType(Long id) {
        List<ProjectPo> pos = projectMapper.selectByTypeId(id);
        ensureCollectionEmpty(pos, "项目分类已被使用,无法删除");
        int record = projectTypeMapper.deleteById(id);
        ensureConditionSatisfied(record == 1, "项目分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjectTypes(List<Long> ids) {
        int rows = projectTypeMapper.batchDelete(ids);
        ensureConditionSatisfied(rows == ids.size(), "项目分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectTypePo modifyProjectType(ProjectTypePo po) {
        ProjectTypePo existingPo = projectTypeMapper.selectByTopTypeAndName(po.getTopType(), po.getName());
        if (existingPo != null) {
            ensureConditionSatisfied(existingPo.getId().equals(po.getId()), "项目分类已存在");
        }
        projectTypeMapper.updateSelective(po);
        ProjectTypePo newPo = projectTypeMapper.selectById(po.getId());
        ensureEntityExist(newPo, "项目分类不存在");
        return newPo;
    }

    @Override
    public ProjectTypePo queryProjectTypeDetail(Long id) {
        ProjectTypePo po = projectTypeMapper.selectById(id);
        ensureEntityExist(po, "未找到项目分类");
        return po;
    }

    @Override
    public PagedList<ProjectTypePo> listPagedProjectTypes(ProjectTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectTypePo> unions = projectTypeMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProjectTypePo> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<ProjectTypePo> listAllProjectTypes() {
        return projectTypeMapper.selectAll();
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectUnion createProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        ProjectPo po = projectMapper.selectByCode(projectPo.getCode());
        ensureEntityNotExist(po, "项目代码已被占用");
        projectMapper.insert(projectPo);
        Long projectId = projectPo.getId();
        ensureAttributeExist(projectId, "项目添加失败");
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        int records = projectStockMapper.batchInsert(stockPos);
        ensureConditionSatisfied(records == stockPos.size(), "项目添加失败");
        ProjectUnion newUnion = new ProjectUnion();
        newUnion.setId(projectPo.getId());
        newUnion.setProjectPo(projectPo);
        List<ProjectStockPo> newStockPos = projectStockMapper.selectByProjectId(projectId);
        ensureCollectionNotEmpty(newStockPos, "项目添加失败");
        List<ProjectStockUnion> newStockUnions = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockPo stockPo : newStockPos) {
            ProjectStockUnion stockUnion = new ProjectStockUnion();
            stockUnion.setId(stockPo.getId());
            stockUnion.setProjectStockPo(stockPo);
            newStockUnions.add(stockUnion);
        }
        newUnion.setProjectStockUnions(newStockUnions);
        return newUnion;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProject(Long id) {
        int row = projectMapper.deleteById(id);
        ensureConditionSatisfied(row == 1, "删除项目失败");
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(id));
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjects(List<Long> ids) {
        int rows = projectMapper.batchDelete(ids);
        ensureConditionSatisfied(rows == ids.size(), "删除项目失败");
        projectStockMapper.batchDeleteByProjectIds(ids);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectUnion modifyProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        Long projectId = projectPo.getId();
        String code = projectPo.getCode();
        if (!Strings.isNullOrEmpty(code)) {
            ProjectPo po = projectMapper.selectByCode(code);
            if (po != null) {
                ensureConditionSatisfied(po.getId().equals(projectId), "项目代码已被占用");
            }
        }
        int row = projectMapper.updateSelective(projectPo);
        ensureConditionSatisfied(row == 1, "修改项目失败");
        ProjectPo newPo = projectMapper.selectById(projectId);
        ensureEntityExist(newPo, "项目不存在");
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(projectId));
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        int rows = projectStockMapper.batchInsert(stockPos);
        ensureConditionSatisfied(rows == stockPos.size(), "修改项目失败");
        ProjectUnion newUnion = new ProjectUnion();
        newUnion.setId(projectId);
        newUnion.setProjectPo(newPo);
        List<ProjectStockPo> newStockPos = projectStockMapper.selectByProjectId(projectId);
        ensureCollectionNotEmpty(newStockPos, "添加项目失败");
        List<ProjectStockUnion> newStockUnions = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockPo stockPo : newStockPos) {
            ProjectStockUnion stockUnion = new ProjectStockUnion();
            stockUnion.setId(stockPo.getId());
            stockUnion.setProjectStockPo(stockPo);
            newStockUnions.add(stockUnion);
        }
        newUnion.setProjectStockUnions(newStockUnions);
        return newUnion;
    }

    @Override
    public ProjectUnion queryProjectDetail(Long id) {
        ProjectUnion union = projectUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到项目");
        ensureAttributeExist(union.getId(), "未找到项目");
        ensureEntityExist(union.getProjectTypePo(), "未找到项目分类");
        ensureEntityExist(union.getProjectPo(), "未找到项目");
        ensureCollectionNotEmpty(union.getProjectStockUnions(), "未找到项目对应库存");
        return union;
    }

    @Override
    public Long queryProjectByCode(String code) {
        ensureParameterExist(code, "项目代码为空");
        ProjectPo po = projectMapper.selectByCode(code);
        if (po == null) {
            return null;
        }
        return po.getId();
    }

    @Override
    public PagedList<ProjectUnion> listPagedProjects(ProjectQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Long> ids = projectMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<ProjectUnion> unions = projectUnionMapper.selectByIds(ids);
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<ProjectPo> listAllProjects() {
        return projectMapper.selectAll();
    }

    @Override
    public Workbook exportProjects() {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("项目统计");
        List<ProjectUnion> unions = projectUnionMapper.selectAll();
        if (CollectionUtils.isEmpty(unions)) {
            return workbook;
        }
        buildSheetByProjectUnions(sheet, unions);
        return workbook;
    }

    private void buildSheetByProjectUnions(Sheet sheet, List<ProjectUnion> unions) {
        int rowNum = 0;
        int columnNum = 0;
        int defaultStockColumnNum = 8;
        Row row = sheet.createRow(rowNum++);
        generateStringCell(row, columnNum++, "顶层分类名称");
        generateStringCell(row, columnNum++, "项目分类名称");
        generateStringCell(row, columnNum++, "项目代码");
        generateStringCell(row, columnNum++, "项目名称");
        generateStringCell(row, columnNum++, "项目单价");
        generateStringCell(row, columnNum++, "项目积分");
        generateStringCell(row, columnNum++, "实习生项目积分");
        generateStringCell(row, columnNum++, "备注");
        generateStringCell(row, columnNum++, "项目所需库存分类名称");
        generateStringCell(row, columnNum++, "项目所需库存代码");
        generateStringCell(row, columnNum++, "项目所需库存名称");
        generateStringCell(row, columnNum++, "项目所需库存数量");
        generateStringCell(row, columnNum++, "项目所需库存计量单位");
        for (ProjectUnion union : unions) {
            ProjectTypePo typePo = union.getProjectTypePo();
            ensureEntityExist(typePo, "项目分类不存在");
            ProjectPo po = union.getProjectPo();
            ensureEntityExist(po, "项目不存在");
            List<ProjectStockUnion> stockUnions = union.getProjectStockUnions();
            ensureCollectionNotEmpty(stockUnions, "项目所需库存为空");
            columnNum = 0;
            row = sheet.createRow(rowNum++);
            //顶层分类名称
            generateStringCell(row, columnNum++, EnumUtils.getDescByKey(typePo.getTopType(), TopTypeEnum.class));
            //项目分类名称
            generateStringCell(row, columnNum++, typePo.getName());
            //项目代码
            generateStringCell(row, columnNum++, po.getCode());
            //项目名称
            generateStringCell(row, columnNum++, po.getName());
            //项目单价
            generateNumericCell(row, columnNum++, po.getUnitPrice().doubleValue());
            //项目积分
            generateNumericCell(row, columnNum++, po.getIntegral().doubleValue());
            //实习生项目积分
            generateNumericCell(row, columnNum++, po.getInternIntegral().doubleValue());
            //备注
            generateStringCell(row, columnNum++, po.getRemark());
            for (int i = 0; i < stockUnions.size(); i++) {
                ProjectStockUnion projectStockUnion = stockUnions.get(i);
                ensureEntityExist(projectStockUnion, "项目所需库存为空");
                ProjectStockPo projectStockPo = projectStockUnion.getProjectStockPo();
                ensureEntityExist(projectStockPo, "项目所需库存为空");
                StockUnion stockUnion = projectStockUnion.getStockUnion();
                ensureEntityExist(stockUnion, "项目所需库存为空");
                StockTypePo stockTypePo = stockUnion.getStockTypePo();
                ensureEntityExist(stockTypePo, "项目所需库存分类为空");
                StockPo stockPo = stockUnion.getStockPo();
                columnNum = defaultStockColumnNum;
                if (i > 0) {
                    row = sheet.createRow(rowNum++);
                }
                ensureEntityExist(stockPo, "项目所需库存为空");
                //项目所需库存分类名称
                generateStringCell(row, columnNum++, stockTypePo.getName());
                //项目所需库存代码
                generateStringCell(row, columnNum++, stockPo.getCode());
                //项目所需库存名称
                generateStringCell(row, columnNum++, stockPo.getName());
                //项目所需库存数量
                generateNumericCell(row, columnNum++, projectStockPo.getStockConsumptionAmount().doubleValue());
                //项目所需库存计量单位
                generateStringCell(row, columnNum++, stockPo.getUnitName());
            }
        }
    }

}
