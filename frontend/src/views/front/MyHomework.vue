<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">作业提交</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="homeworkTitle" label="作业标题" min-width="200" />
      <el-table-column prop="content" label="提交内容" min-width="200" />
      <el-table-column prop="zhuangtai" label="状态" width="100">
        <template #default="{ row }"><el-tag :type="row.zhuangtai === '已批改' ? 'success' : ''" size="small">{{ row.zhuangtai }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="addtime" label="提交时间" width="180" />
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => { const res = await request.get('/homework/submit/mylist', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
</script>
