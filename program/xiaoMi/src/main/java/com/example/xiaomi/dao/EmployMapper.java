package com.example.xiaomi.dao;

import com.example.xiaomi.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployMapper {

    /**
     * 新增职工数据
     * @param employee 职工实体
     */
    int insertEmployee(Employee employee);

    /**
     * 查询职工数据
     * @param id 职工ID
     * @return 返回实体
     */
    Employee getEmployeeById(@Param("id") int id);

    /**
     * 更新职工数据
     * @param employee 必须包含 ID
     */
    int updateEmployeeById(Employee employee);

    /**
     * 删除职工信息
     * @param id 职工ID
     */
    void deleteEmployeeById(@Param("id") int id);
}
