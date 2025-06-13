package com.example.exceldemo.Entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SalaryBudgetDTO {

    @ExcelProperty("ID")
    /* ID */
    private String id;
    /** 组织编码 */
    @ExcelProperty("组织编码")
    private String orgCode;
    /** 组织名称 */
    @ExcelProperty("组织名称")
    private String orgName;
    /** 批复状态 */
    @ExcelProperty("批复状态")
    private String salaryAppStatus;

    /** 预算申报数总数 */
    @ExcelProperty({"职工工资总额", "预算申报数"})
    private String declareSum;
    /** 批复数总 */
    @ExcelProperty({"职工工资总额", "批复数"})
    private String approvalSum;

    /** 预算申报 */
    @ExcelProperty({"工资总额预算基数", "预算申报数"})
    private String declareTotal;
    /** 批复数 */
    @ExcelProperty({"工资总额预算基数", "批复数"})
    private String approvalTotal;

    /** 预算申报1 */
    @ExcelProperty({"新增效益工资", "预算申报数"})
    private String declare1;
    /** 批复数1 */
    @ExcelProperty({"新增效益工资", "批复数"})
    private String approval1;

    /** 预算申报2 */
    @ExcelProperty({"效益工资增幅", "预算申报数"})
    private String declare2;
    /** 批复数2 */
    @ExcelProperty({"效益工资增幅", "批复数"})
    private String approval2;

    /** 预算申报3 */
    @ExcelProperty({"利润总额", "预算申报数"})
    private String declare3;
    /** 批复数3空 */
    @ExcelProperty({"利润总额", "批复数"})
    private String approval3;

    /** 预算申报4 */
    @ExcelProperty({"年度绩效考核等级", "预算申报数"})
    private String declare4;
    /** 批复数4空 */
    @ExcelProperty({"年度绩效考核等级", "批复数"})
    private String approval4;

    /** 预算申报5 */
    @ExcelProperty({"一、实施兼并重组处僵治困、新设立（减少）企业等预算管理范围变化", "预算申报数"})
    private String declare5;
    /** 批复数5 */
    @ExcelProperty({"一、实施兼并重组处僵治困、新设立（减少）企业等预算管理范围变化", "批复数"})
    private String approval5;

    /** 预算申报6 */
    @ExcelProperty({"二、承担国家及省部级科技攻关任务", "预算申报数"})
    private String declare6;
    /** 批复数6 */
    @ExcelProperty({"二、承担国家及省部级科技攻关任务", "批复数"})
    private String approval6;

    /** 预算申报7 */
    @ExcelProperty({"三、设立国家及省部级创新平台", "预算申报数"})
    private String declare7;
    /** 批复数7 */
    @ExcelProperty({"三、设立国家及省部级创新平台", "批复数"})
    private String approval7;

    /** 预算申报8 */
    @ExcelProperty({"四、吸引和保留两院院士、海外高层次人才引进计划专家、中央企业关键核心技术攻关人才及团队、集团公司认定的高层次人才", "预算申报数"})
    private String declare8;
    /** 批复数8 */
    @ExcelProperty({"四、吸引和保留两院院士、海外高层次人才引进计划专家、中央企业关键核心技术攻关人才及团队、集团公司认定的高层次人才", "批复数"})
    private String approval8;

    /** 预算申报9 */
    @ExcelProperty({"五、职务科技成果转化分红激励、集团公司批准实施的科技创新激励和其他中长期激励", "预算申报数"})
    private String declare9;
    /** 批复数9 */
    @ExcelProperty({"五、职务科技成果转化分红激励、集团公司批准实施的科技创新激励和其他中长期激励", "批复数"})
    private String approval9;

    /** 预算申报10 */
    @ExcelProperty({"六、承担共抓长江大保护等国家和集团公司重大专项任务和高新工程等特殊业务", "预算申报数"})
    private String declare10;
    /** 批复数10 */
    @ExcelProperty({"六、承担共抓长江大保护等国家和集团公司重大专项任务和高新工程等特殊业务", "批复数"})
    private String approval10;

    /** 预算申报11 */
    @ExcelProperty({"七、集团公司重点发展的创新业务单元、处于战略培育期业务以及发展尚未进入平稳期的企业增加人员，以及成建制划转人员", "预算申报数"})
    private String declare11;
    /** 批复数11 */
    @ExcelProperty({"七、集团公司重点发展的创新业务单元、处于战略培育期业务以及发展尚未进入平稳期的企业增加人员，以及成建制划转人员", "批复数"})
    private String approval11;

    /** 预算申报8_1 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.1特别奖励","预算申报数"})
    private String declare8_1;
    /** 批复数8_1 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.1特别奖励","批复数"})
    private String approval8_1;
    /** 预算申报8_2 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.2专项奖励","预算申报数"})
    private String declare8_2;
    /** 批复数8_2 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.2专项奖励","批复数"})
    private String approval8_2;
    /** 预算申报8_3 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.3安全风险责任金","预算申报数"})
    private String declare8_3;
    /** 批复数8_3 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.3安全风险责任金","批复数"})
    private String approval8_3;
    /** 预算申报8_4 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.4一线津补贴","预算申报数"})
    private String declare8_4;
    /** 批复数8_4 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.4一线津补贴","批复数"})
    private String approval8_4;
    /** 预算申报8_5 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.5市场化薪酬制度改革","预算申报数"})
    private String declare8_5;
    /** 批复数8_5 */
    @ExcelProperty({"八、经集团公司批准实施的特别奖励、专项奖励、安全风险责任金、一线津补贴、市场化薪酬制度改革等事项", "8.5市场化薪酬制度改革","批复数"})
    private String approval8_5;

    /** 预算申报12 */
    @ExcelProperty({"九、扩大高校毕业生招聘一次性增人增资", "预算申报数"})
    private String declare12;
    /** 批复数12 */
    @ExcelProperty({"九、扩大高校毕业生招聘一次性增人增资", "批复数"})
    private String approval12;
    /** 预算申报13 */
    @ExcelProperty({"十、企业负责人薪酬", "预算申报数"})
    private String declare13;
    /** 批复数13 */
    @ExcelProperty({"十、企业负责人薪酬", "批复数"})
    private String approval13;
    /** 预算申报14 */
    @ExcelProperty({"十、其他集团公司允许的单列事项（一）", "预算申报数"})
    private String declare14;
    /** 批复数14 */
    @ExcelProperty({"十、其他集团公司允许的单列事项（一）", "批复数"})
    private String approval14;
    /** 预算申报15 */
    @ExcelProperty({"其他集团公司允许的单列事项（二）", "预算申报数"})
    private String declare15;
    /** 批复数15 */
    @ExcelProperty({"其他集团公司允许的单列事项（二）", "批复数"})
    private String approval15;
    /** 预算申报16 */
    @ExcelProperty({"其他集团公司允许的单列事项（三）", "预算申报数"})
    private String declare16;
    /** 批复数16 */
    @ExcelProperty({"其他集团公司允许的单列事项（三）", "批复数"})
    private String approval16;
    /** 预算申报17 */
    @ExcelProperty({"其他集团公司允许的单列事项（四）", "预算申报数"})
    private String declare17;
    /** 批复数17 */
    @ExcelProperty({"其他集团公司允许的单列事项（四）", "批复数"})
    private String approval17;
    /** 预算申报18 */
    @ExcelProperty({"其他集团公司允许的单列事项（五）", "预算申报数"})
    private String declare18;
    /** 批复数18 */
    @ExcelProperty({"其他集团公司允许的单列事项（五）", "批复数"})
    private String approval18;
    /** 预算申报19 */
    @ExcelProperty({"十五、实施走出去战略，设立境外单位", "预算申报数"})
    private String declare19;
    /** 批复数19 */
    @ExcelProperty({"十五、实施走出去战略，设立境外单位", "批复数"})
    private String approval19;
    /** 预算申报20 */
    @ExcelProperty({"一次性核减", "预算申报数"})
    private String declare20;
    /** 批复数20 */
    @ExcelProperty({"一次性核减", "批复数"})
    private String approval20;

    /** 预算申报21 */
    @ExcelProperty({"新能源专项奖励", "预算申报数"})
    private String declare21;
    /** 批复数21 */
    @ExcelProperty({"新能源专项奖励", "批复数"})
    private String approval21;
//    /** 预算申报22 */
//    @ExcelProperty({"工资总额预算基数", "预算申报数"})
//    private String declare22;
//    /** 批复数22 */
//    @ExcelProperty({"工资总额预算基数", "批复数"})
//    private String approval22;
//    /** 预算申报23 */
//    @ExcelProperty({"工资总额预算基数", "预算申报数"})
//    private String declare23;
//    /** 批复数23 */
//    @ExcelProperty({"工资总额预算基数", "批复数"})
//    private String approval23;






}
