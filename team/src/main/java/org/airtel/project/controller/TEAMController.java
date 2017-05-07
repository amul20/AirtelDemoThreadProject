package org.airtel.project.controller;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.airtel.project.Jobs.MainJob;
import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.util.ResponseUtils;
import org.airtel.project.util.SQLUtilFunctions;
import org.restexpress.Request;
import org.restexpress.Response;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by amulyam on 06/05/17.
 */
public class TEAMController {
    private SQLUtilFunctions utils = new SQLUtilFunctions();
    private ResponseUtils responseUtils = new ResponseUtils();
    private Set<String> happy;
    private Set<String> sad;
    private Set<String> blacklist_keywords;
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public TEAMController() {
        happy = new HashSet<>();
        sad = new HashSet<>();
        blacklist_keywords = new HashSet<>();
    }

    /**
     * function which will be activated by POST request
     * inserts the data in mysql and submits the job in thread pool to run
     * @param request
     * @param response
     */
    public void analyzeText(Request request, Response response){
        try(Connection connection = MySqlConnector.getInstance().getConnection()){
            InputStream inputStream = request.getBodyAsStream();
            Reader reader = new InputStreamReader(inputStream);
            JSONObject textJsonObj = (JSONObject) JSONValue.parse(reader);
            String text = textJsonObj.getAsString("text");
            if(text!=null){
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy");
                Date start_time = new Date();
                String date = format.format(start_time);
                int id = utils.writeTextToTable(connection,text,date);
                Map<String,Integer> wordCounter = new HashMap<>();
                Map<String,Integer> vowelCounter = new HashMap<>();
                MainJob mainJob = new MainJob(utils,blacklist_keywords,id,text,happy,sad,wordCounter,vowelCounter);
                executor.submit(mainJob);
                responseUtils.setPostSuccessResponse(response,id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Function which will be activated by GET request
     * reads the data from mysql and generates the report
     * @param request
     * @param response
     */
    public void getReport(Request request,Response response){
        int id = Integer.parseInt(request.getHeader("id"));
        try(Connection connection = MySqlConnector.getInstance().getConnection()) {
            ResultSet rs = utils.getARow(connection,id);
            responseUtils.setGetResponse(rs,response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
