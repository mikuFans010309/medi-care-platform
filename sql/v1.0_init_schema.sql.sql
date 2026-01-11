
CREATE DATABASE `medi_care_platform` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';

CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号（手机号/邮箱）',
  `password` VARCHAR(100) NOT NULL COMMENT '加密密码',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `id_card` VARCHAR(18) UNIQUE COMMENT '身份证号（医生必填）',
  `phone` VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
  `user_type` TINYINT NOT NULL COMMENT '用户类型：1-患者, 2-医生, 3-管理员',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用, 1-启用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_user_type` (`user_type`),
  INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB;

CREATE TABLE `department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '科室名称',
  `description` VARCHAR(255) COMMENT '描述',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父科室ID（支持多级）',
  `sort_order` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `doctor_schedule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `doctor_id` BIGINT NOT NULL COMMENT '关联 sys_user.id（user_type=2）',
  `department_id` BIGINT NOT NULL,
  `schedule_date` DATE NOT NULL COMMENT '排班日期',
  `time_slot` VARCHAR(20) NOT NULL COMMENT '时间段，如 "09:00-12:00"',
  `max_appointments` INT NOT NULL DEFAULT 20 COMMENT '最大可预约人数',
  `available_count` INT NOT NULL DEFAULT 20 COMMENT '剩余可约数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  --  创建索引列
  -- 唯一索引
  UNIQUE KEY `uk_doctor_date_slot` (`doctor_id`, `schedule_date`, `time_slot`),
  -- 普通索引
  INDEX `idx_dept_date` (`department_id`, `schedule_date`)
) ENGINE=InnoDB;