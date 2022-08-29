package com.baoge.wnotes.util;

import android.content.Context;

import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.manager.DbManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
    private volatile static ExcelUtil INSTANCE;


    private ExcelUtil() {
    }

    public static ExcelUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ExcelUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExcelUtil();
                }
            }
        }
        return INSTANCE;
    }


    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";


    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();

            }
            file.createNewFile();
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, filePath, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param orders   待写入的list
     * @param filePath
     */
    @SuppressWarnings("unchecked")
    public static void writeOrdersListToExcel(List<Order> orders, String filePath) {
        if (orders != null && orders.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                File file = new File(filePath);
//                if(file.exists()){
//                    file.delete();
//                }
//                file.createNewFile();
                in = new FileInputStream(file);

                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(file, workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < orders.size(); j++) {




                    Order order = orders.get(j);

                    int  totalMoney = order.getTransactionAmount();
                    int taxiMoney =   order.getTaxiFare();
                    int  partDeviceMoney =  order.getPartPrice();
                    int  otherMoney =   order.getOtherPrice();
                    int  supportMoney =   order.getSupportPrice();
                    int deviceMoneyDB = order.getDevicePrice();

                    int installMoney =  order.getInstallPrice();
                    int  invoceMoney =  order.getInvoice();


                    List<String> list = new ArrayList<>();
                    list.add(DateFormat.getDate(order.getOrderTime(), DateFormat.FORMAT_YYYY_MM_DD));
                    list.add(order.getCity());
                    list.add(order.getInstaller());
                    list.add(order.getTechnician());

                     int profit = totalMoney- deviceMoneyDB - partDeviceMoney - installMoney - supportMoney - taxiMoney - otherMoney-invoceMoney;

                    list.add(CommUtil.parsePrice(profit/3.0));
                    list.add(order.getDevice());
                    list.add(order.getTransactionAmount() + "");
                    list.add(order.getTaxiFare() + "");
                    list.add(order.getSupportName() + "");
                    list.add(order.getSupportPrice() + "");
                    list.add(order.getPartPrice() + "");
                    list.add(order.getInvoice() + "");
                    list.add(order.getOtherPrice() + "");


                    double toMe = profit * 2 / 3.0 + taxiMoney + supportMoney;

                    list.add(CommUtil.parsePrice(toMe));

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }

                writebook.write();
                workbook.close();
                LogUtil.e("导出Excel成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
