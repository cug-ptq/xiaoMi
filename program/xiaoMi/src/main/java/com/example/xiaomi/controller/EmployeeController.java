package com.example.xiaomi.controller;

import com.example.xiaomi.constant.Constants;
import com.example.xiaomi.entity.Employee;
import com.example.xiaomi.entity.Response;
import com.example.xiaomi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

@RequestMapping("/api/xiaomi/employee")
@RestController
@Slf4j
public class EmployeeController {

    @Resource
    public EmployeeService employeeService;
    @Resource
    public RocketMQTemplate rocketMQTemplate;

    /**
     * 新增员工
     * @param employee 员工对象，json传入
     * @return 新增是否成功，携带生成的ID
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Response<String> addEmployee(@RequestBody Employee employee) {
        try {
            employeeService.insertEmployee(employee);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.ADD_ERROR.getCode())
                    .info(Constants.ResponseCode.ADD_ERROR.getInfo())
                    .build();
        }
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .build();

    }

    /**
     * 根据ID删除员工
     * @param id 员工ID
     * @return 删除是否成功
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Response<String> deleteEmployee(@PathVariable int id) {
        try {
            employeeService.deleteEmployeeById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.DELETE_ERROR.getCode())
                    .info(Constants.ResponseCode.DELETE_ERROR.getInfo())
                    .build();

        }
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .build();
    }

    /**
     * 更新员工基本信息（姓名、性别、电话、邮箱、上级ID、职位ID、状态）
     * @param employee 员工对象，必须包含ID
     * @return 更新是否成功
     */
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Response<String> updateEmployee(@RequestBody Employee employee) {
        boolean success = employeeService.updateEmployeeById(employee);
        if (success){
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } else {
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UPDATE_ERROR.getCode())
                    .info(Constants.ResponseCode.UPDATE_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 根据ID查询员工
     * @param id 员工ID
     * @return 员工对象或404
     */
    @GetMapping("/get/{id}")
    public Response<String> getEmployeeById(@PathVariable int id) {
        Employee employee;
        try {
            employee = employeeService.getEmployeeById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SELECT_ERROR.getCode())
                    .info(Constants.ResponseCode.SELECT_ERROR.getInfo())
                    .build();

        }
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .data(employee.toString())
                .build();
    }

}