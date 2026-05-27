<template>
  <div class="page-card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <h3 style="margin: 0;">预约审核管理</h3>
      <div style="display: flex; gap: 10px;">
        <el-select v-model="search.sfsh" clearable placeholder="状态筛选" style="width: 140px;">
          <el-option label="待审核" value="待审核" />
          <el-option label="已通过" value="已通过" />
          <el-option label="已驳回" value="已驳回" />
        </el-select>
        <el-input v-model="search.labName" placeholder="实验室名称" clearable style="width: 180px;" />
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
      </div>
    </div>
    <el-table :data="list" stripe border size="default">
      <el-table-column prop="labName" label="实验室" min-width="140" />
      <el-table-column prop="xingming" label="申请人" width="100" />
      <el-table-column prop="yuyueshijian" label="预约开始" width="180" />
      <el-table-column prop="yuyuejieshu" label="预约结束" width="180" />
      <el-table-column prop="yuyuerenshu" label="人数" width="80" />
      <el-table-column prop="yuyueshuoming" label="用途" min-width="160" show-overflow-tooltip />
      <el-table-column prop="sfsh" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.sfsh === '已通过' ? 'success' : row.sfsh === '待审核' ? 'warning' : 'danger'" size="small">{{ row.sfsh }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="shhf" label="审核回复" min-width="120" show-overflow-tooltip />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <template v-if="row.sfsh === '待审核'">
            <el-button type="success" size="small" @click="handleApprove(row, '已通过')">通过</el-button>
            <el-button type="danger" size="small" @click="openReject(row)">驳回</el-button>
          </template>
          <span v-else style="color: #999; font-size: 13px;">已处理</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total > 10" style="margin-top: 16px; justify-content: flex-end;" :current-page="page" :page-size="10" :total="total" layout="total, prev, pager, next" @current-change="p => { page = p; loadData() }" />
  </div>
  <el-dialog v-model="rejectVisible" title="驳回原因" width="400px">
    <el-input v-model="rejectReason" type="textarea" :rows="3" placeholder="请输入驳回原因（选填）" />
    <template #footer><el-button @click="rejectVisible = false">取消</el-button><el-button type="danger" @click="confirmReject">确认驳回</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
const list = ref([]); const total = ref(0); const page = ref(1)
const search = reactive({ sfsh: '', labName: '' })
const rejectVisible = ref(false); const rejectReason = ref(''); const rejectRow = ref(null)
const loadData = async () => {
  const res = await request.get('/reservation/page', { params: { page: page.value, limit: 10, ...search } })
  list.value = res.data?.list || []; total.value = res.data?.totalCount || 0
}
onMounted(() => loadData())
const handleApprove = async (row, status) => {
  await request.post('/reservation/sh', { id: row.id, sfsh: status, shhf: '审核通过' })
  ElMessage.success('已通过'); loadData()
}
const openReject = (row) => { rejectRow.value = row; rejectReason.value = ''; rejectVisible.value = true }
const confirmReject = async () => {
  await request.post('/reservation/sh', { id: rejectRow.value.id, sfsh: '已驳回', shhf: rejectReason.value || '不符合要求' })
  ElMessage.success('已驳回'); rejectVisible.value = false; loadData()
}
</script>
