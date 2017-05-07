package org.airtel.project.Jobs;

import net.minidev.json.JSONObject;
import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.util.SQLUtilFunctions;
import org.airtel.project.util.ServerConstants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by amulyam on 06/05/17.
 */
public class VowelCounterJob implements Runnable {
    private Map<String,Integer> vowelCounter;
    private String text;
    private SQLUtilFunctions utils;
    private int id;

    public VowelCounterJob(Map<String, Integer> vowelCounter, String text, SQLUtilFunctions utils, int id) {
        this.vowelCounter = vowelCounter;
        this.text = text;
        this.utils = utils;
        this.id = id;
    }


    @Override
    public void run() {
        for(char c : text.toCharArray()){
            if(c=='a'){
                if(vowelCounter.containsKey("a")){
                    int ctr = vowelCounter.get("a");
                    vowelCounter.put("a",++ctr);
                }else {
                    vowelCounter.put("a",1);
                }
            }else if(c=='e'){
                if(vowelCounter.containsKey("e")){
                    int ctr = vowelCounter.get("e");
                    vowelCounter.put("e",++ctr);
                }else {
                    vowelCounter.put("e",1);
                }
            }else if(c=='i'){
                if(vowelCounter.containsKey("i")){
                    int ctr = vowelCounter.get("i");
                    vowelCounter.put("i",++ctr);
                }else {
                    vowelCounter.put("i",1);
                }
            }else if(c=='o'){
                if(vowelCounter.containsKey("o")){
                    int ctr = vowelCounter.get("o");
                    vowelCounter.put("o",++ctr);
                }else {
                    vowelCounter.put("o",1);
                }
            }else if(c=='u'){
                if(vowelCounter.containsKey("u")){
                    int ctr = vowelCounter.get("u");
                    vowelCounter.put("u",++ctr);
                }else {
                    vowelCounter.put("u",1);
                }
            }
        }
        try(Connection connection = MySqlConnector.getInstance().getConnection()){
            utils.updateVowelCountTask(connection,id,makeJsonString());
            utils.updateCompletedTask(connection,id);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String makeJsonString(){
        JSONObject mainObj = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        vowelCounter.keySet().forEach(x -> jsonObject.put(x,vowelCounter.get(x)));
        mainObj.put(ServerConstants.VOWEL_FREQUENCY,jsonObject.toString());
        return mainObj.toString();
    }
}
