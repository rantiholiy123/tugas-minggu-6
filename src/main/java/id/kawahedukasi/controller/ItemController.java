package id.kawahedukasi.controller;

import id.kawahedukasi.model.Item;
import id.kawahedukasi.service.ItemService;
import id.kawahedukasi.service.ReportService;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.engine.JRException;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
        return itemService.create(request);
    }

    @GET
    public Response getAll(){
        List<Item> items = Item.listAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Item item : items){
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("count", item.getCount());
            map.put("type", item.getType());
            map.put("price", item.getPrice());
            map.put("description", item.getDescription());

            result.add(map);
        }
        return Response.ok().entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, JsonObject request){
        String name = request.getString("name");
        Long count = request.getLong("count");
        String type = request.getString("type");
        Double price = request.getDouble("price");

        if (name == null || price == null || type == null || count == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "BAD_REQUEST"))
                    .build();
        }

        Item item = Item.findById(id);
        if (item ==  null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ITEM_NOT_FOUND"))
                    .build();

        }


        item.setName(name);
        item.setCount(count);
        item.setType(type);
        item.setPrice(price);

        item.persist();

        return Response.ok().entity(Map.of("id", item.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id){
        Item item = Item.findById(id);
        if (item ==  null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ITEM_ALLREADY_DELETE"))
                    .build();
        }
        item.delete();

        return Response.status(Response.Status.NO_CONTENT).entity(Map.of("id", item.getId())).build();
    }
}
