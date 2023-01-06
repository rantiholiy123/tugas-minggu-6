package id.kawahedukasi;

import id.kawahedukasi.model.Item;
import io.vertx.core.json.JsonObject;

import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/item")
public class ItemController {
    @POST
    @Transactional
    public Response create(JsonObject request){

        Item item = new Item();
        item.name = request.getString("name");
        item.count = request.getInteger("count");
        item.price = request.getInteger("price");
        item.type = request.getString("type");
        item.description = request.getString("description");

        item.persist();
        return Response.ok().entity(new HashMap<>()).build();
    }
}
