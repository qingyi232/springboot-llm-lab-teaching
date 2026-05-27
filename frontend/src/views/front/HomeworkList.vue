<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">作业信息</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="作业标题" min-width="200" />
      <el-table-column prop="coursetype" label="课程类型" width="140" />
      <el-table-column prop="faburenxingming" label="发布人" width="120" />
      <el-table-column prop="jiezhishijian" label="截止时间" width="180" />
      <el-table-column v-if="userStore.role === '学生'" label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="getSubmitStatus(row.id) === '已批改'" type="success" size="small">已批改</el-tag>
          <el-tag v-else-if="getSubmitStatus(row.id) === '已提交'" type="warning" size="small">已提交</el-tag>
          <el-tag v-else type="info" size="small">未提交</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="userStore.role === '学生'" label="操作" width="160">
        <template #default="{ row }">
          <el-button v-if="!getSubmitStatus(row.id)" type="primary" size="small" @click="showSubmit(row)">提交作业</el-button>
          <el-button v-else type="info" size="small" disabled>已提交</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" title="提交作业" width="500px">
      <el-form label-width="80px">
        <el-form-item label="作业"><span>{{ current.title }}</span></el-form-item>
        <el-form-item label="提交内容"><el-input v-model="submitForm.content" type="textarea" :rows="4" placeholder="请输入提交内容" /></el-form-item>
        <el-form-item label="附件">
          <el-upload action="/api/file/upload" :headers="uploadHeaders" :on-success="onUploadSuccess" :on-remove="onUploadRemove" :limit="1" :file-list="fileList">
            <el-button size="small" type="primary"><el-icon><Upload /></el-icon>上传附件</el-button>
            <template #tip><div style="color: var(--text-light); font-size: 12px; margin-top: 4px;">支持 doc/docx/pdf/zip/rar 等格式，最大 100MB</div></template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">提交</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
const userStore = useUserStore(); const list = ref([]); const dialogVisible = ref(false); const current = ref({})
const submitForm = reactive({ content: '', fujian: '' })
const fileList = ref([])
const uploadHeaders = { Token: localStorage.getItem('token') || '' }
const mySubmits = ref([])
const getSubmitStatus = (homeworkId) => {
  const s = mySubmits.value.find(item => item.homeworkId === homeworkId)
  return s ? s.zhuangtai : null
}
const loadMySubmits = async () => {
  try {
    const res = await request.get('/homework/submit/mylist', { params: { limit: 999 } })
    mySubmits.value = res.data?.list || []
  } catch (e) {}
}
onMounted(async () => {
  const res = await request.get('/homework/list'); list.value = res.data || []
  await loadMySubmits()
})
const showSubmit = (row) => { current.value = row; submitForm.content = ''; submitForm.fujian = ''; fileList.value = []; dialogVisible.value = true }
const onUploadSuccess = (res) => { if (res.code === 0) { submitForm.fujian = res.url; ElMessage.success('附件上传成功') } else { ElMessage.error(res.msg || '上传失败') } }
const onUploadRemove = () => { submitForm.fujian = '' }
const handleSubmit = async () => {
  if (!submitForm.content) { ElMessage.warning('请输入提交内容'); return }
  const u = userStore.userInfo
  await request.post('/homework/submit/save', { homeworkId: current.value.id, homeworkTitle: current.value.title, content: submitForm.content, fujian: submitForm.fujian, xingming: u.xingming, xuehao: u.xuehao })
  ElMessage.success('提交成功'); dialogVisible.value = false; await loadMySubmits()
}
</script>
