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
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="14">
        <div class="page-card">
          <h4 style="margin-bottom: 20px;">各类数据统计</h4>
          <div class="bar-chart">
            <div class="bar-item" v-for="(b, i) in barData" :key="i">
              <div class="bar-label">{{ b.label }}</div>
              <div class="bar-track">
                <div class="bar-fill" :style="{ width: b.percent + '%', background: b.color }"></div>
              </div>
              <div class="bar-value">{{ b.value }}</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="page-card">
          <h4 style="margin-bottom: 20px;">预约状态分布</h4>
          <div class="donut-chart-wrapper">
            <div class="donut-chart" :style="donutStyle"></div>
            <div class="donut-legend">
              <div v-for="(item, i) in reservationStats" :key="i" class="legend-item">
                <span class="legend-dot" :style="{ background: item.color }"></span>
                <span>{{ item.label }}</span>
                <span style="margin-left: auto; font-weight: 600;">{{ item.value }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="page-card">
          <h4 style="margin-bottom: 16px;">最近预约申请</h4>
          <el-table :data="reservations" size="small" stripe>
            <el-table-column prop="labName" label="实验室" />
            <el-table-column prop="xingming" label="申请人" width="100" />
            <el-table-column prop="sfsh" label="状态" width="100"><template #default="{ row }"><el-tag :type="row.sfsh === '已通过' ? 'success' : row.sfsh === '待审核' ? 'warning' : 'danger'" size="small">{{ row.sfsh }}</el-tag></template></el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="page-card">
          <h4 style="margin-bottom: 16px;">最新公告</h4>
          <div v-for="n in notices" :key="n.id" style="padding: 10px 0; border-bottom: 1px solid var(--border-light); font-size: 14px;">
            <div style="display: flex; justify-content: space-between;"><span>{{ n.title }}</span><span style="color: var(--text-light); font-size: 12px;">{{ n.addtime?.substring(0, 10) }}</span></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../../api/request'
const stats = ref([
  { label: '学生人数', value: 0, icon: 'UserFilled', bg: '#ecfdf5', color: '#10b981' },
  { label: '教师人数', value: 0, icon: 'Reading', bg: '#eff6ff', color: '#3b82f6' },
  { label: '实验室数量', value: 0, icon: 'Monitor', bg: '#fef3c7', color: '#f59e0b' },
  { label: '待审核预约', value: 0, icon: 'Calendar', bg: '#fce7f3', color: '#ec4899' },
])
const reservations = ref([]); const notices = ref([])
const barData = ref([])
const reservationStats = ref([
  { label: '待审核', value: 0, color: '#f59e0b' },
  { label: '已通过', value: 0, color: '#10b981' },
  { label: '已驳回', value: 0, color: '#ef4444' },
])
const donutStyle = computed(() => {
  const total = reservationStats.value.reduce((s, i) => s + i.value, 0) || 1
  const p1 = (reservationStats.value[0].value / total) * 100
  const p2 = (reservationStats.value[1].value / total) * 100
  return {
    background: `conic-gradient(#f59e0b 0% ${p1}%, #10b981 ${p1}% ${p1 + p2}%, #ef4444 ${p1 + p2}% 100%)`
  }
})
onMounted(async () => {
  try {
    const [s, t, l, r, n, cm, v, hw, f] = await Promise.all([
      request.get('/student/page', { params: { page: 1, limit: 1 } }),
      request.get('/teacher/page', { params: { page: 1, limit: 1 } }),
      request.get('/labInfo/page', { params: { page: 1, limit: 1 } }),
      request.get('/reservation/page', { params: { page: 1, limit: 100 } }),
      request.get('/notice/page', { params: { page: 1, limit: 5 } }),
      request.get('/courseMaterial/page', { params: { page: 1, limit: 1 } }),
      request.get('/video/page', { params: { page: 1, limit: 1 } }),
      request.get('/homework/page', { params: { page: 1, limit: 1 } }),
      request.get('/forum/page', { params: { page: 1, limit: 1 } }),
    ])
    const studentCount = s.data?.totalCount || 0
    const teacherCount = t.data?.totalCount || 0
    const labCount = l.data?.totalCount || 0
    const cmCount = cm.data?.totalCount || 0
    const vCount = v.data?.totalCount || 0
    const hwCount = hw.data?.totalCount || 0
    const fCount = f.data?.totalCount || 0
    stats.value[0].value = studentCount
    stats.value[1].value = teacherCount
    stats.value[2].value = labCount
    const allRes = r.data?.list || []
    const pending = allRes.filter(i => i.sfsh === '待审核')
    const approved = allRes.filter(i => i.sfsh === '已通过')
    const rejected = allRes.filter(i => i.sfsh === '已驳回')
    stats.value[3].value = pending.length
    reservations.value = allRes.slice(0, 5)
    notices.value = n.data?.list || []
    reservationStats.value[0].value = pending.length
    reservationStats.value[1].value = approved.length
    reservationStats.value[2].value = rejected.length
    const maxVal = Math.max(studentCount, teacherCount, labCount, cmCount, vCount, hwCount, fCount, 1)
    barData.value = [
      { label: '学生', value: studentCount, percent: (studentCount / maxVal) * 100, color: '#10b981' },
      { label: '教师', value: teacherCount, percent: (teacherCount / maxVal) * 100, color: '#3b82f6' },
      { label: '实验室', value: labCount, percent: (labCount / maxVal) * 100, color: '#f59e0b' },
      { label: '课程资料', value: cmCount, percent: (cmCount / maxVal) * 100, color: '#8b5cf6' },
      { label: '学习视频', value: vCount, percent: (vCount / maxVal) * 100, color: '#ec4899' },
      { label: '作业', value: hwCount, percent: (hwCount / maxVal) * 100, color: '#06b6d4' },
      { label: '论坛帖子', value: fCount, percent: (fCount / maxVal) * 100, color: '#f97316' },
    ]
  } catch (e) {}
})
</script>
<style scoped>
.bar-chart { display: flex; flex-direction: column; gap: 14px; }
.bar-item { display: flex; align-items: center; gap: 12px; }
.bar-label { width: 65px; font-size: 13px; color: #64748b; text-align: right; flex-shrink: 0; }
.bar-track { flex: 1; height: 22px; background: #f1f5f9; border-radius: 11px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 11px; transition: width 0.8s ease; min-width: 4px; }
.bar-value { width: 36px; font-size: 14px; font-weight: 600; color: #334155; flex-shrink: 0; }
.donut-chart-wrapper { display: flex; align-items: center; gap: 30px; justify-content: center; padding: 10px 0; }
.donut-chart { width: 130px; height: 130px; border-radius: 50%; position: relative; flex-shrink: 0; }
.donut-chart::after { content: ''; position: absolute; top: 30px; left: 30px; width: 70px; height: 70px; background: white; border-radius: 50%; }
.donut-legend { display: flex; flex-direction: column; gap: 12px; }
.legend-item { display: flex; align-items: center; gap: 8px; font-size: 13px; min-width: 120px; }
.legend-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
</style>
