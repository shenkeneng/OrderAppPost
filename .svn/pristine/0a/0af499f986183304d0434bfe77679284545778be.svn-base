/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.ewu.core.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ewu.core.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimeUtils {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private int time = 60;

    private Context context;

    private Timer timer;

    private TextView btnSure;

    private String btnText;

    public TimeUtils(Context context, TextView btnSure, String btnText) {
        super();
        this.context = context;
        this.btnSure = btnSure;
        this.btnText = btnText;
    }

    public void RunTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run(){
                time--;
                Message msg=handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        };


        timer.schedule(task, 100, 1000);
    }


    private Handler handler =new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if(time>0){
                        btnSure.setEnabled(false);
                        btnSure.setText(time+"秒后重新发送");
                        btnSure.setTextSize(14);
                        btnSure.setTextColor(context.getResources().getColor(R.color.card_background));
                        btnSure.setSelected(true);
                    }else{
                        timer.cancel();
                        btnSure.setText(btnText);
                        btnSure.setEnabled(true);
                        btnSure.setTextSize(14);
                        btnSure.setTextColor(context.getResources().getColor(R.color.card_background));
                        btnSure.setSelected(false);
                    }

                    break;


                default:
                    break;
            }

        };
    };

//    private static final DateTimeFormatter DOUBAN_DATE_TIME_FORMATTER =
//            new DateTimeFormatterBuilder()
//                    .append(DateTimeFormatter.ISO_LOCAL_DATE)
//                    .appendLiteral(' ')
//                    .append(DateTimeFormatter.ISO_LOCAL_TIME)
//                    .toFormatter()
//                    .withChronology(IsoChronology.INSTANCE);
//
//    private static final ZoneId DOUBAN_ZONED_ID = ZoneId.of("Asia/Shanghai");
//
//    private static final Duration JUST_NOW_DURATION = Duration.ofMinutes(5);
//    private static final Duration MINUTE_PATTERN_DURATION = Duration.ofHours(1);
//    private static final Duration HOUR_PATTERN_DURATION = Duration.ofHours(2);
//
//    /**
//     * @throws DateTimeParseException
//     */
//    public static ZonedDateTime parseDoubanDateTime(String doubanDateTime) {
//        return ZonedDateTime.of(LocalDateTime.parse(doubanDateTime, DOUBAN_DATE_TIME_FORMATTER),
//                DOUBAN_ZONED_ID);
//    }
//
//    public static String formatDateTime(ZonedDateTime dateTime, Context context) {
//        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(dateTime.getZone());
//        LocalDate date = dateTime.toLocalDate();
//        LocalDate nowDate = now.toLocalDate();
//        if (date.equals(nowDate)) {
//            Duration duration = Duration.between(dateTime, now);
//            if (duration.compareTo(Duration.ZERO) > 0) {
//                if (duration.compareTo(JUST_NOW_DURATION) < 0) {
//                    return context.getString(R.string.just_now);
//                } else if (duration.compareTo(MINUTE_PATTERN_DURATION) < 0) {
//                    return context.getString(R.string.minute_format,
//                            Math.round((float) duration.getSeconds() / SECONDS_PER_MINUTE));
//                } else if (duration.compareTo(HOUR_PATTERN_DURATION) < 0) {
//                    return context.getString(R.string.hour_format,
//                            Math.round((float) duration.getSeconds() / SECONDS_PER_HOUR));
//                }
//            }
//            return DateTimeFormatter
//                    .ofPattern(context.getString(R.string.today_hour_minute_pattern))
//                    .format(dateTime);
//        }
//        if (date.plusDays(1).equals(nowDate)) {
//            return DateTimeFormatter
//                    .ofPattern(context.getString(R.string.yesterday_hour_minute_pattern))
//                    .format(dateTime);
//        } else if (date.getYear() == nowDate.getYear()) {
//            return DateTimeFormatter
//                    .ofPattern(context.getString(R.string.month_day_hour_minute_pattern))
//                    .format(dateTime);
//        } else {
//            return DateTimeFormatter
//                    .ofPattern(context.getString(R.string.date_hour_minute_pattern))
//                    .format(dateTime);
//        }
//    }
//
//    /**
//     * Use  instead if the text is to be set on a
//     * {@code TextView}.
//     */
//    public static String formatDoubanDateTime(String doubanDateTime, Context context) {
//        try {
//            return formatDateTime(parseDoubanDateTime(doubanDateTime), context);
//        } catch (DateTimeParseException e) {
//            LogUtils.e("Unable to parse date time: " + doubanDateTime);
//            e.printStackTrace();
//            return doubanDateTime;
//        }
//    }
}
