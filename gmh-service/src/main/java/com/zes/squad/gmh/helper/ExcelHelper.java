package com.zes.squad.gmh.helper;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelHelper {

    private static final String DEFAULT_SHEET_NAME_PREFIX = "Sheet%s";
    private static final String DEFAULT_SHEET_NAME        = "Sheet1";

    public static Workbook generateWorkbook(String fileName, List<String> sheetNames) {
        checkArgument(!Strings.isNullOrEmpty(fileName), "excel文件名为空");
        Workbook workbook = new XSSFWorkbook();
        if (CollectionUtils.isEmpty(sheetNames)) {
            workbook.createSheet(DEFAULT_SHEET_NAME);
        } else {
            Set<String> uniqueSheetNames = Sets.newHashSet(sheetNames);
            checkArgument(sheetNames.size() == uniqueSheetNames.size(), "工作表名称存在重复");
            int sheetIndex = 1;
            for (int index = 0; index < sheetNames.size(); index++) {
                String sheetName = sheetNames.get(index);
                sheetIndex = index + 1;
                if (Strings.isNullOrEmpty(sheetName)) {
                    sheetName = String.format(DEFAULT_SHEET_NAME_PREFIX, sheetIndex);
                    while (containsIgnoreCase(sheetNames, sheetName)) {
                        sheetIndex++;
                        sheetName = String.format(DEFAULT_SHEET_NAME_PREFIX, sheetIndex);
                    }
                }
                workbook.createSheet(sheetName);
            }
        }
        try {
            @Cleanup
            OutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            return workbook;
        } catch (Exception e) {
            log.error("生成excel文件异常", e);
            return null;
        }
    }

    public static Cell generateStringCell(Row row, int column, String stringCellValue, CellStyle cellStyle) {
        Cell cell = row.createCell(column);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(stringCellValue);
        return cell;
    }
    
    public static Cell generateNumericCell(Row row, int column, double numericCellValue, CellStyle cellStyle) {
        Cell cell = row.createCell(column);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(numericCellValue);
        return cell;
    }

    private static boolean containsIgnoreCase(List<String> comparedStrings, String comparedString) {
        if (comparedString == null) {
            for (int i = 0; i < comparedStrings.size(); i++) {
                if (comparedStrings.get(i) == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < comparedStrings.size(); i++) {
                if (comparedString.equalsIgnoreCase(comparedStrings.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

}
