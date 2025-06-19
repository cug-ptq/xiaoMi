package com.example.xiaomi.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String SPLIT = ",";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "成功"),
        UPDATE_ERROR("0001", "更新失败"),
        SELECT_ERROR("0002", "查询失败"),
        DELETE_ERROR("0003", "删除失败"),
        ADD_ERROR("0004", "新增失败"),
        DEDUCE("0005", "扣减失败")
        ;

        private String code;
        private String info;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Gender {
        MALE(1,"男"), FEMALE(2,"女");

        private int code;
        private String gender;
    }

    public static String PRODUCE_DEDUCE = "product_deduce";
    public static String EMPLOYEE_UPDATE = "employee_update";

}