<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.username" placeholder="用户名" clearable style="width: 200px;" /></template>
    <el-table-column prop="username" label="用户名" width="140" />
    <el-table-column prop="nickname" label="昵称" width="120" />
    <el-table-column prop="role" label="角色" width="120" />
    <el-table-column prop="phone" label="手机" width="130" />
    <el-table-column prop="email" label="邮箱" min-width="180" />
    <el-table-column prop="addtime" label="创建时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="460px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="用户名"><el-input v-model="form.username" :disabled="isEdit" /></el-form-item>
      <el-form-item label="密码"><el-input v-model="form.password" /></el-form-item>
      <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
      <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false)
const search = reactive({ username: '' })
const form = reactive({ id: null, username: '', password: '', nickname: '', phone: '', email: '', role: '管理员' })
const loadData = async () => { const res = await request.get('/users/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; if (row) Object.assign(form, row); else Object.assign(form, { id: null, username: '', password: '123456', nickname: '', phone: '', email: '', role: '管理员' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/users/update', form); else await request.post('/users/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/users/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
