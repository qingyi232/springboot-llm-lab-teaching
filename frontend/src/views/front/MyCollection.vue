<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">我的收藏</h3>
    <el-table :data="list" stripe>
      <el-table-column label="图片" width="80"><template #default="{ row }"><el-avatar :size="48" :src="row.picture" shape="square" /></template></el-table-column>
      <el-table-column prop="name" label="名称" min-width="200" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column prop="addtime" label="收藏时间" width="180" />
      <el-table-column label="操作" width="100"><template #default="{ row }"><el-button type="danger" size="small" @click="handleDelete(row)">取消</el-button></template></el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => { const res = await request.get('/collection/page', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleDelete = async (row) => { await request.delete('/collection/delete', { params: { refid: row.refid, tablename: row.tablename } }); ElMessage.success('取消收藏'); loadData() }
</script>
