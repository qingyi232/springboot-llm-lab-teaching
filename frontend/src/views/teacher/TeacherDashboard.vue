<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6" v-for="(s, i) in stats" :key="i">
        <div class="stat-card">
          <div class="stat-icon" :style="{ background: s.bg }"><el-icon :size="24" :color="s.color"><component :is="s.icon" /></el-icon></div>
          <div class="stat-info"><div class="label">{{ s.label }}</div><div class="value">{{ s.value }}</div></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="page-card">
          <h4 style="margin-bottom: 16px;">最近布置的作业</h4>
          <el-table :data="recentHomework" size="small" stripe>
            <el-table-column prop="title" label="作业标题" min-width="200" />
            <el-table-column prop="coursetype" label="课程类型" width="120" />
            <el-table-column prop="addtime" label="发布时间" width="180" />
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="page-card">
          <h4 style="margin-bottom: 16px;">待批改的作业提交</h4>
          <el-table :data="pendingSubmits" size="small" stripe>
            <el-table-column prop="homeworkTitle" label="作业标题" min-width="160" />
            <el-table-column prop="xingming" label="学生" width="100" />
            <el-table-column prop="addtime" label="提交时间" width="180" />
          </el-table>
          <el-empty v-if="pendingSubmits.length === 0" description="暂无待批改提交" :image-size="60" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { useUserStore } from '../../store'
const userStore = useUserStore()
const teacherName = userStore.userInfo.jiaoshixingming || userStore.userInfo.xingming || userStore.userInfo.username
const stats = ref([
  { label: '我的课程资料', value: 0, icon: 'Document', bg: '#ecfdf5', color: '#10b981' },
  { label: '我的学习视频', value: 0, icon: 'VideoCamera', bg: '#eff6ff', color: '#3b82f6' },
  { label: '布置的作业', value: 0, icon: 'Edit', bg: '#fef3c7', color: '#f59e0b' },
  { label: '待批改提交', value: 0, icon: 'Tickets', bg: '#fce7f3', color: '#ec4899' },
])
const recentHomework = ref([]); const pendingSubmits = ref([])
onMounted(async () => {
  try {
    const [cm, v, hw, hs] = await Promise.all([
      request.get('/courseMaterial/page', { params: { page: 1, limit: 1, faburenxingming: teacherName } }),
      request.get('/video/page', { params: { page: 1, limit: 1, faburenxingming: teacherName } }),
      request.get('/homework/page', { params: { page: 1, limit: 5, faburenxingming: teacherName } }),
      request.get('/homework/submit/page', { params: { page: 1, limit: 100, faburenxingming: teacherName } }),
    ])
    stats.value[0].value = cm.data?.totalCount || 0
    stats.value[1].value = v.data?.totalCount || 0
    stats.value[2].value = hw.data?.totalCount || 0
    recentHomework.value = hw.data?.list || []
    const allSubmits = hs.data?.list || []
    pendingSubmits.value = allSubmits.filter(s => s.zhuangtai !== '已批改')
    stats.value[3].value = pendingSubmits.value.length
  } catch (e) {}
})
</script>
