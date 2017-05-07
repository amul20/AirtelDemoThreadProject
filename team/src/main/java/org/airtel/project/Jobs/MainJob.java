package org.airtel.project.Jobs;

import org.airtel.project.util.SQLUtilFunctions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by amulyam on 06/05/17.
 */
public class MainJob implements Runnable
{
    private SQLUtilFunctions utils;
    private Set<String> blacklist_keywords;
    private int id;
    private String text;
    private Set<String> happy;
    private Set<String> sad;
    private Map<String,Integer> wordCounter;
    private Map<String,Integer> vowelCounter;

    public MainJob(SQLUtilFunctions utils, Set<String> blacklist_keywords, int id,
                   String text, Set<String> happy, Set<String> sad, Map<String, Integer> wordCounter, Map<String, Integer> vowelCounter) {
        this.utils = utils;
        this.blacklist_keywords = blacklist_keywords;
        this.id = id;
        this.text = text;
        this.happy = happy;
        this.sad = sad;
        this.wordCounter = wordCounter;
        this.vowelCounter = vowelCounter;
    }

    @Override
    public void run() {
        try {
            Thread t1 = new Thread(new SanitizeTask(id,text,utils,blacklist_keywords),"thread1");
            t1.start();
            t1.join();
            Thread t2 = new Thread(new CombinedJob(utils,id,text,happy,sad,wordCounter),"thread2");
            t2.start();
            Thread t3 = new Thread(new VowelCounterJob(vowelCounter,text,utils,id), "thread3");
            t3.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
