<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search>
      <el-input v-model="search.labName" placeholder="实验室名称" clearable style="width: 180px;" />
      <el-input v-model="search.xingming" placeholder="使用人" clearable style="width: 140px;" />
    </template>
    <el-table-column prop="labName" label="实验室" min-width="140" />
    <el-table-column prop="xingming" label="使用人" width="100" />
    <el-table-column prop="shiyongshijian" label="使用时间" width="180" />
    <el-table-column prop="shiyongrenshu" label="使用人数" width="90" />
    <el-table-column prop="shiyongjilu" label="使用记录" min-width="200" show-overflow-tooltip />
    <el-table-column prop="addtime" label="记录时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加使用记录'" width="500px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="实验室"><el-select v-model="form.labName" style="width: 100%;" @change="onLabChange"><el-option v-for="lab in labs" :key="lab.labName" :label="lab.labName" :value="lab.labName" /></el-select></el-form-item>
      <el-form-item label="使用人"><el-input v-model="form.xingming" /></el-form-item>
      <el-form-item label="使用时间"><el-date-picker v-model="form.shiyongshijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%;" /></el-form-item>
      <el-form-item label="使用人数"><el-input-number v-model="form.shiyongrenshu" :min="1" :max="200" /></el-form-item>
      <el-form-item label="记录"><el-input v-model="form.shiyongjilu" type="textarea" :rows="2" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const labs = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ labName: '', xingming: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/usageRecord/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => { const l = await request.get('/labInfo/list'); labs.value = l.data || []; loadData() })
const onLabChange = (val) => { const lab = labs.value.find(l => l.labName === val); if (lab) { form.labId = lab.labId; form.shiyanshileixing = lab.shiyanshileixing } }
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, labName: '', labId: '', shiyanshileixing: '', xingming: '', shiyongshijian: '', shiyongrenshu: 1, shiyongjilu: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/usageRecord/update', form); else await request.post('/usageRecord/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/usageRecord/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
