package com.boot.utils;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：关河九州
 * @date：2019/11/19 20:11
 * @version：1.0
 */
public class ExcelUtil {
    private int totalRows = 0; //总行数
    private int totalCells = 0; //总列数

    public Map<String, List<List<String>>> read(String fileName) {
        Map<String, List<List<String>>> maps = new HashMap<String, List<List<String>>>();
        if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$"))
            return maps;
        File file = new File(fileName);
        if (file == null || !file.exists())
            return maps;
        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream(file));
            maps = read(wb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }

    public int getTotalRows(){return totalRows;}
    public int getTotalCells(){return totalCells;}

    private Map<String,List<List<String>>> read(Workbook wb){
        Map<String,List<List<String>>> maps=new HashMap<String,List<List<String>>>();
        int number=wb.getNumberOfSheets();
        if (number>0){
            for (int i=0;i<number;i++){ //循环每个工作表
                List<List<String>> list=new ArrayList<List<String>>();
                //第一页去除行数
                int delnumber=0;
                Sheet sheet=wb.getSheetAt(i);
                //获取工作表中行数
                this.totalRows=sheet.getPhysicalNumberOfRows() - delnumber;
                if (this.totalRows>=1 && sheet.getRow(delnumber) !=null){
                    this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();//得到当前行的所有单元格
                    for (int j=0;j<totalRows;j++){
                        List<String> rowLst=new ArrayList<String>();
                        for (int f=0;f<totalCells;f++){
                            if (totalCells>0){
                                String value=getCell(sheet.getRow(j).getCell(f));
                                rowLst.add(value);
                            }
                        }
                        list.add(rowLst);
                    }
                }
                maps.put(sheet.getSheetName(),list);
            }
        }
        return maps;
    }
    public String getCell(Cell cell){
        String cellValue=null;
        HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
        cellValue = hSSFDataFormatter.formatCellValue(cell); // 使用EXCEL原来格式的方式取得值
        return cellValue;
    }

    public static void main(String[] args) {
        try {
            Map<String, List<List<String>>> map = new ExcelUtil()
                    .read("src/test/resources/StoreList.xlsx");
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

