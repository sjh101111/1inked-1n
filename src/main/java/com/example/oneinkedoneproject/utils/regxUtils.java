package com.example.oneinkedoneproject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class regxUtils {
    /**
        * @brief 이메일 정규식 형태가 일치하는지 확인합니다.
        * The email couldn't start or finish with a dot
        * The email shouldn't contain spaces into the string
        * The email shouldn't contain special chars (<:, *,ecc)
        * The email could contain dots in the middle of mail address before the @
        * The email could contain a double doman ( '.de.org' or similar rarity)
    **/
    public static boolean emailRegxCheck(String email){
        String regx = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * @brief 비밀번호 정규식 형태가 일치하는지 확인합니다.
     * 형태는
     * password must contain 1 number (0-9)
     * password must contain 1 uppercase letters
     * password must contain 1 lowercase letters
     * password must contain 1 non-alpha numeric number
     * password is 8-16 characters with no space
     * @param {String} inputString
     * @returns boolean
     */
    public static boolean pwdRegxCheck(String password){
        String regx = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[\\W\\_])[\\S]{8,16}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
