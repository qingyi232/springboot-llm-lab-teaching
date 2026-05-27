<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">作业成绩</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="homeworkTitle" label="作业标题" min-width="200" />
      <el-table-column prop="score" label="成绩" width="100"><template #default="{ row }"><el-tag :type="row.score >= 90 ? 'success' : row.score >= 60 ? '' : 'danger'">{{ row.score }}分</el-tag></template></el-table-column>
      <el-table-column prop="pingyuContent" label="教师评语" min-width="300" />
      <el-table-column prop="addtime" label="批改时间" width="180" />
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => { const res = await request.get('/homework/score/mylist', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
</script>
