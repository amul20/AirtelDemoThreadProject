package org.airtel.project.Jobs;

import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.util.SQLUtilFunctions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by amulyam on 06/05/17.
 */
public class SentimentAnalysisJob implements Runnable {
    private Set<String> happy;
    private Set<String> sad;
    private Map<String,Integer> wordCounter;
    private int id;
    private SQLUtilFunctions utils;


    public SentimentAnalysisJob(Set<String> happy, Set<String> sad, Map<String, Integer> wordCounter, int id,SQLUtilFunctions utils) {
        this.happy = happy;
        this.sad = sad;
        this.wordCounter = wordCounter;
        this.id = id;
        this.utils = utils;
    }

    @Override
    public void run() {
        int score = 0;
        for(String key : wordCounter.keySet()){
            if(happy.contains(key)) score++;
            else if(sad.contains(key)) score--;
        }

        String sentiment;
        if(score >2) sentiment = "positive";
        else if(score<-2) sentiment = "negative";
        else sentiment = "neutral";
        try(Connection connection = MySqlConnector.getInstance().getConnection()){
            utils.updateSentimentTask(connection,id,sentiment);
            utils.updateCompletedTask(connection, id);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
