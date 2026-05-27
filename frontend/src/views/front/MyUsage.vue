<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">使用记录</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="labName" label="实验室" width="200" />
      <el-table-column prop="shiyanshileixing" label="类型" width="140" />
      <el-table-column prop="shiyongrenshu" label="使用人数" width="100" />
      <el-table-column prop="shiyongjilu" label="使用记录" min-width="200" />
      <el-table-column prop="shiyongshijian" label="使用时间" width="180" />
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => {
  await request.post('/reservation/syncUsageRecords').catch(() => {})
  const res = await request.get('/usageRecord/mylist', { params: { page: page.value, limit: 10 } })
  list.value = res.data?.list || []
  total.value = res.data?.totalCount || 0
}
onMounted(() => loadData())
</script>
