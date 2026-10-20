package com.alana;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    public LogEntry parse(String linha) {
        Pattern padrao = Pattern.compile("^(\\S+) \\S+ \\S+ \\[([^\\]]+)\\] \"(\\S+) (\\S+)[^\"]*\" (\\d{3})"); //cria o molde da regex
        Matcher matcher = padrao.matcher(linha);

        if(matcher.find()) {
            String ip = matcher.group(1);
            String time = matcher.group(2);
            String method = matcher.group(3);
            String endpoint = matcher.group(4);
            int statusCode = Integer.parseInt(matcher.group(5));

            //System.out.println(ip +" "+ time + " "+ method + " "+ endpoint + " "+ statusCode);

            return new LogEntry(ip, time, method, endpoint, statusCode);
        }
        return null;
    }
}
