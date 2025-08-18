/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : x_springboot

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 18/08/2025 17:25:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu_new
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_new`;
CREATE TABLE `sys_menu_new`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int NULL DEFAULT NULL COMMENT '菜单父ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由重定向，有子集 children 时',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `is_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外链/内嵌时链接地址（http:xxx.com），开启外链条件，`1、isLink: 链接地址不为空`',
  `is_hide` int NULL DEFAULT NULL COMMENT '是否隐藏',
  `is_keep_alive` int NULL DEFAULT NULL COMMENT '是否缓存',
  `is_affix` int NULL DEFAULT NULL COMMENT '是否固定',
  `is_iframe` int NULL DEFAULT NULL COMMENT '是否内嵌，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `order_sort` int NULL DEFAULT NULL COMMENT '排序',
  `disabled` int NULL DEFAULT NULL COMMENT '是否显示：1显示，0不显示',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_new
-- ----------------------------
INSERT INTO `sys_menu_new` VALUES (1, 0, 'home', '/home', NULL, 'home/index', '首页', NULL, 0, 1, 1, 0, 'iconfont icon-shouye', 'admin,common', 1, 1);
INSERT INTO `sys_menu_new` VALUES (2, 0, 'system', '/system', '/system/menu', 'layout/routerView/parent', '系统管理', NULL, 0, 1, 0, 0, 'iconfont icon-xitongshezhi', 'admin', 2, 1);
INSERT INTO `sys_menu_new` VALUES (3, 0, 'logs', '/logs', NULL, 'layout/routerView/parent', '日志管理', NULL, 0, 1, 0, 0, 'el-icon-office-building', 'admin', 3, 1);
INSERT INTO `sys_menu_new` VALUES (4, 0, 'codeGen', '/codeGen', NULL, 'code/index', '代码生成', NULL, 0, 1, 0, 0, 'el-icon-set-up', 'admin', 98, 1);
INSERT INTO `sys_menu_new` VALUES (5, 0, 'swagger', '/swagger', NULL, 'layout/routerView/parent', '接口文档', 'http://localhost:8080/swagger-ui/index.html', 0, 1, 0, 1, 'el-icon-news', 'admin', 99, 1);
INSERT INTO `sys_menu_new` VALUES (20, 2, 'systemUser', '/systemUser', NULL, 'user/index', '用户管理', NULL, 0, 1, 0, 0, 'el-icon-user', 'admin', 1, 1);
INSERT INTO `sys_menu_new` VALUES (21, 2, 'systemRole', '/systemRole', NULL, 'role/index', '角色管理', NULL, 0, 1, 0, 0, 'el-icon-lock', 'admin', 2, 1);
INSERT INTO `sys_menu_new` VALUES (22, 2, 'systemMenu', '/systemMenu', NULL, 'menu/index', '菜单管理', NULL, 0, 1, 0, 0, 'el-icon-box', 'admin', 3, 1);
INSERT INTO `sys_menu_new` VALUES (23, 2, 'personal', '/personal', NULL, 'personal/index', '个人中心', NULL, 0, 1, 0, 0, 'iconfont icon-gerenzhongxin', 'admin,common', 4, 0);
INSERT INTO `sys_menu_new` VALUES (31, 3, 'optionLog', '/optionLog', NULL, 'optionLog/index', '操作日志', NULL, 0, 1, 0, 0, 'el-icon-tickets', 'admin', 1, 1);
INSERT INTO `sys_menu_new` VALUES (32, 3, 'loginLog', '/loginLog', NULL, 'loginLog/index', '登录日志', NULL, 0, 1, 0, 0, 'el-icon-tickets', 'admin', 2, 1);
INSERT INTO `sys_menu_new` VALUES (38, 2, 'apkVersion', '/apkVersion', NULL, 'apk/index', 'APK版本管理', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 99, 1);
INSERT INTO `sys_menu_new` VALUES (50, 2, 'syspermission', '/syspermission', NULL, 'permission/index', '接口权限管理', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 3, 1);
INSERT INTO `sys_menu_new` VALUES (51, 2, 'tenancy', '/tenancy', NULL, 'tenancy/defaultTenancyPage', '租户管理', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 3, 1);
INSERT INTO `sys_menu_new` VALUES (52, 0, 'application', '/application', NULL, 'application/index', '应用服务', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 4, 1);
INSERT INTO `sys_menu_new` VALUES (53, 0, 'msg', '/msg', '', 'layout/routerView/parent', '短信管理', NULL, 0, 1, 0, 0, 'iconfont icon-xitongshezhi', 'admin', 2, 1);
INSERT INTO `sys_menu_new` VALUES (54, 53, 'msgRecord', '/msgRecord', NULL, 'msg/record', '短信发送记录', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 4, 1);
INSERT INTO `sys_menu_new` VALUES (55, 53, 'msgService', '/msgService', NULL, 'msg/service', '短信服务', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 1, 1);
INSERT INTO `sys_menu_new` VALUES (56, 53, 'msgTemplate', '/msgTemplate', NULL, 'msg/template', '短信模板', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 2, 1);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '文件上传' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `permission_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限ID',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `english_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '英文名称',
  `url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'URL',
  `module_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `delete_status` int NULL DEFAULT NULL COMMENT '删除状态',
  `menu_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '接口权限管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('0bbb8872-2492-41aa-b5b4-7405a10ff9f7', '修改用户', 'SysUserUpdate', '/sys/user/update', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('0f87ddbb-bdb9-4de5-a3ea-1249555d4491', '选择角色列表', 'SysRoleSelect', '/sys/role/select', '角色管理', 0, '21');
INSERT INTO `sys_permission` VALUES ('19b139e8-9967-4807-9e02-655bd4d4f1d5', '登录日志列表', 'SysLogLoginList', '/sys/log/loginList', '系统日志', 0, '3');
INSERT INTO `sys_permission` VALUES ('1f467b08-8176-4a60-80c0-de3b3b957a00', '所有菜单列表', 'SysMenuList', '/sys/menu/list', '系统菜单', 0, '22');
INSERT INTO `sys_permission` VALUES ('2bb3acb9-8f7d-4aff-b8e1-1be4605dbe6d', '修改角色', 'SysRoleUpdate', '/sys/role/update', '角色管理', 0, '21');
INSERT INTO `sys_permission` VALUES ('782dd390-e50f-489e-9908-01893042ace0', '权限解除绑定菜单', 'SysPermissionUnBuildMenu', '/sys/permission/unBuildMenu', '接口权限管理', 0, '50');
INSERT INTO `sys_permission` VALUES ('7b9cac62-8183-4157-ad2d-911b1c2fa897', '修改菜单', 'SysMenuUpdate', '/sys/menu/update', '系统菜单', 0, '22');
INSERT INTO `sys_permission` VALUES ('7ca75856-d055-46f4-b57f-d7bd42021b79', '获取登录的用户信息和菜单信息', 'SysUserSysInfo', '/sys/user/sysInfo', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('81e6c308-8834-4b28-a0b5-58b9bbd0e773', '保存用户', 'SysUserSave', '/sys/user/save', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('90d4f699-c2b2-479f-bb36-b80fc70233f5', '删除角色', 'SysRoleDelete', '/sys/role/delete', '角色管理', 0, '21');
INSERT INTO `sys_permission` VALUES ('9fd5a003-9b77-423d-adba-a83f41558119', '删除菜单', 'SysMenuDelete', '/sys/menu/delete', '系统菜单', 0, '22');
INSERT INTO `sys_permission` VALUES ('aaf9161f-3512-45cd-b412-1e2437d46a30', '所有用户列表', 'SysUserList', '/sys/user/list', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('b47457ed-7628-4685-9007-365a09dcf268', '角色列表', 'SysRoleList', '/sys/role/list', '角色管理', 0, '21');
INSERT INTO `sys_permission` VALUES ('d5db2fe0-c445-44a1-8c4e-b9df8e5ba38b', '菜单权限树', 'SysPermissionMenuPermissionTree', '/sys/permission/menuPermissionTree', '接口权限管理', 0, '50');
INSERT INTO `sys_permission` VALUES ('d6bfc0b1-fc0d-4105-a77c-6a88b844dd13', '删除用户', 'SysUserDelete', '/sys/user/delete', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('d9360716-cc9b-44a4-b41d-8944088a5550', '修改密码', 'SysUserPassword', '/sys/user/password', '系统用户', 0, '20');
INSERT INTO `sys_permission` VALUES ('e735a0ff-5d36-40da-8835-da4179c34c2f', '系统日志列表', 'SysLogList', '/sys/log/list', '系统日志', 0, '3');
INSERT INTO `sys_permission` VALUES ('eb11747f-e0a0-4f64-baee-251c7d4ba101', '权限绑定菜单', 'SysPermissionBuildMenu', '/sys/permission/buildMenu', '接口权限管理', 0, '50');
INSERT INTO `sys_permission` VALUES ('f12fc4d2-9fc6-4586-86a1-c78119c3ceb7', '权限同步', 'SysPermissionPermissionSync', '/sys/permission/permissionSync', '接口权限管理', 0, '50');
INSERT INTO `sys_permission` VALUES ('f2af8456-2e6e-4603-9c66-e997b88b0e59', '保存角色', 'SysRoleSave', '/sys/role/save', '角色管理', 0, '21');
INSERT INTO `sys_permission` VALUES ('f7bd9025-4242-4076-b9de-0e52f83276c5', '接口权限管理列表', 'SysPermissionList', '/sys/permission/list', '接口权限管理', 0, '50');
INSERT INTO `sys_permission` VALUES ('fdd86d3d-49ad-4d3a-b78b-e9426944bb84', '保存菜单', 'SysMenuSave', '/sys/menu/save', '系统菜单', 0, '22');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (4, '管理员', '管理员', 1, '2019-04-18 10:12:05');
INSERT INTO `sys_role` VALUES (5, '测试', '测试', 0, '2019-12-26 16:51:37');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 95 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (88, 5, 1);
INSERT INTO `sys_role_menu` VALUES (89, 5, 2);
INSERT INTO `sys_role_menu` VALUES (90, 5, 20);
INSERT INTO `sys_role_menu` VALUES (91, 5, 21);
INSERT INTO `sys_role_menu` VALUES (92, 5, 22);
INSERT INTO `sys_role_menu` VALUES (93, 5, 38);
INSERT INTO `sys_role_menu` VALUES (94, 5, 50);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `permission_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限ID',
  `role_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`permission_id`, `role_id`) USING BTREE,
  INDEX `index_1`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '权限和角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('0f87ddbb-bdb9-4de5-a3ea-1249555d4491', '5');
INSERT INTO `sys_role_permission` VALUES ('1732095938647', '5');
INSERT INTO `sys_role_permission` VALUES ('1732095938651', '5');
INSERT INTO `sys_role_permission` VALUES ('1732095938658', '5');
INSERT INTO `sys_role_permission` VALUES ('1f467b08-8176-4a60-80c0-de3b3b957a00', '5');
INSERT INTO `sys_role_permission` VALUES ('2bb3acb9-8f7d-4aff-b8e1-1be4605dbe6d', '5');
INSERT INTO `sys_role_permission` VALUES ('782dd390-e50f-489e-9908-01893042ace0', '5');
INSERT INTO `sys_role_permission` VALUES ('7b9cac62-8183-4157-ad2d-911b1c2fa897', '5');
INSERT INTO `sys_role_permission` VALUES ('7ca75856-d055-46f4-b57f-d7bd42021b79', '5');
INSERT INTO `sys_role_permission` VALUES ('81e6c308-8834-4b28-a0b5-58b9bbd0e773', '5');
INSERT INTO `sys_role_permission` VALUES ('90d4f699-c2b2-479f-bb36-b80fc70233f5', '5');
INSERT INTO `sys_role_permission` VALUES ('9fd5a003-9b77-423d-adba-a83f41558119', '5');
INSERT INTO `sys_role_permission` VALUES ('aaf9161f-3512-45cd-b412-1e2437d46a30', '5');
INSERT INTO `sys_role_permission` VALUES ('b47457ed-7628-4685-9007-365a09dcf268', '5');
INSERT INTO `sys_role_permission` VALUES ('d5db2fe0-c445-44a1-8c4e-b9df8e5ba38b', '5');
INSERT INTO `sys_role_permission` VALUES ('d6bfc0b1-fc0d-4105-a77c-6a88b844dd13', '5');
INSERT INTO `sys_role_permission` VALUES ('d9360716-cc9b-44a4-b41d-8944088a5550', '5');
INSERT INTO `sys_role_permission` VALUES ('eb11747f-e0a0-4f64-baee-251c7d4ba101', '5');
INSERT INTO `sys_role_permission` VALUES ('f12fc4d2-9fc6-4586-86a1-c78119c3ceb7', '5');
INSERT INTO `sys_role_permission` VALUES ('f2af8456-2e6e-4603-9c66-e997b88b0e59', '5');
INSERT INTO `sys_role_permission` VALUES ('f7bd9025-4242-4076-b9de-0e52f83276c5', '5');
INSERT INTO `sys_role_permission` VALUES ('fdd86d3d-49ad-4d3a-b78b-e9426944bb84', '5');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `photo` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `tenancy_id` int NULL DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('0', 'admin', '$2a$10$RG5KOoicH3f3IH948VW3AOzhJKepSteupeuQ8JAB28ElsYH3KlU4a', 'yzcheng90@qq.com', '13612345678', 1, '1', '2016-11-11 11:11:11', NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES ('53e3215ed12b227b59b6b3b9e9efb984', 'test', '$2a$10$yhqIeY5RhmPJxUsCnRGpBu7gDEBF.dwx2YFK4tbCCtmBndlBQSIG2', 'yzcheng90@qq.com', '13888888888', 1, '0', '2023-01-29 14:31:16', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (4, '53e3215ed12b227b59b6b3b9e9efb984', 5);

-- ----------------------------
-- Table structure for tb_apk_version
-- ----------------------------
DROP TABLE IF EXISTS `tb_apk_version`;
CREATE TABLE `tb_apk_version`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `update_content` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新内容',
  `version_code` int NULL DEFAULT NULL COMMENT '版本码',
  `version_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `package_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '包名',
  `download_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `app_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'APP名',
  `md5_value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'MD5值',
  `file_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_size` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '上传人',
  `is_force` tinyint NULL DEFAULT NULL COMMENT '是否强制安装',
  `is_ignorable` tinyint NULL DEFAULT NULL COMMENT '是否可忽略该版本',
  `is_silent` tinyint NULL DEFAULT NULL COMMENT '是否静默下载',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'APK版本管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_apk_version
