<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.typename" placeholder="类型名称" clearable style="width: 200px;" /></template>
    <el-table-column prop="id" label="ID" width="80" /><el-table-column prop="typename" label="类型名称" min-width="200" /><el-table-column prop="addtime" label="创建时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="400px">
    <el-form :model="form" label-width="80px"><el-form-item label="类型名称"><el-input v-model="form.typename" /></el-form-item></el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ typename: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/labType/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, typename: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/labType/update', form); else await request.post('/labType/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/labType/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
