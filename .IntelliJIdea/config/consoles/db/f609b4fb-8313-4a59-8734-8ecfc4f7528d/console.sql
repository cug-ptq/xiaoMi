CREATE TABLE `position` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`name` char(20) NOT NULL DEFAULT '' COMMENT '名称',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用, 1-启用',
`gnt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gnt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';


CREATE TABLE `employee` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工唯一ID',
`name` varchar(255) NOT NULL COMMENT '员工姓名',
`gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别：0-未知, 1-男, 2-女',
`phone` varchar(64) DEFAULT NULL COMMENT '联系电话',
`email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
`birth_date` date DEFAULT NULL COMMENT '出生日期',
`hire_date` date NOT NULL COMMENT '入职日期',
`manager_id` bigint(20) DEFAULT NULL COMMENT '直属上级ID',
`position_id` bigint(20) NOT NULL COMMENT '职位ID',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-离职, 1-在职, 2-休假',
`gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工信息表';


CREATE TABLE `department` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门唯一ID',
`name` varchar(255) NOT NULL COMMENT '部门名称',
`manager_id` bigint(20) DEFAULT NULL COMMENT '部门负责人ID（关联employee表）',
`parent_id` bigint(20) DEFAULT NULL COMMENT '父部门ID（树形结构）',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用, 1-启用',
`gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息表';


CREATE TABLE `project` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目唯一ID',
`name` varchar(255) NOT NULL COMMENT '项目名称',
`manager_id` bigint(20) NOT NULL COMMENT '项目经理ID',
`start_date` date NOT NULL COMMENT '开始日期',
`end_date` date DEFAULT NULL COMMENT '结束日期（NULL表示未截止）',
`status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-未开始, 1-进行中, 2-已延期, 3-已完成',
`gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目信息表';

CREATE TABLE `emp_dep_relation` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系唯一ID',
`employee_id` bigint(20) NOT NULL COMMENT '员工ID（关联employee表）',
`department_id` bigint(20) NOT NULL COMMENT '部门ID（关联department表）',
`start_date` date NOT NULL COMMENT '关系生效日期',
`end_date` date DEFAULT NULL COMMENT '关系结束日期（NULL表示长期有效）',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-无效, 1-有效, 2-待审核',
`gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工-部门关系表';

CREATE TABLE `emp_pro_relation` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系唯一ID',
`employee_id` bigint(20) NOT NULL COMMENT '员工ID（关联employee表）',
`project_id` bigint(20) NOT NULL COMMENT '项目ID（关联project表）',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-未参与, 1-参与中, 2-已退出',
`gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工-项目关系表';


-- 查询张三领导
SELECT m.name
FROM employee e
         JOIN employee m ON e.manager_id = m.id
WHERE e.name = '张三';

-- 第三题 修改属性
ALTER TABLE `position`
    MODIFY COLUMN `name` VARCHAR(200);

-- 第四题 查询财务部员工数
-- 统计财务部(部门ID=3)的员工数量
SELECT COUNT(*) AS finance_employee_count
FROM emp_dep_relation
WHERE department_id = 3
  AND status = 1
  AND (end_date IS NULL OR end_date >= CURDATE());

-- 财务部子部门的员工数
SELECT COUNT(DISTINCT ed.employee_id) AS total_employees
FROM emp_dep_relation ed
WHERE ed.status = 1
  AND (ed.end_date IS NULL OR ed.end_date >= CURDATE())
  AND ed.department_id IN (
    -- 财务部本身
    SELECT id FROM department WHERE id = 3

    UNION

    -- 一级子部门
    SELECT id FROM department WHERE parent_id = 3

    UNION

    -- 二级子部门
    SELECT d2.id
    FROM department d1
             JOIN department d2 ON d2.parent_id = d1.id
    WHERE d1.parent_id = 3
);

-- 查询没有参加任何项目的员工姓名
SELECT e.name AS employee_name
FROM employee e
         LEFT JOIN emp_pro_relation epr ON e.id = epr.employee_id
WHERE epr.employee_id IS NULL;


CREATE TABLE Product (
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100) NOT NULL,
 stock INT NOT NULL DEFAULT 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';


INSERT INTO Product (id, name, stock) VALUES
(1, 'iPhone 15 Pro', 100),
(2, '纯棉T恤（白色）', 200),
(3, '有机牛奶 1L装', 150),
(4, '无线蓝牙耳机', 80),
(5, '精装笔记本套装', 120);