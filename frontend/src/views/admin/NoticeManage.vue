<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.title" placeholder="标题" clearable style="width: 200px;" /></template>
    <el-table-column prop="title" label="标题" min-width="250" /><el-table-column prop="typename" label="分类" width="120" /><el-table-column prop="name" label="发布人" width="120" /><el-table-column prop="clicknum" label="浏览" width="80" /><el-table-column prop="addtime" label="发布时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="600px">
    <el-form :model="form" label-width="80px"><el-form-item label="标题"><el-input v-model="form.title" /></el-form-item><el-form-item label="分类"><el-select v-model="form.typename" style="width: 100%;" allow-create filterable><el-option label="教务通知" value="教务通知" /><el-option label="设备通知" value="设备通知" /><el-option label="安全通知" value="安全通知" /><el-option label="放假通知" value="放假通知" /></el-select></el-form-item><el-form-item label="发布人"><el-input v-model="form.name" /></el-form-item><el-form-item label="内容"><el-input v-model="form.introduction" type="textarea" :rows="6" /></el-form-item></el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ title: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/notice/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, title: '', typename: '教务通知', name: '系统管理员', introduction: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/notice/update', form); else await request.post('/notice/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/notice/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