-- ----------------------------
INSERT INTO `tb_apk_version` VALUES (3, '新增v1.0.0版本', 445, '4.4.5', 'com.sup.android.superb', 'https://cdn.wowud1.cc/task/2023-01-27/ef79def81fca4249a2e3c065c9d8c93f.apk', '皮皮虾', 'aef5b96315f0eee773f8838738605a32', 'aef5b96315f0eee773f8838738605a32.apk', '51870114', '2023-01-27 17:00:35', '2023-01-27 17:00:35', '0', 0, 1, 0);

-- ----------------------------
-- Table structure for tb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log`  (
  `log_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录日志ID',
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `option_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作',
  `option_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `option_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作IP',
  `option_terminal` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作终端',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '登录日志管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_platform_tenancy
-- ----------------------------
DROP TABLE IF EXISTS `tb_platform_tenancy`;
CREATE TABLE `tb_platform_tenancy`  (
  `tenancy_id` int NOT NULL AUTO_INCREMENT COMMENT '渠道ID',
  `tenancy_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
  `tenancy_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道备注',
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '折扣',
  `ex` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告位配置',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `tenancy_pid` bigint NULL DEFAULT NULL COMMENT '父级租户ID',
  `is_default` int NULL DEFAULT NULL COMMENT '是否默认租户（只能设置一个）',
  `is_delete` int NULL DEFAULT 0 COMMENT '是否删除(0否 1 是)',
  `update_data` int NULL DEFAULT 0 COMMENT '是否更新数据（1：是，0：否）',
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`tenancy_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '渠道管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_platform_tenancy
-- ----------------------------
INSERT INTO `tb_platform_tenancy` VALUES (1, '1', '1', NULL, NULL, '2025-04-19 14:56:18', NULL, 0, 1, 0, NULL);
INSERT INTO `tb_platform_tenancy` VALUES (2, '皮皮渠道', '皮皮渠道', NULL, NULL, '2025-08-15 17:54:26', NULL, 0, 0, 0, '0');

