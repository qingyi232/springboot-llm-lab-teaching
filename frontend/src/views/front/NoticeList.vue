<template>
  <div class="page-card">
    <h3 style="margin-bottom: 20px;">公告信息</h3>
    <div class="notice-list">
      <div class="notice-item" v-for="n in list" :key="n.id" @click="$router.push('/notice/' + n.id)">
        <el-tag :type="n.typename?.includes('安全') ? 'danger' : n.typename?.includes('设备') ? 'warning' : 'info'" size="small">{{ n.typename || '通知' }}</el-tag>
        <span class="notice-title">{{ n.title }}</span>
        <span style="font-size: 12px; color: var(--text-light);"><el-icon><View /></el-icon> {{ n.clicknum }}</span>
        <span class="notice-time">{{ n.addtime?.substring(0, 10) }}</span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([])
onMounted(async () => { const res = await request.get('/notice/list'); list.value = res.data || [] })
</script>
<style scoped>
.notice-list { background: white; border-radius: 8px; }
.notice-item { display: flex; align-items: center; gap: 12px; padding: 14px 0; border-bottom: 1px solid var(--border-light); cursor: pointer; }
.notice-item:hover { background: var(--bg-main); }
.notice-item:last-child { border-bottom: none; }
.notice-title { flex: 1; font-size: 14px; }
.notice-time { font-size: 12px; color: var(--text-light); }
</style>
