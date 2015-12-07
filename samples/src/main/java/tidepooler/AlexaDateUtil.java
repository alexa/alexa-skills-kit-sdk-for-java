/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package tidepooler;

import java.util.Calendar;
import java.util.Date;

/**
 * Provides date and time utilities to format responses in a manner appropriate for speech output.
 */
public final class AlexaDateUtil {

    private AlexaDateUtil() {
    }

    /**
     * Hour representing threshold for morning.
     */
    private static final int MORNING_THRESHOLD = 12;

    /**
     * Hour representing threshold for afternoon.
     */
    private static final int AFTERNOON_THRESHOLD = 17;

    /**
     * Hour representing threshold for evening.
     */
    private static final int EVENING_THRESHOLD = 20;

    /**
     * Midnight hour.
     */
    private static final int MIDNIGHT_HOUR = 12;

    /**
     * Minutes threshold before which a "0" should be appended to the minute value.
     */
    private static final int MINUTE_TWO_DIGIT_THRESHOLD = 10;

    private static final String[] DAYS_OF_MONTH = {
            "1st",
            "2nd",
            "3rd",
            "4th",
            "5th",
            "6th",
            "7th",
            "8th",
            "9th",
            "10th",
            "11th",
            "12th",
            "13th",
            "14th",
            "15th",
            "16th",
            "17th",
            "18th",
            "19th",
            "20th",
            "21st",
            "22nd",
            "23rd",
            "24th",
            "25th",
            "26th",
            "27th",
            "28th",
            "29th",
            "30th",
            "31st"
    };

    private static final String[] MONTHS = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    private static final String[] DAYS_OF_WEEK = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    /**
     * Returns a speech formatted date, without the time. If the year is the same as current year,
     * it is omitted. Example: 'Friday June 12th', '6/5/2016'
     */
    public static String getFormattedDate(Date date) {
        Date today = new Date();

        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(today);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        if (todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
            return DAYS_OF_WEEK[dateCal.get(Calendar.DAY_OF_WEEK) - 1] + ' '
                    + MONTHS[dateCal.get(Calendar.MONTH)] + ' '
                    + DAYS_OF_MONTH[dateCal.get(Calendar.DATE) - 1];
        } else {
            return DAYS_OF_WEEK[dateCal.get(Calendar.DAY_OF_WEEK) - 1] + ' '
                    + (dateCal.get(Calendar.MONTH) + 1) + '/' + dateCal.get(Calendar.DATE) + '/'
                    + dateCal.get(Calendar.YEAR);
        }
    }

    /**
     * Returns a speech formatted time, without a date, based on a period in the day. E.g. '12:35 in
     * the afternoon'
     * 
     * @return
     */
    public static String getFormattedTime(Date date) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        int hours = dateCal.get(Calendar.HOUR_OF_DAY);
        int minutes = dateCal.get(Calendar.MINUTE);
        String minuteString;

        String periodOfDay;
        if (hours < MORNING_THRESHOLD) {
            periodOfDay = " in the morning";
        } else if (hours < AFTERNOON_THRESHOLD) {
            periodOfDay = " in the afternoon";
        } else if (hours < EVENING_THRESHOLD) {
            periodOfDay = " in the evening";
        } else {
            periodOfDay = " at night";
        }

        hours = dateCal.get(Calendar.HOUR);
        hours = hours == 0 ? MIDNIGHT_HOUR : hours; // handle noon and midnight
        minuteString =
                minutes < MINUTE_TWO_DIGIT_THRESHOLD ? "0" + Integer.toString(minutes) : Integer
                        .toString(minutes);
        String formattedTime = Integer.toString(hours) + ":" + minuteString + periodOfDay;
        return formattedTime;
    }

    /**
     * Returns a speech formatted, without a date, based on am/pm E.g. '12:35 pm'
     */
    public static String getFormattedTimeAmPm(Date date) {
        Calendar dateCal = Calendar.getInstance();
        int hours = dateCal.get(Calendar.HOUR_OF_DAY);
        int minutes = dateCal.get(Calendar.MINUTE);
        String minuteString;

        String ampm = hours >= MIDNIGHT_HOUR ? "pm" : "am";
        hours = dateCal.get(Calendar.HOUR);
        hours = hours == 0 ? MIDNIGHT_HOUR : hours; // handle noon and midnight
        minuteString =
                minutes < MINUTE_TWO_DIGIT_THRESHOLD ? "0" + Integer.toString(minutes) : Integer
                        .toString(minutes);
        String formattedTime = Integer.toString(hours) + ":" + minuteString + " " + ampm;
        return formattedTime;
    }
}
