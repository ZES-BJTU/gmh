package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.helper.ExcelHelper.generateStringCell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.enums.FlowTypeEnum;
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	public int insert(CustomerPo customerPo) {
		customerPo.setStoreId(ThreadContext.getUserStoreId());
		int i = customerMapper.insert(customerPo);
		return i;
	}

	@Override
	public int update(CustomerPo customerPo) {
		customerPo.setStoreId(ThreadContext.getUserStoreId());
		int i = customerMapper.update(customerPo);
		return i;
	}

	@Override
	public int delete(Long id) {
		int i = customerMapper.delete(id);
		return i;
	}

	@Override
	public PagedList<CustomerPo> listPagedCustomerPo(CustomerQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerPo> unions = customerMapper.selectByCondition(condition);
		if (CollectionUtils.isEmpty(unions)) {
			return PagedLists.newPagedList(pageNum, pageSize);
		}
		PageInfo<CustomerPo> info = new PageInfo<>(unions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
	}

	@Override
	public Workbook exportCustomerRecord() {
		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("库存流水统计");
		Long storeId = ThreadContext.getUserStoreId();
		ensureEntityExist(storeId, "当前用户不属于任何门店");
		
		CustomerQueryCondition condition = new CustomerQueryCondition();
		condition.setStoreId(storeId);		
		List<CustomerPo> unions = customerMapper.selectByCondition(condition);
		
		if (CollectionUtils.isEmpty(unions)) {
			return workbook;
		}
		buildSheetByCustomerUnions(sheet, unions);
		return workbook;

	}

	private void buildSheetByCustomerUnions(Sheet sheet, List<CustomerPo> pos) {
		int rowNum = 0;
        int columnNum = 0;
        Row row = sheet.createRow(rowNum++);
        generateStringCell(row, columnNum++, "姓名");
        generateStringCell(row, columnNum++, "性别");
        generateStringCell(row, columnNum++, "电话");
        generateStringCell(row, columnNum++, "出生日期");
        generateStringCell(row, columnNum++, "来源");
        generateStringCell(row, columnNum++, "录入时间");
        generateStringCell(row, columnNum++, "备注");

        for (CustomerPo po : pos) {
            columnNum = 0;
            row = sheet.createRow(rowNum++);
            //姓名
            generateStringCell(row, columnNum++, po.getName());
            //性别
            generateStringCell(row, columnNum++, EnumUtils.getDescByKey(po.getGender(),GenderEnum.class));
            //电话
            generateStringCell(row, columnNum++, po.getMobile());
            //出生日期
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(po.getBirthday());
            generateStringCell(row, columnNum++, dateString);
            //来源
            generateStringCell(row, columnNum++, po.getSource());
            //录入日期
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString1 = formatter1.format(po.getInputTime());
            generateStringCell(row, columnNum++, dateString1);
            //备注
            generateStringCell(row, columnNum++, po.getRemark());
        }
		
	}

}
