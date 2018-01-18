package com.readnews.app2018.common;

import android.text.TextUtils;

/**
 * Created by sanglx on 1/18/18.
 */

public class Utils {
    public static boolean isVNPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }

        //String regex = "^(0|84)(9\\d|16[2-9]|12\\d|86|88|89|186|188|199)(\\d{7})$";

        if (phone.startsWith("0"))
            phone = phone.replaceFirst("0", "");
        else if (phone.startsWith("84"))
            phone = phone.replaceFirst("84", "");

        String regex = "^(9\\d|16[2-9]|12\\d|86|88|89|186|188|199)(\\d{7})$";

        phone = phone.replace("+", "");
        if (phone.matches(regex)) {
            return true;
        }
        return false;
    }
}
