<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">个人中心</h3>
    <el-form :model="form" label-width="100px" style="max-width: 500px;">
      <el-form-item label="头像">
        <el-avatar :size="64" :src="form.touxiang || form.image || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'" />
      </el-form-item>
      <template v-if="role === '学生'">
        <el-form-item label="学号"><el-input v-model="form.xuehao" disabled /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.xingming" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="form.xingbie" style="width: 100%;"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item>
        <el-form-item label="专业"><el-input v-model="form.zhuanye" /></el-form-item>
        <el-form-item label="班级"><el-input v-model="form.banji" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.shouji" /></el-form-item>
      </template>
      <template v-else-if="role === '教师'">
        <el-form-item label="工号"><el-input v-model="form.gonghao" disabled /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.jiaoshixingming" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="form.xingbie" style="width: 100%;"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item>
        <el-form-item label="职称"><el-input v-model="form.zhicheng" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.shouji" /></el-form-item>
      </template>
      <template v-else>
        <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
      </template>
      <el-form-item><el-button type="primary" @click="handleSave">保存修改</el-button></el-form-item>
    </el-form>
    <el-divider />
    <h4 style="margin-bottom: 16px;">修改密码</h4>
    <el-form label-width="100px" style="max-width: 500px;">
      <el-form-item label="新密码"><el-input v-model="newPassword" type="password" show-password placeholder="请输入新密码" /></el-form-item>
      <el-form-item><el-button type="warning" @click="changePassword">修改密码</el-button></el-form-item>
    </el-form>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
const userStore = useUserStore(); const role = userStore.role; const form = reactive({}); const newPassword = ref('')
const apiMap = { '学生': '/student', '教师': '/teacher', '实验室管理员': '/labAdmin', '管理员': '/users' }
onMounted(async () => { const res = await request.get(`${apiMap[role]}/session`); Object.assign(form, res.data) })
const handleSave = async () => { await request.put(`${apiMap[role]}/update`, form); userStore.setLogin(userStore.token, form, role); ElMessage.success('保存成功') }
const changePassword = async () => {
  if (!newPassword.value) { ElMessage.warning('请输入新密码'); return }
  const key = role === '管理员' ? 'password' : 'mima'
  form[key] = newPassword.value
  await request.put(`${apiMap[role]}/update`, form); ElMessage.success('密码修改成功')
}
</script>
