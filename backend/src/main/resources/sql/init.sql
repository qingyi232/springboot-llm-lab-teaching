-- 高校实验教学支持系统 数据库初始化脚本
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS lab_teaching DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lab_teaching;

-- 用户表（管理员登录用）
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `image` varchar(500) DEFAULT NULL COMMENT '头像',
  `role` varchar(100) DEFAULT '管理员' COMMENT '角色',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Token表
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '令牌',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token表';

-- 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `xuehao` varchar(200) NOT NULL COMMENT '学号',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `xingming` varchar(200) DEFAULT NULL COMMENT '姓名',
  `touxiang` varchar(500) DEFAULT NULL COMMENT '头像',
  `xingbie` varchar(10) DEFAULT NULL COMMENT '性别',
  `nianling` int DEFAULT NULL COMMENT '年龄',
  `zhuanye` varchar(200) DEFAULT NULL COMMENT '专业',
  `banji` varchar(200) DEFAULT NULL COMMENT '班级',
  `shouji` varchar(20) DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(100) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_xuehao` (`xuehao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 教师表
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gonghao` varchar(200) NOT NULL COMMENT '工号',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '教师姓名',
  `touxiang` varchar(500) DEFAULT NULL COMMENT '头像',
  `xingbie` varchar(10) DEFAULT NULL COMMENT '性别',
  `nianling` int DEFAULT NULL COMMENT '年龄',
  `zhicheng` varchar(200) DEFAULT NULL COMMENT '职称',
  `shouji` varchar(20) DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(100) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gonghao` (`gonghao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- 实验室管理员表
DROP TABLE IF EXISTS `lab_admin`;
CREATE TABLE `lab_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gonghao` varchar(200) NOT NULL COMMENT '工号',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `xingming` varchar(200) DEFAULT NULL COMMENT '姓名',
  `touxiang` varchar(500) DEFAULT NULL COMMENT '头像',
  `xingbie` varchar(10) DEFAULT NULL COMMENT '性别',
  `shouji` varchar(20) DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `guanlishipanshi` varchar(500) DEFAULT NULL COMMENT '管理实验室',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gonghao` (`gonghao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室管理员表';

-- 实验室类型表
DROP TABLE IF EXISTS `lab_type`;
CREATE TABLE `lab_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `typename` varchar(200) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室类型表';

-- 实验室信息表
DROP TABLE IF EXISTS `lab_info`;
CREATE TABLE `lab_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lab_id` varchar(200) NOT NULL COMMENT '实验室编号',
  `lab_name` varchar(200) DEFAULT NULL COMMENT '实验室名称',
  `shiyanshileixing` varchar(200) DEFAULT NULL COMMENT '实验室类型',
  `tupian` varchar(500) DEFAULT NULL COMMENT '图片',
  `weizhi` varchar(200) DEFAULT NULL COMMENT '位置',
  `rongna` int DEFAULT 0 COMMENT '容纳人数',
  `zhuangtai` varchar(50) DEFAULT '空闲' COMMENT '状态',
  `kaifangshijian` varchar(200) DEFAULT NULL COMMENT '开放时间',
  `jianjie` text COMMENT '简介',
  `guanliyuan` varchar(200) DEFAULT NULL COMMENT '管理员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_lab_id` (`lab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室信息表';

-- 预约申请表
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lab_id` varchar(200) DEFAULT NULL COMMENT '实验室编号',
  `lab_name` varchar(200) DEFAULT NULL COMMENT '实验室名称',
  `shiyanshileixing` varchar(200) DEFAULT NULL COMMENT '实验室类型',
  `tupian` varchar(500) DEFAULT NULL COMMENT '图片',
  `weizhi` varchar(200) DEFAULT NULL COMMENT '位置',
  `yuyueshuoming` text COMMENT '预约说明',
  `yuyuerenshu` int DEFAULT 0 COMMENT '预约人数',
  `yuyueshijian` datetime DEFAULT NULL COMMENT '预约开始时间',
  `yuyuejieshu` datetime DEFAULT NULL COMMENT '预约结束时间',
  `xingming` varchar(200) DEFAULT NULL COMMENT '预约人姓名',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `zhuanye` varchar(200) DEFAULT NULL COMMENT '专业',
  `banji` varchar(200) DEFAULT NULL COMMENT '班级',
  `zhuangtai` varchar(50) DEFAULT '待审核' COMMENT '状态',
  `sfsh` varchar(50) DEFAULT '待审核' COMMENT '是否审核',
  `shhf` text COMMENT '审核回复',
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约申请表';

-- 使用记录表
DROP TABLE IF EXISTS `usage_record`;
CREATE TABLE `usage_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lab_id` varchar(200) DEFAULT NULL COMMENT '实验室编号',
  `lab_name` varchar(200) DEFAULT NULL COMMENT '实验室名称',
  `shiyanshileixing` varchar(200) DEFAULT NULL COMMENT '实验室类型',
  `shiyongrenshu` int DEFAULT 0 COMMENT '使用人数',
  `shiyongjilu` text COMMENT '使用记录',
  `shiyongshijian` datetime DEFAULT NULL COMMENT '使用时间',
  `xingming` varchar(200) DEFAULT NULL COMMENT '姓名',
  `zhuanye` varchar(200) DEFAULT NULL COMMENT '专业',
  `banji` varchar(200) DEFAULT NULL COMMENT '班级',
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用记录表';

-- 课程类型表
DROP TABLE IF EXISTS `course_type`;
CREATE TABLE `course_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `typename` varchar(200) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程类型表';

-- 课程资料表
DROP TABLE IF EXISTS `course_material`;
CREATE TABLE `course_material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(200) NOT NULL COMMENT '标题',
  `coursetype` varchar(200) DEFAULT NULL COMMENT '课程类型',
  `content` text COMMENT '内容',
  `fujian` varchar(500) DEFAULT NULL COMMENT '附件',
  `tupian` varchar(500) DEFAULT NULL COMMENT '图片',
  `faburenxingming` varchar(200) DEFAULT NULL COMMENT '发布人姓名',
  `faburenid` bigint DEFAULT NULL COMMENT '发布人ID',
  `clicknum` int DEFAULT 0 COMMENT '点击次数',
  `storecount` int DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程资料表';

-- 学习视频表
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(200) NOT NULL COMMENT '标题',
  `coursetype` varchar(200) DEFAULT NULL COMMENT '课程类型',
  `videourl` varchar(500) DEFAULT NULL COMMENT '视频地址',
  `tupian` varchar(500) DEFAULT NULL COMMENT '封面图',
  `content` text COMMENT '简介',
  `faburenxingming` varchar(200) DEFAULT NULL COMMENT '发布人',
  `faburenid` bigint DEFAULT NULL COMMENT '发布人ID',
  `clicknum` int DEFAULT 0 COMMENT '点击次数',
  `thumbsup` int DEFAULT 0 COMMENT '点赞数',
  `crazynum` int DEFAULT 0 COMMENT '踩数',
  `storecount` int DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习视频表';

-- 作业信息表
DROP TABLE IF EXISTS `homework`;
CREATE TABLE `homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(200) NOT NULL COMMENT '标题',
  `coursetype` varchar(200) DEFAULT NULL COMMENT '课程类型',
  `content` text COMMENT '内容',
  `fujian` varchar(500) DEFAULT NULL COMMENT '附件',
  `jiezhishijian` datetime DEFAULT NULL COMMENT '截止时间',
  `faburenxingming` varchar(200) DEFAULT NULL COMMENT '发布人',
  `faburenid` bigint DEFAULT NULL COMMENT '发布人ID',
  `banji` varchar(200) DEFAULT NULL COMMENT '班级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业信息表';

-- 作业提交表
DROP TABLE IF EXISTS `homework_submit`;
CREATE TABLE `homework_submit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `homework_id` bigint DEFAULT NULL COMMENT '作业ID',
  `homework_title` varchar(200) DEFAULT NULL COMMENT '作业标题',
  `content` text COMMENT '提交内容',
  `fujian` varchar(500) DEFAULT NULL COMMENT '附件',
  `xingming` varchar(200) DEFAULT NULL COMMENT '学生姓名',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  `zhuangtai` varchar(50) DEFAULT '已提交' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交表';

-- 作业成绩表
DROP TABLE IF EXISTS `homework_score`;
CREATE TABLE `homework_score` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `submit_id` bigint DEFAULT NULL COMMENT '提交ID',
  `homework_id` bigint DEFAULT NULL COMMENT '作业ID',
  `homework_title` varchar(200) DEFAULT NULL COMMENT '作业标题',
  `xingming` varchar(200) DEFAULT NULL COMMENT '学生姓名',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `score` int DEFAULT NULL COMMENT '成绩',
  `pingyu_content` text COMMENT '评语',
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业成绩表';

-- 论坛交流表
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `parentid` varchar(50) DEFAULT '0' COMMENT '父级ID',
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `avatarurl` varchar(500) DEFAULT NULL COMMENT '头像',
  `isdone` int DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛交流表';

-- 公告信息表
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(200) NOT NULL COMMENT '标题',
  `introduction` text COMMENT '简介',
  `typename` varchar(200) DEFAULT NULL COMMENT '分类',
  `name` varchar(200) DEFAULT NULL COMMENT '发布人',
  `headportrait` varchar(500) DEFAULT NULL COMMENT '头像',
  `clicknum` int DEFAULT 0 COMMENT '点击次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告信息表';

-- 收藏表
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  `refid` bigint DEFAULT NULL COMMENT '关联ID',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `picture` varchar(500) DEFAULT NULL COMMENT '图片',
  `type` varchar(100) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ============== 示例数据 ==============

-- 管理员账号
INSERT INTO `users` (`username`, `password`, `role`, `nickname`, `phone`, `email`, `image`) VALUES
('admin', '123456', '管理员', '系统管理员', '13800000001', 'admin@lab.edu.cn', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin');

-- 实验室类型
INSERT INTO `lab_type` (`typename`) VALUES
('计算机实验室'), ('物理实验室'), ('化学实验室'), ('电子信息实验室'), ('网络工程实验室'), ('人工智能实验室');

-- 课程类型
INSERT INTO `course_type` (`typename`) VALUES
('计算机基础'), ('数据结构'), ('操作系统'), ('计算机网络'), ('数据库原理'), ('人工智能导论'), ('软件工程'), ('Web前端开发');

-- 实验室信息
INSERT INTO `lab_info` (`lab_id`, `lab_name`, `shiyanshileixing`, `tupian`, `weizhi`, `rongna`, `zhuangtai`, `kaifangshijian`, `jianjie`, `guanliyuan`) VALUES
('LAB001', '计算机基础实验室A', '计算机实验室', 'https://images.unsplash.com/photo-1580894894513-541e068a3e2b?w=400', '实验楼A座301', 60, '空闲', '周一至周五 8:00-21:00', '配备60台高性能计算机，支持各类编程语言和开发环境的教学实验', '张管理'),
('LAB002', '计算机网络实验室', '网络工程实验室', 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=400', '实验楼A座302', 40, '空闲', '周一至周五 8:00-21:00', '配备Cisco路由器、交换机等网络设备，支持网络工程相关课程实验', '张管理'),
('LAB003', '物理综合实验室', '物理实验室', 'https://images.unsplash.com/photo-1532094349884-543bc11b234d?w=400', '实验楼B座201', 50, '空闲', '周一至周五 8:00-18:00', '配备光学、电学、力学等物理实验设备，支持大学物理课程实验', '李管理'),
('LAB004', '人工智能实验室', '人工智能实验室', 'https://images.unsplash.com/photo-1677442136019-21780ecad995?w=400', '实验楼C座401', 30, '空闲', '周一至周六 8:00-22:00', '配备GPU工作站和深度学习服务器，支持AI相关课程实验和科研', '张管理'),
('LAB005', '电子信息实验室', '电子信息实验室', 'https://images.unsplash.com/photo-1581092160562-40aa08e78837?w=400', '实验楼B座301', 45, '使用中', '周一至周五 8:00-20:00', '配备示波器、信号发生器、电路板等设备，支持电子信息类课程实验', '张管理'),
('LAB006', '化学分析实验室', '化学实验室', 'https://images.unsplash.com/photo-1582719471384-894fbb16e074?w=400', '实验楼D座101', 35, '空闲', '周一至周五 8:00-17:30', '配备分析天平、分光光度计等化学分析设备，支持化学类课程实验', '张管理');

-- 实验室管理员
INSERT INTO `lab_admin` (`gonghao`, `mima`, `xingming`, `touxiang`, `xingbie`, `shouji`, `youxiang`, `guanlishipanshi`) VALUES
('G2024001', '123456', '张管理', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhanggl', '男', '13800000010', 'zhanggl@lab.edu.cn', '计算机基础实验室A,计算机网络实验室,人工智能实验室,电子信息实验室,化学分析实验室'),
('G2024002', '123456', '李管理', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ligl', '女', '13800000011', 'ligl@lab.edu.cn', '物理综合实验室'),
('G2024003', '123456', '王管理', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wanggl', '男', '13800000012', 'wanggl@lab.edu.cn', '');

-- 教师
INSERT INTO `teacher` (`gonghao`, `mima`, `jiaoshixingming`, `touxiang`, `xingbie`, `nianling`, `zhicheng`, `shouji`, `youxiang`) VALUES
('T2024001', '123456', '王教授', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangjs', '男', 45, '教授', '13800000002', 'wangjs@lab.edu.cn'),
('T2024002', '123456', '李副教授', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lifjs', '女', 38, '副教授', '13800000003', 'lifjs@lab.edu.cn'),
('T2024003', '123456', '赵讲师', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaojs', '男', 32, '讲师', '13800000004', 'zhaojs@lab.edu.cn');

-- 学生
INSERT INTO `student` (`xuehao`, `mima`, `xingming`, `touxiang`, `xingbie`, `nianling`, `zhuanye`, `banji`, `shouji`, `youxiang`) VALUES
('2021001', '123456', '张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan', '男', 21, '计算机科学与技术', '计科2101班', '13800000005', 'zhangsan@stu.edu.cn'),
('2021002', '123456', '李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi', '女', 20, '软件工程', '软工2101班', '13800000006', 'lisi@stu.edu.cn'),
('2021003', '123456', '王五', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu', '男', 22, '网络工程', '网工2101班', '13800000007', 'wangwu@stu.edu.cn'),
('2021004', '123456', '赵六', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu', '女', 21, '人工智能', 'AI2101班', '13800000008', 'zhaoliu@stu.edu.cn'),
('2021005', '123456', '孙七', 'https://api.dicebear.com/7.x/avataaars/svg?seed=sunqi', '男', 20, '计算机科学与技术', '计科2102班', '13800000009', 'sunqi@stu.edu.cn');

-- 课程资料
INSERT INTO `course_material` (`title`, `coursetype`, `content`, `tupian`, `faburenxingming`, `faburenid`, `clicknum`, `storecount`) VALUES
('数据结构与算法导论', '数据结构', '## 课程概述\n本课程资料涵盖线性表、栈、队列、树、图等基本数据结构的定义与实现，以及常用排序与查找算法的分析。\n\n## 教学目标\n- 掌握常用数据结构的定义、存储结构及基本操作\n- 理解各类排序与查找算法的设计思想与时间复杂度\n- 能够针对实际问题选择合适的数据结构与算法\n\n## 知识要点\n1. **线性表**：顺序表与链表的实现、单链表/双向链表/循环链表\n2. **栈与队列**：栈的应用（表达式求值、递归）、队列的应用（BFS）\n3. **树与二叉树**：二叉搜索树、AVL树、哈夫曼树与哈夫曼编码\n4. **图**：邻接矩阵与邻接表、DFS与BFS、最短路径算法（Dijkstra）\n5. **排序算法**：冒泡排序、快速排序、归并排序、堆排序的比较\n6. **查找算法**：顺序查找、折半查找、哈希查找\n\n## 参考教材\n- 严蔚敏《数据结构（C语言版）》清华大学出版社\n- 邓俊辉《数据结构（C++语言版）》清华大学出版社\n- Thomas H. Cormen《算法导论》机械工业出版社', 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=400', '王教授', 1, 156, 23),
('操作系统原理讲义', '操作系统', '## 课程概述\n详细介绍进程管理、内存管理、文件系统和I/O管理等操作系统核心概念及实现原理。\n\n## 教学目标\n- 理解操作系统的基本概念、功能和设计原则\n- 掌握进程管理、内存管理、文件系统的核心机制\n- 能够分析和解决操作系统相关的实际问题\n\n## 知识要点\n1. **操作系统概论**：OS的发展历程、类型与功能\n2. **进程管理**：进程与线程、进程调度算法（FCFS、SJF、RR、优先级）\n3. **进程同步**：信号量、管程、经典同步问题（生产者消费者、读者写者、哲学家就餐）\n4. **死锁处理**：死锁条件、预防、避免（银行家算法）、检测与恢复\n5. **内存管理**：连续分配、分页、分段、虚拟内存与页面置换算法\n6. **文件系统**：文件组织、目录结构、磁盘调度算法\n7. **I/O管理**：I/O控制方式、缓冲技术、SPOOLing\n\n## 参考教材\n- 汤小丹《计算机操作系统》西安电子科技大学出版社\n- Abraham Silberschatz《操作系统概念》机械工业出版社\n- Andrew S. Tanenbaum《现代操作系统》机械工业出版社', 'https://images.unsplash.com/photo-1629654297299-c8506221ca97?w=400', '李副教授', 2, 132, 18),
('计算机网络基础教程', '计算机网络', '## 课程概述\n系统讲解TCP/IP协议栈、网络层次模型、路由算法和网络安全的基本知识。\n\n## 教学目标\n- 理解计算机网络的体系结构和各层协议\n- 掌握TCP/IP协议族的核心工作原理\n- 能够进行基本的网络编程和网络故障排查\n\n## 知识要点\n1. **网络体系结构**：OSI七层模型与TCP/IP四层模型对比\n2. **物理层与数据链路层**：编码方式、差错控制、MAC协议、以太网\n3. **网络层**：IP协议、子网划分、路由算法（RIP、OSPF）、ICMP\n4. **传输层**：TCP与UDP对比、TCP三次握手与四次挥手、拥塞控制\n5. **应用层**：HTTP/HTTPS、DNS、FTP、SMTP/POP3协议\n6. **网络安全**：对称加密与非对称加密、数字签名、SSL/TLS\n\n## 实验项目\n- 使用Wireshark抓包分析HTTP/TCP协议\n- Socket编程实现客户端-服务器通信\n- 路由器配置与VLAN划分\n\n## 参考教材\n- 谢希仁《计算机网络》电子工业出版社\n- James F. Kurose《计算机网络：自顶向下方法》机械工业出版社', 'https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=400', '赵讲师', 3, 98, 15),
('MySQL数据库实战指南', '数据库原理', '## 课程概述\n从MySQL安装配置到SQL语法、索引优化和事务处理，全面讲解数据库实战技巧。\n\n## 教学目标\n- 掌握关系数据库的基本理论与设计方法\n- 熟练使用SQL语言进行数据定义、查询和操作\n- 理解事务处理、并发控制和数据库优化技术\n\n## 知识要点\n1. **数据库概论**：数据库系统概述、ER模型、关系代数\n2. **SQL基础**：DDL（CREATE/ALTER/DROP）、DML（INSERT/UPDATE/DELETE）、DQL（SELECT）\n3. **高级查询**：多表联接（INNER/LEFT/RIGHT JOIN）、子查询、聚合函数与GROUP BY\n4. **索引优化**：B+树索引原理、索引设计策略、EXPLAIN执行计划分析\n5. **事务与并发**：ACID特性、隔离级别、锁机制、MVCC\n6. **数据库设计**：三大范式、反范式化、ER图到关系模式的转换\n7. **存储过程与触发器**：编写存储过程、函数和触发器\n\n## 实验项目\n- MySQL安装与基本操作\n- 学生选课系统数据库设计与实现\n- SQL查询优化与索引分析\n\n## 参考教材\n- 王珊《数据库系统概论》高等教育出版社\n- Abraham Silberschatz《数据库系统概念》机械工业出版社', 'https://images.unsplash.com/photo-1489875347897-49f64b51c1f8?w=400', '王教授', 1, 210, 35),
('Python人工智能入门', '人工智能导论', '## 课程概述\n使用Python语言介绍机器学习、深度学习的基础概念和实践案例。\n\n## 教学目标\n- 了解人工智能的发展历程与主要技术分支\n- 掌握机器学习基本算法的原理与Python实现\n- 能够使用深度学习框架完成实际项目\n\n## 知识要点\n1. **AI概论**：人工智能发展简史、应用领域、发展趋势\n2. **Python基础**：NumPy、Pandas、Matplotlib数据处理与可视化\n3. **机器学习基础**：线性回归、逻辑回归、决策树、SVM、KNN\n4. **集成学习**：随机森林、XGBoost、Bagging与Boosting\n5. **神经网络**：感知机、BP算法、激活函数、梯度下降优化\n6. **深度学习**：CNN（卷积神经网络）、RNN（循环神经网络）、Transformer\n7. **实战项目**：手写数字识别、文本分类、图像风格迁移\n\n## 开发环境\n- Python 3.8+、Jupyter Notebook\n- scikit-learn、PyTorch/TensorFlow\n\n## 参考教材\n- 周志华《机器学习》清华大学出版社\n- Ian Goodfellow《深度学习》人民邮电出版社\n- 李航《统计学习方法》清华大学出版社', 'https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=400', '李副教授', 2, 189, 28),
('Web前端开发技术', 'Web前端开发', '## 课程概述\n涵盖HTML5、CSS3、JavaScript、Vue.js等前端技术的基础知识与项目实践。\n\n## 教学目标\n- 掌握Web前端开发的核心技术栈\n- 能够使用Vue.js框架开发单页应用\n- 理解前后端分离的开发模式和工程化实践\n\n## 知识要点\n1. **HTML5**：语义化标签、表单增强、Canvas绘图、音视频API\n2. **CSS3**：Flexbox与Grid布局、动画与过渡、响应式设计、媒体查询\n3. **JavaScript核心**：ES6+语法、DOM操作、事件机制、异步编程（Promise/async-await）\n4. **Vue.js框架**：组件化开发、响应式原理、Vue Router、Pinia状态管理\n5. **工程化工具**：Vite构建工具、ESLint代码规范、Git版本控制\n6. **前后端交互**：Axios请求封装、RESTful API设计、跨域处理\n7. **UI组件库**：Element Plus常用组件与自定义主题\n\n## 实验项目\n- 个人博客系统前端开发\n- 电商网站商品列表与购物车\n- 基于Vue3的后台管理系统\n\n## 参考资源\n- MDN Web Docs (developer.mozilla.org)\n- Vue.js官方文档 (vuejs.org)\n- 阮一峰《ES6标准入门》', 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400', '赵讲师', 3, 175, 22);

-- 学习视频
INSERT INTO `video` (`title`, `coursetype`, `videourl`, `tupian`, `content`, `faburenxingming`, `faburenid`, `clicknum`, `thumbsup`, `crazynum`, `storecount`) VALUES
('数据结构-链表详解', '数据结构', 'https://player.bilibili.com/player.html?bvid=BV1b7411N798&page=3&autoplay=0', 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400', '深入讲解单链表、双向链表和循环链表的概念、实现和应用场景', '王教授', 1, 320, 45, 2, 38),
('操作系统-进程调度算法', '操作系统', 'https://player.bilibili.com/player.html?bvid=BV1YE411D7nH&page=5&autoplay=0', 'https://images.unsplash.com/photo-1518432031352-d6fc5c10da5a?w=400', '详解FCFS、SJF、RR等经典进程调度算法的原理和实现', '李副教授', 2, 256, 38, 1, 25),
('计算机网络-TCP三次握手', '计算机网络', 'https://player.bilibili.com/player.html?bvid=BV1c4411d7jb&page=18&autoplay=0', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400', '通过动画演示深入理解TCP三次握手与四次挥手的完整过程', '赵讲师', 3, 412, 67, 3, 52),
('SQL语言从入门到精通', '数据库原理', 'https://player.bilibili.com/player.html?bvid=BV1Kr4y1i7ru&page=1&autoplay=0', 'https://images.unsplash.com/photo-1504868584819-f8e8b4b6d7e3?w=400', '从基础SELECT查询到高级联表操作和存储过程的全面教程', '王教授', 1, 385, 56, 2, 41),
('深度学习实战-图像分类', '人工智能导论', 'https://player.bilibili.com/player.html?bvid=BV1FT4y1E74V&page=8&autoplay=0', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400', '使用PyTorch实现CNN图像分类模型的完整流程演示', '李副教授', 2, 298, 42, 1, 33);

-- 作业信息
INSERT INTO `homework` (`title`, `coursetype`, `content`, `jiezhishijian`, `faburenxingming`, `faburenid`) VALUES
('数据结构实验1-链表操作', '数据结构', '实现单链表的创建、插入、删除和查找操作，并编写测试程序验证各功能的正确性。要求使用C语言或Java实现。', '2026-05-01 23:59:59', '王教授', 1),
('操作系统实验2-进程同步', '操作系统', '使用信号量机制解决生产者-消费者问题，要求实现多线程版本，并提交实验报告。', '2026-05-05 23:59:59', '李副教授', 2),
('计算机网络实验3-Socket编程', '计算机网络', '使用Socket编程实现一个简单的客户端-服务器通信程序，支持多客户端同时连接。', '2026-05-10 23:59:59', '赵讲师', 3),
('数据库实验4-SQL查询', '数据库原理', '根据给定的数据库表结构，完成10道SQL查询题目，涵盖单表查询、多表联接和子查询。', '2026-05-15 23:59:59', '王教授', 1);

-- 作业提交
INSERT INTO `homework_submit` (`homework_id`, `homework_title`, `content`, `xingming`, `xuehao`, `userid`, `zhuangtai`) VALUES
(1, '数据结构实验1-链表操作', '已完成单链表的所有操作实现，包含完整测试用例。', '张三', '2021001', 1, '已批改'),
(1, '数据结构实验1-链表操作', '链表操作已全部实现，代码运行通过所有测试。', '李四', '2021002', 2, '已批改'),
(1, '数据结构实验1-链表操作', '完成了基本操作，还有部分边界情况需要处理。', '王五', '2021003', 3, '已提交'),
(2, '操作系统实验2-进程同步', '使用Java多线程实现了生产者-消费者问题。', '张三', '2021001', 1, '已提交');

-- 作业成绩
INSERT INTO `homework_score` (`submit_id`, `homework_id`, `homework_title`, `xingming`, `xuehao`, `score`, `pingyu_content`, `userid`) VALUES
(1, 1, '数据结构实验1-链表操作', '张三', '2021001', 92, '代码结构清晰，测试用例完善，链表操作实现正确。建议可以增加异常处理。', 1),
(2, 1, '数据结构实验1-链表操作', '李四', '2021002', 88, '实现基本正确，代码风格良好。但删除操作的边界情况处理还可以改进。', 2);

-- 论坛交流
INSERT INTO `forum` (`title`, `content`, `parentid`, `userid`, `username`, `avatarurl`, `isdone`) VALUES
('关于数据结构课程的学习心得', '最近在学习链表和二叉树，感觉递归的思想很重要，大家有什么学习建议吗？', '0', 1, '张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan', 0),
('操作系统实验遇到的问题', '在做进程同步实验时遇到死锁问题，请问有没有同学遇到类似的情况？', '0', 2, '李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi', 0),
('推荐一些计算机网络的学习资源', '发现一个很好的网络协议可视化网站，可以动态展示TCP/IP各层协议的工作过程。', '0', 3, '赵讲师', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaojs', 0);

-- 论坛回复
INSERT INTO `forum` (`title`, `content`, `parentid`, `userid`, `username`, `avatarurl`, `isdone`) VALUES
('', '建议先打好递归的基础，可以从简单的斐波那契数列开始练习。', '1', 4, '赵六', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu', 0),
('', '递归确实很重要，推荐看《算法图解》这本书，讲得很通俗易懂。', '1', 5, '孙七', 'https://api.dicebear.com/7.x/avataaars/svg?seed=sunqi', 0),
('', '死锁通常是因为资源获取顺序不一致导致的，可以尝试按固定顺序获取锁。', '2', 1, '王教授', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangjs', 0);

-- 公告信息
INSERT INTO `notice` (`title`, `introduction`, `typename`, `name`, `headportrait`, `clicknum`) VALUES
('关于2025-2026学年第二学期实验室开放安排的通知', '根据教务处安排，本学期实验室将于2026年3月1日起正式开放，各实验室具体开放时间详见系统。请同学们提前做好实验预约安排。', '教务通知', '系统管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 356),
('人工智能实验室设备升级通知', '人工智能实验室已完成GPU服务器升级，新增4台NVIDIA A100 GPU工作站，欢迎同学们预约使用。', '设备通知', '系统管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 245),
('关于实验室安全规范的提醒', '请各位同学在使用实验室时严格遵守安全规范，注意用电安全，实验结束后请关闭设备并清理实验台。', '安全通知', '系统管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 189),
('五一假期实验室闭馆通知', '五一劳动节假期（5月1日-5月5日）期间，全校实验室暂停对外开放，5月6日恢复正常。请合理安排实验时间。', '放假通知', '系统管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 123);

-- 预约申请示例数据
INSERT INTO `reservation` (`lab_id`, `lab_name`, `shiyanshileixing`, `weizhi`, `yuyueshuoming`, `yuyueshijian`, `yuyuejieshu`, `xingming`, `xuehao`, `zhuanye`, `banji`, `zhuangtai`, `sfsh`, `shhf`, `userid`) VALUES
('LAB001', '计算机基础实验室A', '计算机实验室', '实验楼A座301', '数据结构课程实验', '2026-04-20 14:00:00', '2026-04-20 16:00:00', '张三', '2021001', '计算机科学与技术', '计科2101班', '已通过', '已通过', '申请已通过，请按时到达实验室', 1),
('LAB004', '人工智能实验室', '人工智能实验室', '实验楼C座401', '深度学习模型训练', '2026-04-22 09:00:00', '2026-04-22 12:00:00', '赵六', '2021004', '人工智能', 'AI2101班', '已通过', '已通过', '请注意GPU使用规范', 4),
('LAB002', '计算机网络实验室', '网络工程实验室', '实验楼A座302', 'Socket编程实验', '2026-04-25 14:00:00', '2026-04-25 17:00:00', '王五', '2021003', '网络工程', '网工2101班', '待审核', '待审核', NULL, 3);

-- 使用记录示例数据
INSERT INTO `usage_record` (`lab_id`, `lab_name`, `shiyanshileixing`, `shiyongrenshu`, `shiyongjilu`, `shiyongshijian`, `xingming`, `zhuanye`, `banji`, `userid`) VALUES
('LAB001', '计算机基础实验室A', '计算机实验室', 35, '数据结构课程实验-链表操作', '2026-04-15 14:00:00', '张三', '计算机科学与技术', '计科2101班', 1),
('LAB004', '人工智能实验室', '人工智能实验室', 12, '深度学习图像分类实验', '2026-04-16 09:00:00', '赵六', '人工智能', 'AI2101班', 4);
