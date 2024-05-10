package com.example.oneinkedoneproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithdrawUserRequestDto {
    private String email;
    private String password;
    private boolean isWithdraw;

    public boolean getIsWithdraw(){
        return this.isWithdraw;
    }
}
