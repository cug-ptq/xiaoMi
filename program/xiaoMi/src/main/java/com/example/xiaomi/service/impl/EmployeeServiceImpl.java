package com.example.xiaomi.service.impl;

import com.example.xiaomi.cache.impl.EmployeeCacheHelper;
import com.example.xiaomi.constant.Constants;
import com.example.xiaomi.dao.EmployMapper;
import com.example.xiaomi.entity.Employee;
import com.example.xiaomi.redis.RedissonService;
import com.example.xiaomi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Resource
    private EmployMapper employMapper;
    @Resource
    private EmployeeCacheHelper employeeCacheHelper;
    @Resource
    private RedissonService redissonService;

    @Override
    public void insertEmployee(Employee employee) {
        try {
            employMapper.insertEmployee(employee);
            log.info("加入缓存");
            employeeCacheHelper.put(employee.getId(),employee, 20000);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        Employee employee = null;
        boolean lock = false;
        RLock rLock = redissonService.getLock("employ");;
        try {
            if ((employee = employeeCacheHelper.get(id)) != null) {
                log.info("查询缓存");
                return employee;
            }
            lock = rLock.tryLock(3,10, TimeUnit.SECONDS);
            if (lock) {
                employee = employMapper.getEmployeeById(id);
            }
            else {
                log.info("Could not acquire lock");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        finally {
            // 释放锁（如果成功加锁了）
            if (lock) {
                rLock.unlock();
                System.out.println("Lock released");
            }
        }
        return employeeCacheHelper.put(id, employee, 180_000);
    }

    @Override
    public boolean updateEmployeeById(Employee employee) {
        boolean success = false;
        boolean lock = false;
        RLock rLock = redissonService.getLock(Constants.EMPLOYEE_UPDATE);;
        try {
            lock = rLock.tryLock(3,10, TimeUnit.SECONDS);
            if (lock) {
                log.info("获得锁");
                employMapper.updateEmployeeById(employee);
                log.info("更新缓存");
                employeeCacheHelper.put(employee.getId(),employee, 180_000);
                success = true;
                Thread.sleep(5000);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        finally {
            // 释放锁（如果成功加锁了）
            if (lock) {
                rLock.unlock();
                System.out.println("Lock released");
            }
        }
        return success;
    }

    @Override
    public void deleteEmployeeById(int id) {
        try {
            employMapper.deleteEmployeeById(id);
            employeeCacheHelper.evict(id);
            log.info("删除缓存");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
