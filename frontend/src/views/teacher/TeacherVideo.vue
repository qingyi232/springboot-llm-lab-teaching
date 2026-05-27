<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.title" placeholder="标题" clearable style="width: 200px;" /></template>
    <el-table-column prop="title" label="标题" min-width="200" />
    <el-table-column prop="coursetype" label="课程类型" width="140" />
    <el-table-column prop="addtime" label="发布时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="600px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="课程类型"><el-select v-model="form.coursetype" style="width: 100%;"><el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" /></el-select></el-form-item>
      <el-form-item label="封面图片">
        <el-upload
          class="cover-uploader"
          action="/api/file/upload"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleCoverSuccess"
          :before-upload="beforeImageUpload"
          accept="image/*"
        >
          <img v-if="form.tupian" :src="form.tupian" class="upload-preview-img" />
          <el-icon v-else class="upload-icon"><Plus /></el-icon>
          <template #tip><div class="el-upload__tip">支持 jpg/png/gif，不超过 10MB</div></template>
        </el-upload>
      </el-form-item>
      <el-form-item label="视频来源">
        <el-radio-group v-model="videoSource" style="margin-bottom: 12px;">
          <el-radio value="upload">上传视频文件</el-radio>
          <el-radio value="bilibili">B站视频链接</el-radio>
        </el-radio-group>
        <div v-if="videoSource === 'upload'" style="width: 100%;">
          <el-upload
            class="video-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleVideoSuccess"
            :on-progress="handleVideoProgress"
            :before-upload="beforeVideoUpload"
            accept="video/*"
          >
            <div v-if="form.videourl && !form.videourl.includes('bilibili')" class="video-uploaded">
              <el-icon color="#67c23a" :size="20"><SuccessFilled /></el-icon>
              <span style="margin-left: 6px;">{{ videoFileName || '视频已上传' }}</span>
              <el-button link type="primary" style="margin-left: 8px;" @click.stop="form.videourl = ''; videoFileName = ''">重新上传</el-button>
            </div>
            <div v-else class="video-upload-btn">
              <el-icon :size="20"><UploadFilled /></el-icon>
              <span style="margin-left: 6px;">点击上传视频</span>
            </div>
            <template #tip><div class="el-upload__tip">支持 mp4/avi/mov，不超过 100MB</div></template>
          </el-upload>
          <el-progress v-if="videoUploading" :percentage="videoProgress" style="margin-top: 8px;" />
        </div>
        <div v-else style="width: 100%;">
          <el-input v-model="form.videourl" placeholder="请粘贴B站视频链接，如 https://www.bilibili.com/video/BVxxxxxxx" clearable>
            <template #prepend>B站链接</template>
          </el-input>
          <div class="el-upload__tip" style="margin-top: 4px;">支持 bilibili.com 视频链接或嵌入链接</div>
        </div>
      </el-form-item>
      <el-form-item label="简介"><el-input v-model="form.content" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, UploadFilled, SuccessFilled } from '@element-plus/icons-vue'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
import { useUserStore } from '../../store'
const userStore = useUserStore()
const teacherName = userStore.userInfo.jiaoshixingming || userStore.userInfo.xingming || userStore.userInfo.username
const list = ref([]); const types = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ title: '' }); const form = reactive({})
const videoSource = ref('upload')
const videoFileName = ref('')
const videoUploading = ref(false)
const videoProgress = ref(0)
const uploadHeaders = { Token: localStorage.getItem('token') || '' }

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isImage) { ElMessage.error('只能上传图片文件'); return false }
  if (!isLt10M) { ElMessage.error('图片大小不能超过 10MB'); return false }
  return true
}
const beforeVideoUpload = (file) => {
  const isVideo = file.type.startsWith('video/')
  const isLt100M = file.size / 1024 / 1024 < 100
  if (!isVideo) { ElMessage.error('只能上传视频文件'); return false }
  if (!isLt100M) { ElMessage.error('视频大小不能超过 100MB'); return false }
  videoUploading.value = true
  videoProgress.value = 0
  return true
}
const handleCoverSuccess = (res) => {
  if (res.code === 0) { form.tupian = res.url; ElMessage.success('封面上传成功') }
  else { ElMessage.error(res.msg || '上传失败') }
}
const handleVideoSuccess = (res) => {
  videoUploading.value = false
  videoProgress.value = 100
  if (res.code === 0) { form.videourl = res.url; videoFileName.value = res.fileName || '视频已上传'; ElMessage.success('视频上传成功') }
  else { ElMessage.error(res.msg || '上传失败') }
}
const handleVideoProgress = (evt) => {
  videoProgress.value = Math.round(evt.percent || 0)
}
const loadData = async () => { const res = await request.get('/video/page', { params: { page: page.value, limit: 10, faburenxingming: teacherName, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => { const t = await request.get('/courseType/list'); types.value = t.data || []; loadData() })
const openDialog = (row) => {
  isEdit.value = !!row
  Object.assign(form, row || { id: null, title: '', coursetype: '', tupian: '', videourl: '', content: '', faburenxingming: teacherName })
  videoSource.value = (form.videourl && form.videourl.includes('bilibili')) ? 'bilibili' : 'upload'
  videoFileName.value = ''
  videoUploading.value = false
  videoProgress.value = 0
  dialogVisible.value = true
}
const handleSave = async () => { form.faburenxingming = teacherName; if (isEdit.value) await request.put('/video/update', form); else await request.post('/video/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/video/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
<style scoped>
.cover-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 148px;
  height: 148px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.cover-uploader :deep(.el-upload:hover) { border-color: #409eff; }
.upload-preview-img { width: 148px; height: 148px; object-fit: cover; display: block; }
.upload-icon { font-size: 28px; color: #8c939d; }
.video-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  padding: 12px 20px;
  width: 100%;
}
.video-uploader :deep(.el-upload:hover) { border-color: #409eff; }
.video-upload-btn { display: flex; align-items: center; color: #606266; }
.video-uploaded { display: flex; align-items: center; color: #606266; }
</style>
