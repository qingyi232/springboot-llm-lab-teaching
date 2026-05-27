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
          <h4 style="margin-bottom: 16px;">待审核预约</h4>
          <el-table :data="pendingReservations" size="small" stripe>
            <el-table-column prop="labName" label="实验室" min-width="140" />
            <el-table-column prop="xingming" label="申请人" width="100" />
            <el-table-column prop="yuyueshijian" label="预约时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="success" size="small" @click="approve(row)">通过</el-button>
                <el-button type="danger" size="small" @click="reject(row)">驳回</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="pendingReservations.length === 0" description="暂无待审核预约" :image-size="60" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="page-card">
          <h4 style="margin-bottom: 16px;">最近使用记录</h4>
          <el-table :data="recentUsage" size="small" stripe>
            <el-table-column prop="labName" label="实验室" min-width="140" />
            <el-table-column prop="xingming" label="使用人" width="100" />
            <el-table-column prop="shiyongshijian" label="使用日期" width="180" />
          </el-table>
          <el-empty v-if="recentUsage.length === 0" description="暂无使用记录" :image-size="60" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
const stats = ref([
  { label: '管辖实验室', value: 0, icon: 'OfficeBuilding', bg: '#ecfdf5', color: '#10b981' },
  { label: '待审核预约', value: 0, icon: 'Calendar', bg: '#fef3c7', color: '#f59e0b' },
  { label: '已通过预约', value: 0, icon: 'CircleCheck', bg: '#eff6ff', color: '#3b82f6' },
  { label: '使用记录', value: 0, icon: 'Tickets', bg: '#fce7f3', color: '#ec4899' },
])
const pendingReservations = ref([]); const recentUsage = ref([])
onMounted(async () => {
  try {
    const [l, r, u] = await Promise.all([
      request.get('/labInfo/page', { params: { page: 1, limit: 1 } }),
      request.get('/reservation/page', { params: { page: 1, limit: 50 } }),
      request.get('/usageRecord/page', { params: { page: 1, limit: 5 } }),
    ])
    stats.value[0].value = l.data?.totalCount || 0
    const all = r.data?.list || []
    const pending = all.filter(i => i.sfsh === '待审核')
    const approved = all.filter(i => i.sfsh === '已通过')
    stats.value[1].value = pending.length
    stats.value[2].value = approved.length
    pendingReservations.value = pending.slice(0, 5)
    stats.value[3].value = u.data?.totalCount || 0
    recentUsage.value = u.data?.list || []
  } catch (e) {}
})
const approve = async (row) => {
  await request.post('/reservation/sh', { id: row.id, sfsh: '已通过', shhf: '审核通过' })
  ElMessage.success('已通过'); row.sfsh = '已通过'; pendingReservations.value = pendingReservations.value.filter(r => r.id !== row.id)
  stats.value[1].value--; stats.value[2].value++
  const u = await request.get('/usageRecord/page', { params: { page: 1, limit: 5 } })
  recentUsage.value = u.data?.list || []
  stats.value[3].value = u.data?.totalCount || 0
}
const reject = async (row) => {
  await request.post('/reservation/sh', { id: row.id, sfsh: '已驳回', shhf: '不符合要求' })
  ElMessage.success('已驳回'); pendingReservations.value = pendingReservations.value.filter(r => r.id !== row.id)
  stats.value[1].value--
}
</script>
