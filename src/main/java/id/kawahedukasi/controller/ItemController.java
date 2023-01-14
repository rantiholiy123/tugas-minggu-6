package id.kawahedukasi.controller;

import id.kawahedukasi.service.ItemService;
import id.kawahedukasi.service.ReportService;
import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.engine.JRException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {

    @Inject
    ItemService itemService;

    @Inject
    ReportService reportService;

    @GET
    @Path("/report")
    @Produces("application/pdf")
    public Response create() throws JRException {
        return reportService.exportJasper();
    }

    @POST
    public Response create(JsonObject request){
        try {
            Map<String, Object> data = itemService.create(request);
            Map<String, Object> result = new HashMap<>();
            result.put("data", data);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (ValidationException e){
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

    }

    @GET
    public Response getAll(){
        List<Map<String, Object>> data = itemService.getAll();
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        return Response.ok().entity(result).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, JsonObject request){
        try {
            Map<String, Object> data = itemService.update(id, request);
            Map<String, Object> result = new HashMap<>();
            result.put("data", data);

            return Response.ok().entity(result).build();
        } catch (ValidationException e){
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id){
        try {
            Map<String, Object> data = itemService.delete(id);
            Map<String, Object> result = new HashMap<>();
            result.put("data", data);

            return Response.ok().entity(result).build();
        } catch (ValidationException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
    }
}
