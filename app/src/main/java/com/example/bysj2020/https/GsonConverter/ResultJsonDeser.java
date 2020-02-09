package com.example.bysj2020.https.GsonConverter;

import com.example.bysj2020.https.HttpBean;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 应对泛型null对象的处理
 */
public class ResultJsonDeser implements JsonDeserializer<HttpBean<?>> {
    @Override
    public HttpBean<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        HttpBean response = new HttpBean();
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            int code = jsonObject.get("state").getAsInt();
            response.setState(code);
            response.setMsg(jsonObject.get("msg").getAsString());
            if (code != 0) {
                return response;
            }
            if (jsonObject.has("lastPage") && !jsonObject.get("lastPage").isJsonNull()) {
                response.setLastPage(jsonObject.get("lastPage").getAsInt());
            }
            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
                response.setData(context.deserialize(jsonObject.get("data"), itemType));
            }
            return response;
        }
        return response;
    }
}
