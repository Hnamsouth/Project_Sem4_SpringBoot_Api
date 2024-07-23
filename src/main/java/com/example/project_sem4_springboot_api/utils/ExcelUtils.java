package com.example.project_sem4_springboot_api.utils;

import com.example.project_sem4_springboot_api.entities.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String HEADER[] = {"id", "firstName", "lastName", "birthday", "address", "studentCode"};
    public static String SHEET_NAME = "sheetForStudentData";
    static String SHEET = "student";

    public static ByteArrayInputStream dataToExcel(List<Student> productList) throws IOException {
        Workbook workbook  = new XSSFWorkbook();

        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row row = sheet.createRow(0);
            for (int i  =0; i< HEADER.length;i++){

                Cell cell = row.createCell(i);
                cell.setCellValue(HEADER[i]);
            }
            int rowIndex = 1;
            for (Student p :productList){
                Row row1 = sheet.createRow(rowIndex);
                rowIndex++;
                row1.createCell(0).setCellValue(p.getId());
                row1.createCell(1).setCellValue(p.getFirstName());
                row1.createCell(2).setCellValue(p.getLastName());
                if (p.getBirthday() != null){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = dateFormat.format(p.getBirthday());
                    row1.createCell(3).setCellValue(dateString);
                    System.out.println(dateString);
                }
                row1.createCell(4).setCellValue(p.getAddress());
                row1.createCell(5).setCellValue(p.getStudentCode());
            }
            workbook.write(byteArrayOutputStream);
            return  new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Student> excelToStuList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            // In ra danh sách các sheet để kiểm tra
            System.out.println("Available sheets:");
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println("Sheet name: " + workbook.getSheetName(i));
            }

            // Sử dụng tên sheet đúng
            String sheetName = "studentss"; // Thay "Trang_tính1" bằng tên sheet thực tế
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' does not exist in the Excel file");
            }

            Iterator<Row> rows = sheet.iterator();
            List<Student> stuList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Student stu = new Student();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            stu.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            stu.setFirstName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            stu.setLastName(currentCell.getStringCellValue());
                            break;
                        case 3:
                            stu.setBirthday(currentCell.getDateCellValue());
                            break;
                        case 4:
                            stu.setAddress(currentCell.getStringCellValue());
                            break;
                        case 5:
                            stu.setStudentCode(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                stuList.add(stu);
            }
            workbook.close();
            return stuList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
