package message;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {
    
    private JSONObject message;

    public Request(){
        this.message = new JSONObject();
    }

    public String send( Integer position, Character playerName){
        Map<Integer, Character>  message = new HashMap<>();
        message.put(position, playerName);
        this.message = new JSONObject(message);

        return this.message.toString(4);
    }
}
