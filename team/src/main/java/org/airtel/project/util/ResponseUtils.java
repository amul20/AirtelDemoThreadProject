package org.airtel.project.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.minidev.json.JSONObject;
import org.restexpress.Response;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amulyam on 06/05/17.
 */

/**
 * Class to set response headers
 */
public class ResponseUtils {
    public static final int RESTEXPRESS_BUFFER_SIZE = 1024;
    private final ByteBufAllocator bufferAlloc = PooledByteBufAllocator.DEFAULT;;

    public void setPostSuccessResponse(Response response, int id) {
        response.setResponseCode(200);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",String.valueOf(id));
        final ByteBuf buffer = bufferAlloc.directBuffer(RESTEXPRESS_BUFFER_SIZE);
        buffer.writeBytes(jsonObject.toString().getBytes());
        response.setBody(buffer);
        response.setContentType("text/json");
        response.setResponseStatus(HttpResponseStatus.OK);
    }

    public void setGetResponse(ResultSet rs,Response response) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        rs.next();
        int id = rs.getInt(1);
        jsonObject.put(ServerConstants.ID,id);
        String start_date = rs.getString(2)!=null ? rs.getString(2) : "";
        jsonObject.put(ServerConstants.START_TIME,start_date);
        String end_date = rs.getString(3)!=null ? rs.getString(3) : "";
        jsonObject.put(ServerConstants.END_TIME, end_date);
        String wordF = rs.getString(4)!=null ? rs.getString(4) : "";
        jsonObject.put(ServerConstants.WORD_FREQUENCY, wordF);
        String vowelF = rs.getString(5)!=null ? rs.getString(5) : "";
        jsonObject.put(ServerConstants.VOWEL_FREQUENCY, vowelF);
        String sentiment = rs.getString(6)!=null ? rs.getString(6) : "";
        jsonObject.put(ServerConstants.SENTIMENT,sentiment);
        int completed = rs.getInt(7);
        String comp = completed+"/5";
        String progress = completed==5 ? ServerConstants.COMPLETED : ServerConstants.IN_PROGRESS;
        jsonObject.put(ServerConstants.COMPLETED_TASKS,comp);
        jsonObject.put(ServerConstants.STATUS,progress);
        final ByteBuf buffer = bufferAlloc.directBuffer(RESTEXPRESS_BUFFER_SIZE);
        buffer.writeBytes(jsonObject.toString().getBytes());
        response.setBody(buffer);
        response.setContentType("text/json");
        response.setResponseStatus(HttpResponseStatus.OK);
    }

}
