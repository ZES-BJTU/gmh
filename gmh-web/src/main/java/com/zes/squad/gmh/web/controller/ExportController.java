package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.service.ProjectService;
import com.zes.squad.gmh.service.StockService;
import com.zes.squad.gmh.web.view.ExcelView;

@RequestMapping(path = "/export")
@Controller
public class ExportController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private StockService   stockService;
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/projects", method = { RequestMethod.GET })
    public ModelAndView doExportProjects(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        map.put("fileName", "项目.xlsx");
        Workbook workbook = projectService.exportProjects();
        ExcelView excelView = new ExcelView();
        excelView.buildExcelDocument(map, workbook, request, response);
        return new ModelAndView(excelView, map);
    }
    @RequestMapping(path = "/stocks", method = { RequestMethod.GET })
    public ModelAndView doExportStocks(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        map.put("fileName", "库存流水.xlsx");
        Workbook workbook = stockService.exportStocks();
        ExcelView excelView = new ExcelView();
        excelView.buildExcelDocument(map, workbook, request, response);
        return new ModelAndView(excelView, map);
    }
    @RequestMapping(path = "/products", method = { RequestMethod.GET })
    public ModelAndView doExportProducts(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        map.put("fileName", "产品流水.xlsx");
        Workbook workbook = productService.exportProducts();
        ExcelView excelView = new ExcelView();
        excelView.buildExcelDocument(map, workbook, request, response);
        return new ModelAndView(excelView, map);
    }

}
