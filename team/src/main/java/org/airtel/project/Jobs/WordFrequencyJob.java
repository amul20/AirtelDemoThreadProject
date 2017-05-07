package org.airtel.project.Jobs;

import net.minidev.json.JSONObject;
import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.util.SQLUtilFunctions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by amulyam on 06/05/17.
 */
public class WordFrequencyJob implements Runnable
{
    private Map<String,Integer> wordCounter;
    private String text;
    private SQLUtilFunctions utils;
    private int id;

    public WordFrequencyJob(Map<String, Integer> wordCounter, String text, SQLUtilFunctions utils, int id) {
        this.wordCounter = wordCounter;
        this.text = text;
        this.utils = utils;
        this.id = id;
    }


    @Override
    public void run() {
        String[] words = text.split(" ");
        for(String token : words){
            if(token.equals("")){
                continue;
            }
            if(wordCounter.containsKey(token)){
                int counter = wordCounter.get(token);
                wordCounter.put(token,counter+1);
            }else {
                wordCounter.put(token,1);
            }
        }
        try(Connection connection = MySqlConnector.getInstance().getConnection()){
            utils.updateWordCountTask(connection,id,makeJsonString());
            utils.updateCompletedTask(connection,id);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String makeJsonString(){
        JSONObject mainObj = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        wordCounter.keySet().forEach(x -> jsonObject.put(x,wordCounter.get(x)));
        mainObj.put("word_freq",jsonObject.toString());
        return mainObj.toString();
    }
}
