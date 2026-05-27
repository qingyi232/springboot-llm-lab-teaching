<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.labName" placeholder="实验室" clearable style="width: 200px;" /></template>
    <el-table-column prop="labName" label="实验室" width="180" /><el-table-column prop="shiyanshileixing" label="类型" width="140" /><el-table-column prop="shiyongrenshu" label="人数" width="80" /><el-table-column prop="shiyongjilu" label="使用记录" min-width="200" /><el-table-column prop="shiyongshijian" label="使用时间" width="170" /><el-table-column prop="xingming" label="姓名" width="100" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="500px">
    <el-form :model="form" label-width="100px"><el-form-item label="实验室名称"><el-input v-model="form.labName" /></el-form-item><el-form-item label="类型"><el-input v-model="form.shiyanshileixing" /></el-form-item><el-form-item label="使用人数"><el-input-number v-model="form.shiyongrenshu" :min="1" /></el-form-item><el-form-item label="使用记录"><el-input v-model="form.shiyongjilu" type="textarea" /></el-form-item><el-form-item label="使用时间"><el-date-picker v-model="form.shiyongshijian" type="datetime" style="width: 100%;" /></el-form-item><el-form-item label="姓名"><el-input v-model="form.xingming" /></el-form-item></el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ labName: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/usageRecord/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, labName: '', shiyanshileixing: '', shiyongrenshu: 1, shiyongjilu: '', shiyongshijian: '', xingming: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/usageRecord/update', form); else await request.post('/usageRecord/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/usageRecord/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
