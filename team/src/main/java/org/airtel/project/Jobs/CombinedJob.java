package org.airtel.project.Jobs;

import org.airtel.project.util.SQLUtilFunctions;

import java.util.Map;
import java.util.Set;

/**
 * Created by amulyam on 06/05/17.
 */
public class CombinedJob implements Runnable {
    private SQLUtilFunctions utils;
    private int id;
    private String text;
    private Set<String> happy;
    private Set<String> sad;
    private Map<String,Integer> wordCounter;

    public CombinedJob(SQLUtilFunctions utils, int id, String text, Set<String> happy, Set<String> sad, Map<String, Integer> wordCounter) {
        this.utils = utils;
        this.id = id;
        this.text = text;
        this.happy = happy;
        this.sad = sad;
        this.wordCounter = wordCounter;
    }

    @Override
    public void run() {
        try {
            Thread t1 = new Thread(new WordFrequencyJob(wordCounter, text, utils, id));
            t1.start();
            t1.join();
            Thread t2 = new Thread(new SentimentAnalysisJob(happy,sad,wordCounter,id,utils));
            t2.start();
            t2.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
