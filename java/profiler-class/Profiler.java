package com.groupdocs.viewer;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liosha
 * Date: 29.11.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Profiler {
    private static long time = 0;
    private static HashMap<String, HashMap<String, String>> profileDetails = new HashMap<String, HashMap<String, String>>();
    private static HashMap<String, List<Long>> profileTimes = new HashMap<String, List<Long>>();
    private static final String KEY_FILE_NAME = "File name: '{0}'";
    private static final String KEY_CLASS_NAME = "Class name: '{0}'";
    private static final String KEY_METHOD_NAME = "Method name: '{0}'";
    private static final String KEY_LINE_NUMBER = "End code line: {0}";
    private static final String KEY_TIME = "Time: {0}ms cicles: {1}";


    public static void profilePoint() {
        profilePoint(null);
    }
    private static void profilePoint(String title) {
        long newTime = new Date().getTime();
        if (time == 0) {
            time = newTime;
            return;
        }

        StackTraceElement stackTraceElement = null;
        try { throw new Exception(); } catch (Exception e) { stackTraceElement = e.getStackTrace()[(title == null ? 2 : 1)]; }
        if (stackTraceElement == null) {
            return;
        }

        final long timeValue = newTime - time;
        final String fileName = stackTraceElement.getFileName();
        final String className = stackTraceElement.getClassName();
        final String methodName = stackTraceElement.getMethodName();
        final int lineNumber = stackTraceElement.getLineNumber();

        String mapKey = className + "@" + methodName + "#" + Integer.toString(lineNumber);
        if (profileDetails.containsKey(mapKey) == false) {
            profileDetails.put(mapKey, new HashMap<String, String>(){{
                put(KEY_FILE_NAME, fileName);
                put(KEY_CLASS_NAME, className);
                put(KEY_METHOD_NAME, methodName);
                put(KEY_LINE_NUMBER, Integer.toString(lineNumber));
            }});
            profileTimes.put(mapKey, new ArrayList<Long>(){{
                add(timeValue);
            }});
        } else {
            profileTimes.get(mapKey).add(timeValue);
        }

        time = new Date().getTime();
    }

    public static void showData(){
        for (String key : profileDetails.keySet()) {
            HashMap<String, String> details = profileDetails.get(key);

            long sum = 0;
            for (Long tm : profileTimes.get(key)) {
                sum += tm;
            }

            double sred =  sum / profileTimes.get(key).size();

            if (sred > 0) {
                System.out.println("\r\n=================== ==================");
                System.out.println(KEY_FILE_NAME.replace("{0}", details.get(KEY_FILE_NAME)));
                System.out.println(KEY_CLASS_NAME.replace("{0}", details.get(KEY_CLASS_NAME)));
                System.out.println(KEY_METHOD_NAME.replace("{0}", details.get(KEY_METHOD_NAME)));
                System.out.println(KEY_LINE_NUMBER.replace("{0}", details.get(KEY_LINE_NUMBER)));
                System.out.println(KEY_TIME.replace("{0}", Double.toString(sred)).replace("{1}", Integer.toString(profileTimes.size())));
            }
        }
        profileDetails.clear();
        profileTimes.clear();
    }
}
