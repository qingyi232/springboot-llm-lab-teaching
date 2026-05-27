# 基于SpringBoot和大模型的高校实验教学支持系统

## 技术栈

- **后端**: Java 8+ / SpringBoot 2.7 / MyBatis-Plus / MySQL 8 / Redis
- **前端**: Vue 3 / Vite / Element Plus / Pinia / Axios
- **AI**: DeepSeek 大模型 API（SSE 流式响应）

## 快速启动

### 1. 数据库初始化

```bash
# 登录 MySQL，执行初始化脚本
mysql -u root -p < backend/src/main/resources/sql/init.sql
```

### 2. 后端启动

```bash
cd backend
# 修改 application.yml 中数据库连接信息
# 如需 AI 功能，配置 deepseek.api-key
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
# 前端运行在 http://localhost:5173
```

## 演示账号（密码均为 123456）

| 角色 | 账号 |
|------|------|
| 管理员 | admin |
| 学生 | 2021001 |
| 教师 | T2024001 |
| 实验室管理员 | G2024001 |

## 功能模块

- 用户注册/登录（学生、教师、实验室管理员、管理员）
- 实验室管理与预约
- 课程资料管理
- 学习视频管理
- 作业提交与成绩管理
- 论坛交流
- 公告信息管理
- AI 智能问答（DeepSeek SSE 流式输出）
- 收藏功能
- 后台管理系统
