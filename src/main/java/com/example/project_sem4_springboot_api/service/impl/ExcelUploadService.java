package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelUploadService {


    private final StudentRepository studentRepository;

    public ExcelUploadService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Student> getCustomersDataFromExcel(InputStream inputStream){
        List<Student> students = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // In ra danh sách các sheet để kiểm tra
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println("Sheet name: " + workbook.getSheetName(i));
            }

            XSSFSheet sheet = workbook.getSheet("students");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'students' does not exist in the Excel file");
            }

            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Student student = new Student();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0 -> student.setId((long) cell.getNumericCellValue());
                        case 1 -> student.setFirstName(cell.getStringCellValue());
                        case 2 -> student.setLastName(cell.getStringCellValue());
                        case 3 -> student.setAddress(cell.getStringCellValue());
                        case 4 -> student.setStudentCode(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void saveStudentsToDatabase(MultipartFile file){
        if (ExcelUploadService.isValidExcelFile(file)) {
            try {
                List<Student> students = ExcelUploadService.getCustomersDataFromExcel(file.getInputStream());
                this.studentRepository.saveAll(students);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid Excel file", e);
            }
        } else {
            throw new IllegalArgumentException("The file is not a valid Excel file");
        }
    }

    public List<Student> getStudents(){
        return this.studentRepository.findAll();
    }






//    private final StudentRepository studentRepository;
//
//    public ExcelUploadService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
//
//    public static boolean isValidExcelFile(MultipartFile file){
//        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
//    }
//
//    public static List<Student> getCustomersDataFromExcel(InputStream inputStream){
//        List<Student> customers = new ArrayList<>();
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheet("customers");
//            int rowIndex =0;
//            for (Row row : sheet){
//                if (rowIndex ==0){
//                    rowIndex++;
//                    continue;
//                }
//                Iterator<Cell> cellIterator = row.iterator();
//                int cellIndex = 0;
//                Student student = new Student();
//                while (cellIterator.hasNext()){
//                    Cell cell = cellIterator.next();
//                    switch (cellIndex){
//                        case 0 -> student.setId((long) cell.getNumericCellValue());
//                        case 1 -> student.setFirstName(cell.getStringCellValue());
//                        case 2 -> student.setLastName(cell.getStringCellValue());
//                        case 3 -> student.setAddress(cell.getStringCellValue());
//                        case 4 -> student.setStudentCode(cell.getStringCellValue());
//                        default -> {
//                        }
//                    }
//                    cellIndex++;
//                }
//                customers.add(student);
//            }
//        } catch (IOException e) {
//            e.getStackTrace();
//        }
//        return customers;
//    }
//
//    public void saveStudentsToDatabase(MultipartFile file){
//        if(ExcelUploadService.isValidExcelFile(file)){
//            try {
//                List<Student> students = ExcelUploadService.getCustomersDataFromExcel(file.getInputStream());
//                this.studentRepository.saveAll(students);
//            } catch (IOException e) {
//                throw new IllegalArgumentException("The file is not a valid excel file");
//            }
//        }
//    }
//
//    public List<Student> getStudents(){
//        return this.studentRepository.findAll();
//    }

}
