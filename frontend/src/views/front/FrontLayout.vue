<template>
  <div class="front-layout">
    <header class="front-header">
      <div class="header-inner">
        <div class="logo" @click="$router.push('/home')">
          <div class="logo-icon"><el-icon :size="22"><School /></el-icon></div>
          <span>高校实验教学支持系统</span>
        </div>
        <nav class="nav-menu">
          <router-link to="/home" :class="{ active: $route.path === '/home' }">首页</router-link>
          <router-link to="/course-material" :class="{ active: $route.path.startsWith('/course-material') }">课程资料</router-link>
          <router-link to="/video" :class="{ active: $route.path.startsWith('/video') }">学习视频</router-link>
          <router-link to="/lab" :class="{ active: $route.path.startsWith('/lab') }">实验室</router-link>
          <router-link v-if="userStore.role !== '实验室管理员'" to="/homework" :class="{ active: $route.path.startsWith('/homework') }">作业信息</router-link>
          <router-link to="/forum" :class="{ active: $route.path.startsWith('/forum') }">论坛交流</router-link>
          <router-link to="/notice" :class="{ active: $route.path.startsWith('/notice') }">公告信息</router-link>
          <router-link to="/ai-chat" :class="{ active: $route.path === '/ai-chat' }">
            <el-icon style="margin-right: 2px;"><ChatDotSquare /></el-icon>AI助手
          </router-link>
        </nav>
        <div class="header-right">
          <template v-if="userStore.token">
            <el-dropdown trigger="click">
              <div class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo.touxiang || userStore.userInfo.image || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'" />
                <span>{{ userStore.userInfo.xingming || userStore.userInfo.jiaoshixingming || userStore.userInfo.nickname || userStore.userInfo.username }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')"><el-icon><User /></el-icon>个人中心</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '学生' || userStore.role === '教师'" @click="$router.push('/my-reservation')"><el-icon><Calendar /></el-icon>预约申请</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '学生' || userStore.role === '教师'" @click="$router.push('/my-usage')"><el-icon><Document /></el-icon>使用记录</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '学生'" @click="$router.push('/my-homework')"><el-icon><Edit /></el-icon>作业提交</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '学生'" @click="$router.push('/my-score')"><el-icon><TrophyBase /></el-icon>作业成绩</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/my-collection')"><el-icon><Star /></el-icon>我的收藏</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/my-forum')"><el-icon><ChatDotRound /></el-icon>我的发布</el-dropdown-item>
                  <el-dropdown-item divided v-if="userStore.role === '教师'" @click="$router.push('/teacher-manage')"><el-icon><Reading /></el-icon>教学管理</el-dropdown-item>
                  <el-dropdown-item divided v-if="userStore.role === '实验室管理员'" @click="$router.push('/labadmin-manage')"><el-icon><Monitor /></el-icon>实验室管理</el-dropdown-item>
                  <el-dropdown-item divided v-if="userStore.role === '管理员'" @click="$router.push('/admin')"><el-icon><Setting /></el-icon>后台管理</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout"><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')" round>登录</el-button>
          </template>
        </div>
      </div>
    </header>
    <main class="front-main">
      <router-view />
    </main>
    <footer class="front-footer">
      <p>高校实验教学支持系统 &copy; 2026 · 基于SpringBoot和大模型技术</p>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.front-layout { min-height: 100vh; display: flex; flex-direction: column; }
.front-header {
  background: white;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  gap: 32px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  white-space: nowrap;
}
.logo-icon {
  width: 36px;
  height: 36px;
  background: var(--primary);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}
.nav-menu {
  display: flex;
  gap: 4px;
  flex: 1;
}
.nav-menu a {
  padding: 8px 14px;
  border-radius: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  white-space: nowrap;
}
.nav-menu a:hover, .nav-menu a.active {
  color: var(--primary);
  background: var(--primary-bg);
}
.header-right { display: flex; align-items: center; }
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
  font-size: 14px;
}
.user-info:hover { background: var(--bg-main); }
.front-main {
  flex: 1;
  max-width: 1280px;
  margin: 0 auto;
  width: 100%;
  padding: 24px;
}
.front-footer {
  text-align: center;
  padding: 20px;
  color: var(--text-light);
  font-size: 13px;
  border-top: 1px solid var(--border-light);
}
</style>
