<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.xingming" placeholder="学生姓名" clearable style="width: 200px;" /></template>
    <el-table-column prop="homeworkTitle" label="作业标题" min-width="180" /><el-table-column prop="xingming" label="学生" width="100" /><el-table-column prop="xuehao" label="学号" width="120" /><el-table-column prop="score" label="成绩" width="80"><template #default="{ row }"><el-tag :type="row.score >= 90 ? 'success' : row.score >= 60 ? '' : 'danger'">{{ row.score }}</el-tag></template></el-table-column><el-table-column prop="pingyuContent" label="评语" min-width="200" show-overflow-tooltip /><el-table-column prop="addtime" label="批改时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑成绩' : '录入成绩'" width="500px">
    <el-form :model="form" label-width="80px"><el-form-item label="作业"><el-input v-model="form.homeworkTitle" /></el-form-item><el-form-item label="学生"><el-input v-model="form.xingming" /></el-form-item><el-form-item label="学号"><el-input v-model="form.xuehao" /></el-form-item><el-form-item label="成绩"><el-input-number v-model="form.score" :min="0" :max="100" /></el-form-item><el-form-item label="评语"><el-input v-model="form.pingyuContent" type="textarea" :rows="3" /></el-form-item></el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ xingming: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/homework/score/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, homeworkTitle: '', xingming: '', xuehao: '', score: 0, pingyuContent: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/homework/score/update', form); else await request.post('/homework/score/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/homework/score/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
