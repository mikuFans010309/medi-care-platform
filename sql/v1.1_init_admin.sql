-- 创建初始管理员账号
INSERT INTO `sys_user` (
    `username`, `password`, `real_name`, `phone`, `user_type`, `status`
) VALUES (
             'admin',
             ' $ 2a $ 10 $ DfYxZqJvJ7KQvW6bH6x7eO8u1X9Y0Z1a2B3c4D5e6F7g8H9i0J', -- BCrypt("admin123")
             '系统管理员',
             '13800000000',
             3,
             1
         );