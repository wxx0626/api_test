package com.autotest.utils;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.autotest.utils.CaseUtil.cases;

public class ExcelUtil {

    // rowIdentifier和行号映射，用于设置写入哪一行
    public static Map<String, Integer> rowIdentifierRowNumMapping = new HashMap<>();
    // 列名和列号映射，用于设置写入哪一列
    public static Map<String, Integer> cellNameCellNumMapping = new HashMap<>();
    // 需要写入的数据集合
    public static List<WriteBackData> writeBackDataList = new ArrayList<>();
    // 加载映射关系
    static{
        loadRowNumAndCellNumMapping(PropertiesUtil.getExcelPath(),"case");
    }

    /**
     * 加载行号和列号映射，为回写做准备
     * @param excelPath 文件路径
     * @param sheetName 表单名
     */
    public static void loadRowNumAndCellNumMapping(String excelPath, String sheetName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row rowTitle = sheet.getRow(0);
            if (rowTitle != null && !isEmptyRow(rowTitle)){
                int lastCellNum = rowTitle.getLastCellNum();
                // 循环处理标题行的每一列，设置列名和列号映射
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = rowTitle.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String title = cell.getStringCellValue();
                    title = title.substring(0, title.indexOf("（"));
                    int cellNum = cell.getAddress().getColumn();
                    cellNameCellNumMapping.put(title, cellNum);
                }
                // 从第二行开始获取所有数据行，设置行标识和行号映射
                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row rowData = sheet.getRow(i);
                    Cell firstCellOfRow = rowData.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    firstCellOfRow.setCellType(CellType.STRING);
                    String rowIdentifier = firstCellOfRow.getStringCellValue();
                    int rowNum = rowData.getRowNum();
                    rowIdentifierRowNumMapping.put(rowIdentifier, rowNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连续的行列用例获取
     * @param excelPath 文件路径
     * @param startRow 起始行号
     * @param endRow 结束行号
     * @param startCell 起始列号
     * @param endCell 结束列号
     * @return 返回获取的用例数据
     */
    public static Object[][] continuousDatas(String excelPath, int startRow,int endRow,int startCell, int endCell){
        // 获取WorkBook工作簿
        Object[][] datas = new Object[endRow-startRow+1][endCell-startCell+1];
        try {
            Workbook workBook =  WorkbookFactory.create(new File(excelPath));
            // 获取Sheet表单
            Sheet sheet = workBook.getSheet("loginCase");
            // 获取Row行（行索引从0开始）
            for (int i = startRow; i <= endRow; i++) {
                // 传行号和列号，需要-1
                Row row = sheet.getRow(i-1);
                // 获取cell列（列索引从0开始）
                for (int j = startCell; j <= endCell; j++) {
                    // Row.MissingCellPolicy.CREATE_NULL_AS_BLANK解决列值为空的策略
                    Cell cell = row.getCell(j-1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // 讲列设置为字符串类型
                    cell.setCellType(CellType.STRING);
                    // 获取单元格的值
                    String value = cell.getStringCellValue();
                    datas[i-startRow][j-startCell] = value;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 不连续的行列用例获取
     * @param excelPath 文件路径
     * @param rows 行号数组
     * @param cells 列号数组
     * @return 返回获取的用例数据
     */
    public static Object[][] discreteDatas(String excelPath, int[] rows,int[] cells){
        // 获取WorkBook工作簿
        Object[][] datas = new Object[rows.length][cells.length];
        try {
            Workbook workBook =  WorkbookFactory.create(new File(excelPath));
            // 获取Sheet表单
            Sheet sheet = workBook.getSheet("loginCase");
            // 获取Row行（行索引从0开始）
            for (int i = 0; i <= rows.length-1; i++) {
                // 传行号和列号，需要-1
                Row row = sheet.getRow(rows[i]-1);
                // 获取cell列（列索引从0开始）
                for (int j = 0; j <= cells.length-1; j++) {
                    // Row.MissingCellPolicy.CREATE_NULL_AS_BLANK解决列值为空的策略
                    Cell cell = row.getCell(cells[j]-1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // 讲列设置为字符串类型
                    cell.setCellType(CellType.STRING);
                    // 获取单元格的值
                    String value = cell.getStringCellValue();
                    datas[i][j] = value;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 接口信息与参数分离的行列用例获取
     * @param excelPath 文件路径
     * @param sheetName 用例表单名字
     * @param rows 行号数组
     * @param cells 列号数组
     * @return 返回获取的用例数据
     */
    public static Object[][] datas(String excelPath, String sheetName, int[] rows,int[] cells){
        // 获取WorkBook工作簿
        Object[][] datas = new Object[rows.length][cells.length];
        try {
            Workbook workBook =  WorkbookFactory.create(new File(excelPath));
            // 获取Sheet表单
            Sheet sheet = workBook.getSheet(sheetName);
            // 获取Row行（行索引从0开始）
            for (int i = 0; i <= rows.length-1; i++) {
                // 传行号和列号，需要-1
                Row row = sheet.getRow(rows[i]-1);
                // 获取cell列（列索引从0开始）
                for (int j = 0; j <= cells.length-1; j++) {
                    // Row.MissingCellPolicy.CREATE_NULL_AS_BLANK解决列值为空的策略
                    Cell cell = row.getCell(cells[j]-1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // 讲列设置为字符串类型
                    cell.setCellType(CellType.STRING);
                    // 获取单元格的值
                    String value = cell.getStringCellValue();
                    datas[i][j] = value;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 解析指定excel表单的数据，封装为对象
     * @param excelPath 文件相对路径
     * @param sheetName 表单名
     */
    public static <T> List<T> load(String excelPath, String sheetName, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            // 获取WorkBook工作簿
            Workbook workBook =  WorkbookFactory.create(new File(excelPath));
            // 获取Sheet表单
            Sheet sheet = workBook.getSheet(sheetName);
            // 获取数据，通过反射封装到Case中
            // 获取表头
            Row rowTitle = sheet.getRow(0);
            // 比索引值大1
            int lastRowNum = sheet.getLastRowNum();
            int lastCellNum = rowTitle.getLastCellNum();
            // title数组用于存储表头
            String[] titleNames = new String[lastCellNum];
            // 取出列名，保存至字符串数组
            for (int i = 0; i < lastCellNum; i++) {
                // 根据索引值获取列
                Cell cell = rowTitle.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                // 设置列的类型为字符串
                cell.setCellType(CellType.STRING);
                // 获取列值
                String title = cell.getStringCellValue();
                title = title.substring(0,title.indexOf("（"));
                // 截取表头中除去括号的部分，用于拼接反射的方法名
                titleNames[i] = title;
            }
            // 循环处理数据行
            for (int i = 1; i <= lastRowNum; i++) {
                T object  = clazz.newInstance();
                Row row = sheet.getRow(i);
                // 判断空行，空行跳过
                if(row == null || isEmptyRow(row)){
                    continue;
                }
                // 拿到数据封装到object对象中
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    // 获取要反射的方法名
                    String setMethodName = "set" + titleNames[j];
                    // 获取要反射的方法对象
                    Method method = clazz.getMethod(setMethodName, String.class);
                    // 完成反射调用
                    method.invoke(object, value);
                }
                list.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断空行方法
     * @param row 行对象
     * @return 返回是否空行
     */
    private static boolean isEmptyRow(Row row) {
        int lastCellNum = row.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            String value = cell.getStringCellValue();
            if (value != null && value.trim().length() > 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 数据回写
     * @param excelPath 文件路径
     * @param sheetName 表单名
     * @param rowIdentifier rowIdentifier
     * @param cellName 列名
     * @param result 结果
     */
    public static void writeBackData(String excelPath, String sheetName, String rowIdentifier, String cellName, String result) {
        System.out.println("回写啦啦啦~");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            int rowNum = rowIdentifierRowNumMapping.get(rowIdentifier);
            Row row = sheet.getRow(rowNum);
            int cellNum = cellNameCellNumMapping.get(cellName);
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            // 设置自动换行
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(result);
            outputStream = new FileOutputStream(new File(excelPath));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量回写数据的方法
     * @param excelPath 文件路径
     */
    public static void batchWriteBackData(String excelPath) {
        System.out.println("批量回写~~~");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));

            Workbook workbook = WorkbookFactory.create(inputStream);
            for (WriteBackData writeBackData : writeBackDataList) {
                String sheetName = writeBackData.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);

                String rowIdentifier = writeBackData.getRowIdentifier();
                int rowNum = rowIdentifierRowNumMapping.get(rowIdentifier);
                Row row = sheet.getRow(rowNum);

                String cellName = writeBackData.getCellName();
                int cellNum = cellNameCellNumMapping.get(cellName);
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                // 设置自动换行
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(writeBackData.getResult());
            }
            outputStream = new FileOutputStream(new File(excelPath));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (inputStream != null){
                    inputStream.close();
                }
                if (outputStream != null){
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExcelUtil.load("src/main/resources/case_02.xlsx", "loginCase", Case.class);
        for (Case cs: cases){
            System.out.println(cs);
        }
    }
}

