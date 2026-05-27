<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.labName" placeholder="实验室名称" clearable style="width: 200px;" /></template>
    <el-table-column prop="labName" label="名称" min-width="160" />
    <el-table-column prop="shiyanshileixing" label="类型" width="120" />
    <el-table-column prop="weizhi" label="位置" width="160" />
    <el-table-column prop="rongna" label="容量" width="80" />
    <el-table-column prop="zhuangtai" label="状态" width="100">
      <template #default="{ row }"><el-tag :type="row.zhuangtai === '空闲' ? 'success' : 'warning'" size="small">{{ row.zhuangtai }}</el-tag></template>
    </el-table-column>
    <el-table-column prop="guanliyuan" label="管理员" width="100" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑实验室' : '新增实验室'" width="600px">
    <el-form :model="form" label-width="100px">
      <el-row :gutter="16"><el-col :span="12"><el-form-item label="实验室编号"><el-input v-model="form.labId" :disabled="isEdit" /></el-form-item></el-col><el-col :span="12"><el-form-item label="名称"><el-input v-model="form.labName" /></el-form-item></el-col></el-row>
      <el-row :gutter="16"><el-col :span="12"><el-form-item label="类型"><el-select v-model="form.shiyanshileixing" style="width: 100%;"><el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" /></el-select></el-form-item></el-col><el-col :span="12"><el-form-item label="位置"><el-input v-model="form.weizhi" /></el-form-item></el-col></el-row>
      <el-row :gutter="16"><el-col :span="12"><el-form-item label="容纳人数"><el-input-number v-model="form.rongna" :min="1" /></el-form-item></el-col><el-col :span="12"><el-form-item label="状态"><el-select v-model="form.zhuangtai" style="width: 100%;"><el-option label="空闲" value="空闲" /><el-option label="使用中" value="使用中" /><el-option label="维修中" value="维修中" /></el-select></el-form-item></el-col></el-row>
      <el-form-item label="开放时间"><el-input v-model="form.kaifangshijian" /></el-form-item>
      <el-form-item label="实验室图片">
        <el-upload action="/api/file/upload" :show-file-list="false" :on-success="onUploadSuccess" :before-upload="beforeUpload" :headers="uploadHeaders">
          <img v-if="form.tupian" :src="form.tupian" style="width: 200px; height: 120px; object-fit: cover; border-radius: 8px; cursor: pointer;" />
          <el-button v-else type="primary" plain>点击上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="管理员"><el-input v-model="form.guanliyuan" /></el-form-item>
      <el-form-item label="简介"><el-input v-model="form.jianjie" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const types = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ labName: '' }); const form = reactive({})
const uploadHeaders = computed(() => ({ Token: window.localStorage.getItem('token') || '' }))
const loadData = async () => { const res = await request.get('/labInfo/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => { const t = await request.get('/labType/list'); types.value = t.data || []; loadData() })
const onUploadSuccess = (res) => { if (res.code === 0) { form.tupian = res.url; ElMessage.success('上传成功') } else { ElMessage.error(res.msg || '上传失败') } }
const beforeUpload = (file) => { const isImage = file.type.startsWith('image/'); if (!isImage) { ElMessage.error('只能上传图片文件'); return false } return true }
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, labId: '', labName: '', shiyanshileixing: '', weizhi: '', rongna: 30, zhuangtai: '空闲', kaifangshijian: '周一至周五 8:00-21:00', tupian: '', guanliyuan: '', jianjie: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/labInfo/update', form); else await request.post('/labInfo/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除该实验室？'); await request.delete('/labInfo/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
