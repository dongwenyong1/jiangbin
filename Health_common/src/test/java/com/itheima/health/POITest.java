package com.itheima.health;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ${dong}
 * @date 2020/1/4 10:10
 */
/*从excel中导入数据*/
public class POITest {
    /*遍历行  遍历每一个单元格  再获取值*/
    @Test
    public void input1() throws IOException {
        //创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\helloPoi.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获取对象
        for (Row row : sheet) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获取单元格中的值
               String richStringCellValue = cell.getStringCellValue();
                System.out.println(richStringCellValue);

            }
            //关闭资源
            workbook.close();
        }
    }

    /*还有一种方式就是获取工作表最后一个行号，从而根据行号获得行对象，
    通过行获取最后一个单元格索引，从而根据单元格索引获取每行的一个单元格对象*/
    @Test
    public void input2() throws IOException {
        //创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\helloPoi.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取当前工作表最后一行行号，从0开始
        int lastRowNum = sheet.getLastRowNum();
        //循环获取每行对象
        for (int i = 0; i < lastRowNum; i++) {
            //根据行号获取对应行对象
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                //获取单元格值
                String stringCellValue = row.getCell(j).getStringCellValue();
                System.out.println(stringCellValue);
            }


        }

        //关闭资源
        workbook.close();

    }

    @Test
    public void output1() throws IOException {
        //创建工作薄对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.createSheet("赌侠");
        //创建行
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");
        row.createCell(2).setCellValue("性别");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("张三");
        row1.createCell(1).setCellValue("10");
        row1.createCell(2).setCellValue("男");
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("李so");
        row2.createCell(1).setCellValue("102");
        row2.createCell(2).setCellValue("女");
        //创建行，0表示第一行


     //使用输出流输出数据
        FileOutputStream outputStream = new FileOutputStream("D:\\helloPoi1.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

    }




}
