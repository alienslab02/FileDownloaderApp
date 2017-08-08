package com.bilal.filedownloaderapp.tools;

import com.bilal.filedownloaderapp.models.Image;
import com.bilal.filedownloaderapp.models.PinBoard;
import com.bilal.filedownloaderapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by applepc on 06/08/2017.
 */

public class APIResponseParser {

    public ArrayList<PinBoard> parsePinBoard(String response) throws JSONException {
        ArrayList<PinBoard> boards = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            PinBoard board = new PinBoard();
            board.setId(object.getString("id"));
            board.setColor(object.getString("color"));
            board.setHeight(object.getInt("height"));
            board.setWidth(object.getInt("width"));
            board.setLikes(object.getInt("likes"));
            board.setUser(parseUser(object.getJSONObject("user")));
            board.setImage(parseImage(object.getJSONObject("urls")));
            boards.add(board);
        }
        return boards;
    }

    public User parseUser(JSONObject userObject) throws JSONException {
        User user = new User();
        user.setName(userObject.getString("name"));
        user.setUsername(userObject.getString("username"));
        user.setProfileImage(parseImage(userObject.getJSONObject("profile_image")));
        return user;
    }

    public Image parseImage(JSONObject urlObject) throws JSONException {
        Image image = new Image();
        String keyFull = urlObject.has("full") ? "full" : "large";
        String keyMedium = urlObject.has("medium") ? "medium" : "regular";

        image.setLarge((String)tryToGet(urlObject, keyFull, ""));
        image.setMedium((String)tryToGet(urlObject, keyMedium, ""));
        image.setSmall((String)tryToGet(urlObject, "small", ""));
        image.setThumbnail((String)tryToGet(urlObject, "thumb", ""));

        return image;
    }

    private Object tryToGet(JSONObject obj, String key, Object defaultValue) throws JSONException {
        return obj.has(key) ? obj.get(key) : defaultValue;
    }

}
