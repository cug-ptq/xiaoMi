package com.example.xiaomi.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    /**
     * 员工唯一ID
     */
    private Integer id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别：0-未知, 1-男, 2-女
     */
    private Integer gender;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 入职日期
     */
    private Date hireDate;

    /**
     * 直属上级ID
     */
    private Long managerId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 状态：0-离职, 1-在职, 2-休假
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 修改时间
     */
    private Date gmtModify;
}