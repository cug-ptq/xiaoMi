# 第二题
-- 插入数据
INSERT INTO employee (name, gender, phone, email, birth_date, hire_date, manager_id, position_id, status, gmt_created, gmt_modify)
VALUES (
           '韩磊',
           0,
           '13599991111',
           'hanlei@xm.com',
           '1991-01-01',
           '2008-08-08',
           NULL,
           1,
           1,
           '2008-08-08 00:00:00',
           '2018-08-08 12:00:01'
       );

-- 插入第二条员工记录（张三）
INSERT INTO employee (name, gender, phone, email, birth_date, hire_date, manager_id, position_id, status, gmt_created, gmt_modify)
VALUES (
           '张三',
           1,
           '18688880169',
           'zhangsan@xm.com',
           '1992-02-02',
           '2010-01-08',
           1,  -- 上级领导ID是1（对应Fe）
           2,
           1,
           '2010-01-08 00:00:00',
           '2010-01-08 00:00:00'
       );


-- 插入部门数据
INSERT INTO department (id, name, manager_id, parent_id, status, gmt_created, gmt_modify)
VALUES
    (1, '腾达集团', 1, NULL, 1, '2008-08-08 00:00:00', '2008-08-08 00:00:00'),
    (2, '销售部', 2, 1, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01'),
    (3, '财务部', 3, 1, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01'),
    (4, '税务部', 4, 3, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01');


-- 插入员工部门关系数据
INSERT INTO emp_dep_relation (id, employee_id, department_id, start_date, end_date, status, gmt_created, gmt_modify)
VALUES
    (1, 1, 1, '2008-08-08', NULL, 1, '2008-08-08 00:00:00', '2008-08-08 00:00:00'),
    (2, 2, 2, '2008-08-08', NULL, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01'),
    (3, 3, 3, '2008-08-08', NULL, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01'),
    (4, 4, 4, '2008-08-08', NULL, 1, '2008-08-08 00:00:00', '2018-08-08 12:00:01');