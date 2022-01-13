package b22.metro2033.Controller;

import b22.metro2033.domain.AjaxResponseBody;
import b22.metro2033.domain.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/validation")
public class ValidationController {

    @GetMapping(value="/test", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validation() {

        AjaxResponseBody result = new AjaxResponseBody();
        result.setMsg("setMsg");
        result.setResult("setResult");

        return ResponseEntity.ok(result);
    }

//    @PostMapping("/login_validation")
//    public Response login_validation(@RequestBody String request) throws IOException, JSONException {
//        JSONObject JSON = new JSONObject(request);
//        String name = JSON.getString("name");
//        return new Response("Done", false);
//    }
//
//    @GetMapping("/test")
//    public Response getFaculty() throws IOException {
//        Response response = new Response("Done", "hello");
//        return response;
//    }

}
