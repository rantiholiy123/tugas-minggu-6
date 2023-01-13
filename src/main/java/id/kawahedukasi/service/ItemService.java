package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Response;
import java.util.Map;

@ApplicationScoped
public class ItemService {

    @Transactional
    public Response create(JsonObject request){
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

        Item item = new Item();
        item.setName(name);
        item.setCount(count);
        item.setType(type);
        item.setPrice(price);
        item.setDescription(request.getString("description"));

        item.persist();

        return Response.status(Response.Status.CREATED).entity(Map.of("id", item.getId())).build();
    }
}
