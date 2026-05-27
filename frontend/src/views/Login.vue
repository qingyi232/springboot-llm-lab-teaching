<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="bg-pattern"></div>
    </div>
    <div class="login-container">
      <div class="login-left">
        <div class="brand">
          <div class="brand-icon">
            <el-icon :size="32"><School /></el-icon>
          </div>
          <h1>高校实验教学支持系统</h1>
          <p>智能化实验教学管理 · AI赋能学习体验</p>
        </div>
        <div class="features">
          <div class="feature-item" v-for="f in features" :key="f.title">
            <el-icon :size="20"><component :is="f.icon" /></el-icon>
            <div>
              <h4>{{ f.title }}</h4>
              <span>{{ f.desc }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-box">
          <h2>欢迎登录</h2>
          <p class="sub-title">请选择角色并输入账号信息</p>

          <div class="role-tabs">
            <div
              v-for="r in roles"
              :key="r.value"
              :class="['role-tab', { active: loginForm.role === r.value }]"
              @click="loginForm.role = r.value"
            >
              <el-icon><component :is="r.icon" /></el-icon>
              <span>{{ r.label }}</span>
            </div>
          </div>

          <el-form :model="loginForm" class="login-form">
            <el-form-item>
              <el-input
                v-model="loginForm.username"
                :placeholder="loginForm.role === '管理员' ? '请输入用户名' : loginForm.role === '学生' ? '请输入学号' : '请输入工号'"
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleLogin" style="width: 100%; height: 48px; font-size: 16px; border-radius: 10px;">
              登 录
            </el-button>
          </el-form>

          <div class="login-footer" v-if="loginForm.role !== '管理员'">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
          </div>

          <div class="demo-accounts">
            <p>演示账号（密码均为 123456）</p>
            <div class="demo-list">
              <span @click="fillDemo('admin', '管理员')">管理员: admin</span>
              <span @click="fillDemo('2021001', '学生')">学生: 2021001</span>
              <span @click="fillDemo('T2024001', '教师')">教师: T2024001</span>
              <span @click="fillDemo('G2024001', '实验室管理员')">实验室管理员: G2024001</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store'
import request from '../api/request'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  role: '学生'
})

const roles = [
  { value: '学生', label: '学生', icon: 'UserFilled' },
  { value: '教师', label: '教师', icon: 'Reading' },
  { value: '实验室管理员', label: '实验室管理员', icon: 'Setting' },
  { value: '管理员', label: '管理员', icon: 'Tools' },
]

const features = [
  { icon: 'Monitor', title: '实验室预约', desc: '在线预约实验室，智能调度' },
  { icon: 'Collection', title: '教学资源', desc: '课程资料和视频一站式管理' },
  { icon: 'ChatDotSquare', title: 'AI智能助手', desc: 'DeepSeek大模型赋能学习' },
  { icon: 'DataAnalysis', title: '数据分析', desc: '实验室使用数据可视化' },
]

const fillDemo = (username, role) => {
  loginForm.username = username
  loginForm.password = '123456'
  loginForm.role = role
}

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true

  try {
    const apiMap = {
      '管理员': '/users/login',
      '学生': '/student/login',
      '教师': '/teacher/login',
      '实验室管理员': '/labAdmin/login',
    }
    const res = await request.post(apiMap[loginForm.role], {
      username: loginForm.username,
      password: loginForm.password,
    })

    userStore.setLogin(res.token, res.user, loginForm.role)
    ElMessage.success('登录成功')

    if (loginForm.role === '管理员') {
      router.push('/admin')
    } else {
      router.push('/home')
    }
  } catch (e) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ecfdf5 0%, #f0fdfa 50%, #f8fafc 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.bg-pattern {
  position: absolute;
  top: -50%;
  right: -20%;
  width: 800px;
  height: 800px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(16, 185, 129, 0.08) 0%, transparent 70%);
}

.login-container {
  display: flex;
  width: 960px;
  min-height: 580px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.login-left {
  width: 400px;
  background: linear-gradient(160deg, #059669 0%, #10b981 50%, #34d399 100%);
  padding: 48px 36px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
}

.brand-icon {
  width: 56px;
  height: 56px;
  background: rgba(255,255,255,0.2);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.brand h1 {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 8px;
}

.brand p {
  font-size: 14px;
  opacity: 0.85;
  margin-bottom: 36px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 14px;
  background: rgba(255,255,255,0.1);
  border-radius: 12px;
  backdrop-filter: blur(4px);
}

.feature-item h4 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 2px;
}

.feature-item span {
  font-size: 12px;
  opacity: 0.8;
}

.login-right {
  flex: 1;
  padding: 48px 40px;
  display: flex;
  align-items: center;
}

.login-box {
  width: 100%;
}

.login-box h2 {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.sub-title {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 28px;
}

.role-tabs {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
  margin-bottom: 24px;
}

.role-tab {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px 4px;
  border-radius: 10px;
  cursor: pointer;
  border: 2px solid var(--border);
  transition: all 0.3s;
  font-size: 12px;
  color: var(--text-secondary);
}

.role-tab:hover {
  border-color: var(--primary-light);
}

.role-tab.active {
  border-color: var(--primary);
  background: var(--primary-bg);
  color: var(--primary);
}

.login-form {
  margin-bottom: 16px;
}

.login-footer {
  text-align: center;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 20px;
}

.login-footer a {
  color: var(--primary);
  font-weight: 500;
  margin-left: 4px;
}

.demo-accounts {
  background: var(--bg-main);
  border-radius: 10px;
  padding: 14px;
}

.demo-accounts p {
  font-size: 12px;
  color: var(--text-light);
  margin-bottom: 8px;
}

.demo-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.demo-list span {
  font-size: 12px;
  padding: 4px 10px;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  color: var(--text-secondary);
  border: 1px solid var(--border);
  transition: all 0.2s;
}

.demo-list span:hover {
  border-color: var(--primary);
  color: var(--primary);
}
</style>
