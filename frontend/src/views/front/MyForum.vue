<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">我的发布</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
      <el-table-column prop="addtime" label="发布时间" width="180" />
      <el-table-column label="操作" width="100"><template #default="{ row }"><el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => { const res = await request.get('/forum/mylist', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/forum/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
