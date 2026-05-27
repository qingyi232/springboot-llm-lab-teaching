<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search>
      <el-input v-model="search.xingming" placeholder="姓名" clearable style="width: 160px;" />
      <el-input v-model="search.xuehao" placeholder="学号" clearable style="width: 160px;" />
    </template>
    <el-table-column prop="xuehao" label="学号" width="120" />
    <el-table-column prop="xingming" label="姓名" width="100" />
    <el-table-column prop="xingbie" label="性别" width="80" />
    <el-table-column prop="nianling" label="年龄" width="80" />
    <el-table-column prop="zhuanye" label="专业" width="160" />
    <el-table-column prop="banji" label="班级" width="140" />
    <el-table-column prop="shouji" label="手机" width="130" />
    <el-table-column prop="addtime" label="注册时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑学生' : '添加学生'" width="500px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="学号"><el-input v-model="form.xuehao" :disabled="isEdit" /></el-form-item>
      <el-form-item label="密码"><el-input v-model="form.mima" placeholder="不修改请留空" /></el-form-item>
      <el-form-item label="姓名"><el-input v-model="form.xingming" /></el-form-item>
      <el-form-item label="性别"><el-select v-model="form.xingbie" style="width: 100%;"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item>
      <el-form-item label="年龄"><el-input-number v-model="form.nianling" :min="15" :max="50" /></el-form-item>
      <el-form-item label="专业"><el-input v-model="form.zhuanye" /></el-form-item>
      <el-form-item label="班级"><el-input v-model="form.banji" /></el-form-item>
      <el-form-item label="手机"><el-input v-model="form.shouji" /></el-form-item>
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
const search = reactive({ xingming: '', xuehao: '' })
const form = reactive({ id: null, xuehao: '', mima: '', xingming: '', xingbie: '男', nianling: 20, zhuanye: '', banji: '', shouji: '' })
const loadData = async () => { const res = await request.get('/student/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; if (row) Object.assign(form, row); else Object.assign(form, { id: null, xuehao: '', mima: '123456', xingming: '', xingbie: '男', nianling: 20, zhuanye: '', banji: '', shouji: '' }); dialogVisible.value = true }
const handleSave = async () => {
  if (isEdit.value) await request.put('/student/update', form)
  else await request.post('/student/save', form)
  ElMessage.success('保存成功'); dialogVisible.value = false; loadData()
}
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/student/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
