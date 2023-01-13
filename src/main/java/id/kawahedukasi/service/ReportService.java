package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReportService {

    public Response exportJasper() throws JRException {
        File file = new File("src/main/resources/Item.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getAllItem());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter(), dataSource);
        byte[] jasperResult = JasperExportManager.exportReportToPdf(jasperPrint);
        return Response.ok().type("application/pdf").entity(jasperResult).build();
    }

    public List<Item> getAllItem(){
       List<Item> List = Item.listAll();
       return List;
    }

    public Map<String , Object> jasperParameter(){
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("createBy", "ranti");
        return parameter;
    }
}
