package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import id.kawahedukasi.request.FileMulipartRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

@ApplicationScoped
public class DocumentService {
    @Inject
    ItemService itemService;
    public void importItem(FileMulipartRequest request) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.file);

        //membuat satu object workbook dari file yang di request
        XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);

        //mengambil sheet pertama
        XSSFSheet sheet = workbook.getSheetAt(0);

        //remove header excel
        sheet.removeRow(sheet.getRow(0));

        for (Row row : sheet){
            itemService.persisItem(
                row.getCell(0).getStringCellValue(),
                Double.valueOf(row.getCell(1).getNumericCellValue()).longValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getNumericCellValue(),
                row.getCell(4).getStringCellValue()
            );
        }
    }

    public Response reportItem() throws JRException {
        //buat template jrxml jasper di folder resources
        File file = new File("src/main/resources/item-report-template.jrxml");

        //build object jasper report dari load template
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        //create data source dari list item
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Item.listAll());

        //bikin object jasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        //export jasperPrint dalam bentuk byte array
        byte[] jasperResult = JasperExportManager.exportReportToPdf(jasperPrint);

        //return response dengan content type pdf
        return Response.ok().type("application/pdf").entity(jasperResult).build();
    }
}
