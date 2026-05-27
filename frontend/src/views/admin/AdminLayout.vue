<template>
  <el-container style="height: 100vh;">
    <el-aside width="220px" style="background: #1e293b;">
      <div style="padding: 20px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.08);">
        <div style="width: 40px; height: 40px; background: var(--primary); border-radius: 10px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 8px;">
          <el-icon :size="20" color="white"><School /></el-icon>
        </div>
        <div style="color: white; font-size: 14px; font-weight: 600;">实验教学支持系统</div>
        <div style="color: #64748b; font-size: 12px;">管理后台</div>
      </div>
      <el-menu :default-active="$route.path" :router="true" background-color="#1e293b" text-color="#94a3b8" active-text-color="#10b981" style="border: none;">
        <el-menu-item index="/admin"><el-icon><DataAnalysis /></el-icon><span>数据面板</span></el-menu-item>
        <el-sub-menu index="user-mgr"><template #title><el-icon><User /></el-icon><span>用户管理</span></template>
          <el-menu-item index="/admin/users">管理员管理</el-menu-item>
          <el-menu-item index="/admin/student">学生管理</el-menu-item>
          <el-menu-item index="/admin/teacher">教师管理</el-menu-item>
          <el-menu-item index="/admin/lab-admin">实验室管理员</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="lab-mgr"><template #title><el-icon><Monitor /></el-icon><span>实验室管理</span></template>
          <el-menu-item index="/admin/lab-type">实验室类型</el-menu-item>
          <el-menu-item index="/admin/lab-info">实验室信息</el-menu-item>
          <el-menu-item index="/admin/reservation">预约申请</el-menu-item>
          <el-menu-item index="/admin/usage-record">使用记录</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="course-mgr"><template #title><el-icon><Collection /></el-icon><span>教学管理</span></template>
          <el-menu-item index="/admin/course-type">课程类型</el-menu-item>
          <el-menu-item index="/admin/course-material">课程资料</el-menu-item>
          <el-menu-item index="/admin/video">学习视频</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="hw-mgr"><template #title><el-icon><Edit /></el-icon><span>作业管理</span></template>
          <el-menu-item index="/admin/homework">作业信息</el-menu-item>
          <el-menu-item index="/admin/homework-submit">作业提交</el-menu-item>
          <el-menu-item index="/admin/homework-score">作业成绩</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/admin/forum"><el-icon><ChatDotRound /></el-icon><span>论坛管理</span></el-menu-item>
        <el-menu-item index="/admin/notice"><el-icon><Bell /></el-icon><span>公告管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: white; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 1px 3px rgba(0,0,0,0.06); padding: 0 24px;">
        <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item></el-breadcrumb>
        <div style="display: flex; align-items: center; gap: 12px;">
          <el-button type="primary" size="small" plain @click="$router.push('/home')"><el-icon><House /></el-icon>前台首页</el-button>
          <el-dropdown>
            <div style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
              <el-avatar :size="32" :src="userStore.userInfo.image || 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin'" />
              <span style="font-size: 14px;">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
            </div>
            <template #dropdown><el-dropdown-menu><el-dropdown-item @click="handleLogout"><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item></el-dropdown-menu></template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main style="background: var(--bg-main); padding: 20px;"><router-view /></el-main>
    </el-container>
  </el-container>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store'
import { ElMessage } from 'element-plus'
const router = useRouter(); const userStore = useUserStore()
const handleLogout = () => { userStore.logout(); ElMessage.success('已退出'); router.push('/login') }
</script>
<style scoped>
.el-aside { overflow-y: auto; }
.el-aside::-webkit-scrollbar { width: 0; }
</style>
