package org.airtel.project.Jobs;

import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.util.SQLUtilFunctions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by amulyam on 06/05/17.
 */
public class SanitizeTask implements Runnable {
    private int id;
    private String text;
    private SQLUtilFunctions utils;
    private Set<String> blacklist_keywords;

    public SanitizeTask(int id, String text, SQLUtilFunctions utils, Set<String> blacklist_keywords) {
        this.id = id;
        this.text = text;
        this.utils = utils;
        this.blacklist_keywords = blacklist_keywords;
    }

    @Override
    public void run() {
        for(String str : blacklist_keywords){
            text.replaceAll(str,"");
        }
        try(Connection connection = MySqlConnector.getInstance().getConnection()) {
            utils.updateTextTask(connection,id,text);
            utils.updateCompletedTask(connection,id);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
