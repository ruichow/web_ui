package com.test.pc.util;

import java.util.Calendar;

/**
 * 获取当前时间（精确到ms）工具类
 *
 * @author：关河九州
 * @date：2019/12/10 15:54
 * @version：1.0
 */
public class ClockTool {
    /**
     * 私有化工具类，拒绝工具类实例化
     */
    private ClockTool() {
        throw new Error("ClockTool 不允许实例化！");
    }

    /**
     * 获取 String 拼接的当前时间
     */
    public static String getCurrentTime(){
        // 获取当前时间，精确到毫秒级别
        Calendar cld = Calendar.getInstance();
        int YY = cld.get(Calendar.YEAR);
        int MM = cld.get(Calendar.MONTH)+1;
        int DD = cld.get(Calendar.DATE);
        int HH = cld.get(Calendar.HOUR_OF_DAY);
        int mm = cld.get(Calendar.MINUTE);
        int SS = cld.get(Calendar.SECOND);
        int MI = cld.get(Calendar.MILLISECOND);
        // 返回当前时间，精确到毫秒
        return "" + YY + MM + DD + HH + mm + SS + MI;
    }

    /**
     * 获取String拼接的当前时间年月日
     * */
    public static String getTime(){
        // 获取当前时间，精确到毫秒级别
        Calendar cld = Calendar.getInstance();
        String  YY = cld.get(Calendar.YEAR)+"";
        String  MM = (cld.get(Calendar.MONTH)+1+"").length()>1? (cld.get(Calendar.MONTH)+1+""): "0"+(cld.get(Calendar.MONTH)+1+"");
        String  DD = (cld.get(Calendar.DATE)+"").length()>1? cld.get(Calendar.DATE)+"" : "0"+cld.get(Calendar.DATE);

        // 返回当前时间，精确到毫秒
        return ""+YY+"-"+MM+"-"+DD;
    }

    public static void main(String[] args) {
        System.out.println(getTime());
    }
}
