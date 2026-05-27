<template>
  <el-container style="height: 100vh;">
    <el-aside width="220px" style="background: #1e293b;">
      <div style="padding: 20px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.08);">
        <div style="width: 40px; height: 40px; background: var(--primary); border-radius: 10px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 8px;">
          <el-icon :size="20" color="white"><Reading /></el-icon>
        </div>
        <div style="color: white; font-size: 14px; font-weight: 600;">教学管理中心</div>
        <div style="color: #64748b; font-size: 12px;">教师端</div>
      </div>
      <el-menu :default-active="$route.path" :router="true" background-color="#1e293b" text-color="#94a3b8" active-text-color="#10b981" style="border: none;">
        <el-menu-item index="/teacher-manage"><el-icon><DataAnalysis /></el-icon><span>教学概览</span></el-menu-item>
        <el-menu-item index="/teacher-manage/course-material"><el-icon><Document /></el-icon><span>课程资料管理</span></el-menu-item>
        <el-menu-item index="/teacher-manage/video"><el-icon><VideoCamera /></el-icon><span>学习视频管理</span></el-menu-item>
        <el-sub-menu index="hw-mgr"><template #title><el-icon><Edit /></el-icon><span>作业管理</span></template>
          <el-menu-item index="/teacher-manage/homework">布置作业</el-menu-item>
          <el-menu-item index="/teacher-manage/homework-submit">查看提交</el-menu-item>
          <el-menu-item index="/teacher-manage/homework-score">评分管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/teacher-manage/notice"><el-icon><Bell /></el-icon><span>发布公告</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: white; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 1px 3px rgba(0,0,0,0.06); padding: 0 24px;">
        <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/teacher-manage' }">教学管理</el-breadcrumb-item></el-breadcrumb>
        <div style="display: flex; align-items: center; gap: 12px;">
          <el-button type="primary" size="small" plain @click="$router.push('/home')"><el-icon><House /></el-icon>前台首页</el-button>
          <el-dropdown>
            <div style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
              <el-avatar :size="32" :src="userStore.userInfo.touxiang || userStore.userInfo.image || 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher'" />
              <span style="font-size: 14px;">{{ userStore.userInfo.jiaoshixingming || userStore.userInfo.xingming || userStore.userInfo.username }}</span>
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
