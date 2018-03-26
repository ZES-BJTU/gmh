package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping(path = "/export")
@Controller
public class ExportController {

    @RequestMapping(path = "/stocks")
    public ModelAndView doExportStock(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        map.put("fileName", "库存.xlsx");
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet();
        ExcelView excelView = new ExcelView();
        excelView.buildExcelDocument(map, workbook, request, response);
        return new ModelAndView(excelView, map);
    }

}
