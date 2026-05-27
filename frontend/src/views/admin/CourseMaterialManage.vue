<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.title" placeholder="标题" clearable style="width: 200px;" /></template>
    <el-table-column prop="title" label="标题" min-width="200" /><el-table-column prop="coursetype" label="课程类型" width="140" /><el-table-column prop="faburenxingming" label="发布人" width="100" /><el-table-column prop="clicknum" label="浏览" width="80" /><el-table-column prop="storecount" label="收藏" width="80" /><el-table-column prop="addtime" label="发布时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="600px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="课程类型"><el-select v-model="form.coursetype" style="width: 100%;"><el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" /></el-select></el-form-item>
      <el-form-item label="封面图片">
        <el-upload class="cover-uploader" action="/api/file/upload" :headers="uploadHeaders" :show-file-list="false" :on-success="handleCoverSuccess" :before-upload="beforeImageUpload" accept="image/*">
          <img v-if="form.tupian" :src="form.tupian" class="upload-preview-img" />
          <el-icon v-else class="upload-icon"><Plus /></el-icon>
          <template #tip><div class="el-upload__tip">支持 jpg/png/gif，不超过 10MB</div></template>
        </el-upload>
      </el-form-item>
      <el-form-item label="发布人"><el-input v-model="form.faburenxingming" /></el-form-item>
      <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const types = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ title: '' }); const form = reactive({})
const uploadHeaders = { Token: localStorage.getItem('token') || '' }

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isImage) { ElMessage.error('只能上传图片文件'); return false }
  if (!isLt10M) { ElMessage.error('图片大小不能超过 10MB'); return false }
  return true
}
const handleCoverSuccess = (res) => {
  if (res.code === 0) { form.tupian = res.url; ElMessage.success('图片上传成功') }
  else { ElMessage.error(res.msg || '上传失败') }
}
const loadData = async () => { const res = await request.get('/courseMaterial/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => { const t = await request.get('/courseType/list'); types.value = t.data || []; loadData() })
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, title: '', coursetype: '', tupian: '', content: '', faburenxingming: '' }); dialogVisible.value = true }
const handleSave = async () => { if (isEdit.value) await request.put('/courseMaterial/update', form); else await request.post('/courseMaterial/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/courseMaterial/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
<style scoped>
.cover-uploader :deep(.el-upload) { border: 1px dashed #d9d9d9; border-radius: 6px; cursor: pointer; width: 148px; height: 148px; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.cover-uploader :deep(.el-upload:hover) { border-color: #409eff; }
.upload-preview-img { width: 148px; height: 148px; object-fit: cover; display: block; }
.upload-icon { font-size: 28px; color: #8c939d; }
</style>
