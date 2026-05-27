<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">我的预约</h3>
    <el-table :data="list" stripe>
      <el-table-column prop="labName" label="实验室" width="200" />
      <el-table-column prop="shiyanshileixing" label="类型" width="140" />
      <el-table-column prop="weizhi" label="位置" width="150" />
      <el-table-column prop="yuyuerenshu" label="人数" width="80" />
      <el-table-column prop="yuyueshijian" label="预约时间" width="180" />
      <el-table-column prop="sfsh" label="状态" width="100">
        <template #default="{ row }"><el-tag :type="row.sfsh === '已通过' ? 'success' : row.sfsh === '已取消' ? 'info' : row.sfsh === '未通过' ? 'danger' : 'warning'" size="small">{{ row.sfsh }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="shhf" label="审核回复" min-width="200" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }"><el-button v-if="row.sfsh === '待审核'" type="danger" size="small" @click="handleCancel(row)">取消</el-button></template>
      </el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const loadData = async () => { const res = await request.get('/reservation/mylist', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleCancel = async (row) => { await ElMessageBox.confirm('确定取消此预约？'); await request.post(`/reservation/cancel/${row.id}`); ElMessage.success('已取消'); loadData() }
</script>
