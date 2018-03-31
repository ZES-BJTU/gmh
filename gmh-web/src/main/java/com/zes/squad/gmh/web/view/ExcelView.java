package com.zes.squad.gmh.web.view;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import lombok.Cleanup;

public class ExcelView extends AbstractXlsxStreamingView {

    private static final String DEFAULT_EXCEL_CONTENT_TYPE = "application/vnd.ms-excel;charset=UTF-8";

    @Override
    public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                   HttpServletResponse response)
            throws Exception {
        String fileName = (String) model.get("fileName");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType(DEFAULT_EXCEL_CONTENT_TYPE);
        @Cleanup
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
    }

}
