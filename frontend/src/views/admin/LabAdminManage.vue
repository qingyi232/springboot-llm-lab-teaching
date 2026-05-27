<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.xingming" placeholder="姓名" clearable style="width: 160px;" /></template>
    <el-table-column prop="gonghao" label="工号" width="120" /><el-table-column prop="xingming" label="姓名" width="100" /><el-table-column prop="xingbie" label="性别" width="80" /><el-table-column prop="shouji" label="手机" width="130" /><el-table-column prop="guanlishipanshi" label="管理实验室" min-width="250" /><el-table-column prop="addtime" label="注册时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="500px">
    <el-form :model="form" label-width="100px"><el-form-item label="工号"><el-input v-model="form.gonghao" :disabled="isEdit" /></el-form-item><el-form-item label="密码"><el-input v-model="form.mima" /></el-form-item><el-form-item label="姓名"><el-input v-model="form.xingming" /></el-form-item><el-form-item label="性别"><el-select v-model="form.xingbie" style="width: 100%;"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item><el-form-item label="手机"><el-input v-model="form.shouji" /></el-form-item><el-form-item label="管理实验室"><el-input v-model="form.guanlishipanshi" placeholder="多个用逗号分隔" /></el-form-item></el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ xingming: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/labAdmin/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, gonghao: '', mima: '123456', xingming: '', xingbie: '男', shouji: '', guanlishipanshi: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/labAdmin/update', form); else await request.post('/labAdmin/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/labAdmin/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
