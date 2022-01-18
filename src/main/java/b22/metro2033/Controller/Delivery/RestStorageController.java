package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Item;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/storage", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestStorageController {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public RestStorageController(ItemRepository itemRepository,
                             UserRepository userRepository,
                             OrderItemRepository orderItemRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public Response create(@RequestBody String response) throws JSONException {

        JSONObject json = new JSONObject(response);
        String name = json.getString("name");
        int quantity = json.getInt("quantity");
        int weight = json.getInt("weight");

        if (itemRepository.findByName(name).isPresent()) {
            return new Response("Error", "Уже есть такой предмет");
        }

        Item item = new Item(name, quantity, weight);

        itemRepository.save(item);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long item_id = Long.parseLong(json.getString("item_id"));
        Item item = itemRepository.findById(item_id);
        if (item == null) {
            return new Response("Error", "Предмет не найден");
        }

        String new_name = json.getString("name");

        if (!new_name.equals(item.getName()) && itemRepository.findByName(new_name).isPresent()) {
            return new Response("Error", "Уже есть такой предмет");
        }

        item.setName(json.getString("name"));
        item.setQuantity(json.getInt("quantity"));
        item.setWeight(json.getInt("weight"));
        itemRepository.save(item);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", json.getString("name"));

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response delete(@RequestBody String response) throws JSONException {

        JSONObject json = new JSONObject(response);

        Long item_id = Long.parseLong(json.getString("item_id"));

        Item item = itemRepository.findById(item_id).orElse(null);

        if (item == null){
            return new Response("Error", "Предмет не найден");
        }

        if (orderItemRepository.findAllByIdItemId(item.getId()).size() > 0){
            return new Response("Error", "Нельзя удалить предмет, пока он есть в заказе");
        }

        String name = item.getName();

        itemRepository.deleteById(item_id);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);

        return new Response("Done", hashMap);
    }
}
