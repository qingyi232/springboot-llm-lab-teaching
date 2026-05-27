<template>
  <div class="page-card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <h3 style="margin: 0;">作业评分管理</h3>
      <div style="display: flex; gap: 10px;">
        <el-input v-model="search.xingming" placeholder="学生姓名" clearable style="width: 150px;" />
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
      </div>
    </div>
    <el-table :data="list" stripe border size="default">
      <el-table-column prop="homeworkTitle" label="作业标题" min-width="180" />
      <el-table-column prop="xingming" label="学生" width="100" />
      <el-table-column prop="score" label="分数" width="100">
        <template #default="{ row }"><span v-if="row.score" style="font-weight: 600; color: var(--primary);">{{ row.score }}</span><span v-else style="color: #999;">未评</span></template>
      </el-table-column>
      <el-table-column prop="pingyuContent" label="评语" min-width="200" show-overflow-tooltip />
      <el-table-column prop="addtime" label="评分时间" width="180" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openScore(row)">{{ row.score ? '修改' : '评分' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total > 10" style="margin-top: 16px; justify-content: flex-end;" :current-page="page" :page-size="10" :total="total" layout="total, prev, pager, next" @current-change="p => { page = p; loadData() }" />
  </div>
  <el-dialog v-model="scoreVisible" title="评分" width="450px">
    <el-form :model="scoreForm" label-width="60px">
      <el-form-item label="学生">{{ scoreForm.studentName }}</el-form-item>
      <el-form-item label="作业">{{ scoreForm.homeworkTitle }}</el-form-item>
      <el-form-item label="分数"><el-input-number v-model="scoreForm.score" :min="0" :max="100" /></el-form-item>
      <el-form-item label="评语"><el-input v-model="scoreForm.pingyuContent" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="scoreVisible = false">取消</el-button><el-button type="primary" @click="handleScore">提交评分</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1); const scoreVisible = ref(false)
const search = reactive({ xingming: '' })
const scoreForm = reactive({ id: null, studentName: '', homeworkTitle: '', score: 0, pingyuContent: '' })
const loadData = async () => {
  const res = await request.get('/homework/score/page', { params: { page: page.value, limit: 10, ...search } })
  list.value = res.data?.list || []; total.value = res.data?.totalCount || 0
}
onMounted(() => loadData())
const openScore = (row) => { Object.assign(scoreForm, { id: row.id, studentName: row.xingming, homeworkTitle: row.homeworkTitle, score: row.score || 0, pingyuContent: row.pingyuContent || '' }); scoreVisible.value = true }
const handleScore = async () => {
  if (scoreForm.id) {
    await request.put('/homework/score/update', { ...scoreForm })
  } else {
    await request.post('/homework/score/save', { ...scoreForm })
  }
  ElMessage.success('评分成功'); scoreVisible.value = false; loadData()
}
</script>
