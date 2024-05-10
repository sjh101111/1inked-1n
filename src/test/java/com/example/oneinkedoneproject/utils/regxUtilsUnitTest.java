package com.example.oneinkedoneproject.utils;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class regxUtilsUnitTest {
    @Test
    @DisplayName("이메일 정규식 테스트")
    void emailRegxTest(){
        //given
        //1. @이후 주소가 존재하지 않는 경우
        String secPrefixEmpty =  "tes1231t";
        //2. prefix에 특수문자가 존재하는경우
        String specialSymbol =  "tes12!#31t@naver.com";

        //when
        boolean secPrefixCase = regxUtils.emailRegxCheck(secPrefixEmpty);
        boolean specialSymbolCase = regxUtils.emailRegxCheck(specialSymbol);

        //then
        assertThat(secPrefixCase).isFalse();
        assertThat(specialSymbolCase).isFalse();
    }

    @Test
    @DisplayName("비밀번호 정규식 테스트")
    void pwdRegxTest(){
        //given
        String lowerRequest = "dlxomlawofmi";
        String upperRequest = "DEFIWAMDLOQ";
        String shortRequest = "a!Q1";
        String longRequest = "Q1W!femfi141qaodaifmwaofimweoim";
        String exceptSpecialSymbolRequest = "1q2w3e4rqiwmd";
        String normalRequest =  "aofmEmqo!1285m";

        //when
        boolean lowerCase = regxUtils.pwdRegxCheck(lowerRequest);
        boolean upperCase = regxUtils.pwdRegxCheck(upperRequest);
        boolean shortCase = regxUtils.pwdRegxCheck(shortRequest);
        boolean longCase = regxUtils.pwdRegxCheck(longRequest);
        boolean exceptSpecialSymbolCase = regxUtils.pwdRegxCheck(exceptSpecialSymbolRequest);
        boolean normalCase = regxUtils.pwdRegxCheck(normalRequest);


        //then
        assertThat(lowerCase).isFalse();
        assertThat(upperCase).isFalse();
        assertThat(shortCase).isFalse();
        assertThat(longCase).isFalse();
        assertThat(exceptSpecialSymbolCase).isFalse();
        assertThat(normalCase).isTrue();
    }

}
