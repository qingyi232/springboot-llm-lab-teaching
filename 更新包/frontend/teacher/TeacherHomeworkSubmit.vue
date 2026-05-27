<template>
  <div class="page-card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <h3 style="margin: 0;">学生作业提交列表</h3>
      <div style="display: flex; gap: 10px;">
        <el-select v-model="search.zhuangtai" placeholder="状态" clearable style="width: 120px;" @change="loadData">
          <el-option label="待批改" value="已提交" />
          <el-option label="已批改" value="已批改" />
        </el-select>
        <el-input v-model="search.homeworkTitle" placeholder="作业标题" clearable style="width: 180px;" />
        <el-input v-model="search.xingming" placeholder="学生姓名" clearable style="width: 150px;" />
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
      </div>
    </div>
    <el-table :data="list" stripe border size="default">
      <el-table-column prop="homeworkTitle" label="作业标题" min-width="180" />
      <el-table-column prop="xingming" label="学生" width="100" />
      <el-table-column prop="content" label="提交内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="fujian" label="附件" width="80">
        <template #default="{ row }"><el-link v-if="row.fujian" :href="row.fujian" target="_blank" type="primary">查看</el-link><span v-else>-</span></template>
      </el-table-column>
      <el-table-column prop="addtime" label="提交时间" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.zhuangtai === '已批改'" type="success" size="small">已批改</el-tag>
          <el-tag v-else type="warning" size="small">{{ row.zhuangtai || '已提交' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openScore(row)">{{ row.zhuangtai === '已批改' ? '查看' : '批改' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total > 10" style="margin-top: 16px; justify-content: flex-end;" :current-page="page" :page-size="10" :total="total" layout="total, prev, pager, next" @current-change="p => { page = p; loadData() }" />
  </div>
  <el-dialog v-model="scoreVisible" title="批改作业" width="500px">
    <el-form :model="scoreForm" label-width="80px">
      <el-form-item label="学生">{{ scoreForm.xingming }}</el-form-item>
      <el-form-item label="作业标题">{{ scoreForm.homeworkTitle }}</el-form-item>
      <el-form-item label="提交内容"><div style="white-space: pre-wrap; line-height: 1.6;">{{ scoreForm.content || '无' }}</div></el-form-item>
      <el-form-item v-if="scoreForm.fujian" label="附件"><el-link :href="scoreForm.fujian" target="_blank" type="primary">查看附件</el-link></el-form-item>
      <el-form-item label="分数"><el-input-number v-model="scoreForm.score" :min="0" :max="100" /></el-form-item>
      <el-form-item label="评语"><el-input v-model="scoreForm.pingyuContent" type="textarea" :rows="3" placeholder="请输入评语" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="scoreVisible = false">取消</el-button><el-button type="primary" @click="handleScore">提交批改</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
import { useUserStore } from '../../store'
const userStore = useUserStore()
const teacherName = userStore.userInfo.jiaoshixingming || userStore.userInfo.xingming || userStore.userInfo.username
const list = ref([]); const total = ref(0); const page = ref(1)
const scoreVisible = ref(false)
const scoreForm = reactive({ id: null, submitId: null, homeworkId: null, xingming: '', xuehao: '', userid: null, homeworkTitle: '', content: '', fujian: '', score: 0, pingyuContent: '' })
const search = reactive({ homeworkTitle: '', xingming: '', zhuangtai: '已提交' })
const loadData = async () => {
  const res = await request.get('/homework/submit/page', { params: { page: page.value, limit: 10, faburenxingming: teacherName, ...search } })
  list.value = res.data?.list || []; total.value = res.data?.totalCount || 0
}
onMounted(() => loadData())
const openScore = (row) => {
  Object.assign(scoreForm, { id: row.scoreId || null, submitId: row.id, homeworkId: row.homeworkId, xingming: row.xingming, xuehao: row.xuehao, userid: row.userid, homeworkTitle: row.homeworkTitle, content: row.content, fujian: row.fujian, score: row.score || 0, pingyuContent: row.pingyuContent || '' })
  scoreVisible.value = true
}
const handleScore = async () => {
  try {
    if (scoreForm.id) {
      await request.put('/homework/score/update', { id: scoreForm.id, score: scoreForm.score, pingyuContent: scoreForm.pingyuContent })
    } else {
      await request.post('/homework/score/save', { submitId: scoreForm.submitId, homeworkId: scoreForm.homeworkId, xingming: scoreForm.xingming, xuehao: scoreForm.xuehao, userid: scoreForm.userid, homeworkTitle: scoreForm.homeworkTitle, score: scoreForm.score, pingyuContent: scoreForm.pingyuContent })
    }
    ElMessage.success('批改成功')
    scoreVisible.value = false; loadData()
  } catch (e) { ElMessage.error('批改失败') }
}
</script>
