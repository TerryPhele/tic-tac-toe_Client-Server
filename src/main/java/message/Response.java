package message;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private JSONObject message;

    public Response(){
        this.message = new JSONObject();
    }

    public String send( String feedBack){
        Map<String, String> message = new HashMap<>();
        message.put("message", feedBack);

        this.message = new JSONObject(message);

        return this.message.toString(4);
    }
}
