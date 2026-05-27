<template>
  <div class="login-page">
    <div class="login-container" style="max-width: 520px;">
      <div class="login-box" style="padding: 40px;">
        <div style="text-align: center; margin-bottom: 28px;">
          <div style="width: 56px; height: 56px; background: var(--primary-bg); border-radius: 14px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 12px;">
            <el-icon :size="28" color="var(--primary)"><UserFilled /></el-icon>
          </div>
          <h2>用户注册</h2>
          <p class="sub-title">请选择注册身份并填写信息</p>
        </div>

        <div class="role-tabs" style="grid-template-columns: repeat(3, 1fr); margin-bottom: 24px;">
          <div v-for="r in regRoles" :key="r.value" :class="['role-tab', { active: form.role === r.value }]" @click="form.role = r.value">
            <el-icon><component :is="r.icon" /></el-icon>
            <span>{{ r.label }}</span>
          </div>
        </div>

        <el-form :model="form" label-position="top">
          <el-form-item :label="form.role === '学生' ? '学号' : '工号'">
            <el-input v-model="form.username" :placeholder="form.role === '学生' ? '请输入学号' : '请输入工号'" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="性别">
                <el-select v-model="form.gender" style="width: 100%;">
                  <el-option label="男" value="男" />
                  <el-option label="女" value="女" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号">
                <el-input v-model="form.phone" placeholder="请输入手机号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="专业" v-if="form.role === '学生'">
            <el-input v-model="form.major" placeholder="请输入专业" />
          </el-form-item>
          <el-form-item label="职称" v-if="form.role === '教师'">
            <el-input v-model="form.title" placeholder="请输入职称" />
          </el-form-item>
          <el-button type="primary" size="large" style="width: 100%; height: 44px; border-radius: 10px;" :loading="loading" @click="handleRegister">
            注 册
          </el-button>
        </el-form>

        <div class="login-footer" style="margin-top: 16px;">
          <span>已有账号？</span>
          <router-link to="/login">返回登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const form = reactive({
  role: '学生', username: '', password: '', name: '', gender: '男', phone: '', major: '', title: ''
})

const regRoles = [
  { value: '学生', label: '学生注册', icon: 'UserFilled' },
  { value: '教师', label: '教师注册', icon: 'Reading' },
  { value: '实验室管理员', label: '实验室管理员', icon: 'Setting' },
]

const handleRegister = async () => {
  if (!form.username || !form.password || !form.name) {
    ElMessage.warning('请填写必要信息')
    return
  }
  loading.value = true
  try {
    const apiMap = {
      '学生': '/student/register',
      '教师': '/teacher/register',
      '实验室管理员': '/labAdmin/register',
    }
    const bodyMap = {
      '学生': { xuehao: form.username, mima: form.password, xingming: form.name, xingbie: form.gender, shouji: form.phone, zhuanye: form.major },
      '教师': { gonghao: form.username, mima: form.password, jiaoshixingming: form.name, xingbie: form.gender, shouji: form.phone, zhicheng: form.title },
      '实验室管理员': { gonghao: form.username, mima: form.password, xingming: form.name, xingbie: form.gender, shouji: form.phone },
    }
    await request.post(apiMap[form.role], bodyMap[form.role])
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {} finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ecfdf5 0%, #f0fdfa 50%, #f8fafc 100%);
}
.login-container {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}
.login-box h2 { font-size: 24px; font-weight: 700; color: var(--text-primary); margin-bottom: 4px; }
.sub-title { font-size: 14px; color: var(--text-secondary); }
.role-tabs { display: grid; gap: 8px; }
.role-tab { display: flex; flex-direction: column; align-items: center; gap: 6px; padding: 12px 4px; border-radius: 10px; cursor: pointer; border: 2px solid var(--border); transition: all 0.3s; font-size: 12px; color: var(--text-secondary); }
.role-tab:hover { border-color: var(--primary-light); }
.role-tab.active { border-color: var(--primary); background: var(--primary-bg); color: var(--primary); }
.login-footer { text-align: center; font-size: 14px; color: var(--text-secondary); }
.login-footer a { color: var(--primary); font-weight: 500; margin-left: 4px; }
</style>