-- ----------------------------
-- Table structure for x_application
-- ----------------------------
DROP TABLE IF EXISTS `x_application`;
CREATE TABLE `x_application`  (
  `app_id` int NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `app_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `app_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'AppKey',
  `app_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'AppSecret',
  `is_enable` int NULL DEFAULT NULL COMMENT '是否可用（1可用，0不可用）',
  `tenancy_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用服务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_application
-- ----------------------------
INSERT INTO `x_application` VALUES (1, '皮皮虾', '6f7d3d8e5ec4af3bfb29fad6219fee99', '237271194510e1fea9b1765de4c4fcca', 1, '2');

-- ----------------------------
-- Table structure for x_message_service
-- ----------------------------
DROP TABLE IF EXISTS `x_message_service`;
CREATE TABLE `x_message_service`  (
  `service_id` int NOT NULL AUTO_INCREMENT COMMENT '服务ID',
  `service_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `service_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口URL',
  `product_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `app_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'AppKey',
  `app_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'AppSecret',
  `app_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'AppCode',
  `default_template` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认模板',
  `is_enable` int NULL DEFAULT NULL COMMENT '是否使用（1使用，0未使用）',
  `service_class` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务实现类地址',
  `tenancy_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  `app_id` int NULL DEFAULT NULL COMMENT '应用ID',
  `is_default` int NULL DEFAULT NULL COMMENT '同租户下只能有一个默认服务（1是，0否）',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信服务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_message_service
-- ----------------------------
INSERT INTO `x_message_service` VALUES (1, '阿里云New', 'https://cxkjsms.market.alicloudapi.com/chuangxinsms/dxjk', '', 'xxxx', 'xxxx', 'xxxx', '【皮皮虾】您的验证码为：#code，有效期5分钟，请确保是本人操作，不要把验证码泄露给其他人。', 1, 'com.suke.czx.modules.msg.component.AliyunNewSendMessageComponent', '2', 1, 1);

-- ----------------------------
-- Table structure for x_message_service_send_record
-- ----------------------------
DROP TABLE IF EXISTS `x_message_service_send_record`;
CREATE TABLE `x_message_service_send_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_year` int NULL DEFAULT NULL COMMENT '年',
  `record_month` int NULL DEFAULT NULL COMMENT '月',
  `record_day` int NULL DEFAULT NULL COMMENT '日',
  `send_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `send_status` int NULL DEFAULT NULL COMMENT '发送状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `tenancy_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  `send_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送类型',
  `send_source` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送来源',
  `service_id` int NULL DEFAULT NULL COMMENT '服务ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID',
  `app_id` int NULL DEFAULT NULL COMMENT '应用ID',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信发送记录（按月分表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for x_message_service_send_record_2025_08
-- ----------------------------
DROP TABLE IF EXISTS `x_message_service_send_record_2025_08`;
CREATE TABLE `x_message_service_send_record_2025_08`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_year` int NULL DEFAULT NULL COMMENT '年',
  `record_month` int NULL DEFAULT NULL COMMENT '月',
  `record_day` int NULL DEFAULT NULL COMMENT '日',
  `send_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `send_status` int NULL DEFAULT NULL COMMENT '发送状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `tenancy_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  `send_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送类型',
  `send_source` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送来源',
  `service_id` int NULL DEFAULT NULL COMMENT '服务ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID',
  `app_id` int NULL DEFAULT NULL COMMENT '应用ID',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信发送记录（按月分表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_message_service_send_record_2025_08
-- ----------------------------

-- ----------------------------
-- Table structure for x_message_service_template
-- ----------------------------
DROP TABLE IF EXISTS `x_message_service_template`;
CREATE TABLE `x_message_service_template`  (
  `template_id` int NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板类型（预留，比如验证码，营销）',
  `template_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `is_enable` int NULL DEFAULT NULL COMMENT '是否启用，1启动，0禁用',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenancy_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_message_service_template
-- ----------------------------
INSERT INTO `x_message_service_template` VALUES (2, '验证码', '【皮皮虾】您的验证码是：#code，有效期5分钟，请勿泄露于他人！', 1, '登录注册使用', '2');

-- ----------------------------
-- Table structure for x_message_service_template_config
-- ----------------------------
DROP TABLE IF EXISTS `x_message_service_template_config`;
CREATE TABLE `x_message_service_template_config`  (
  `service_id` int NOT NULL COMMENT '服务ID',
  `template_id` int NOT NULL COMMENT '模板ID',
  PRIMARY KEY (`service_id`, `template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信模板配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_message_service_template_config
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
