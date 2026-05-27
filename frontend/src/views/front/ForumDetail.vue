<template>
  <div class="page-card" v-if="data">
    <el-page-header @back="$router.back()" :title="'返回'" />
    <div style="margin-top: 20px;">
      <h2>{{ data.title }}</h2>
      <div style="font-size: 13px; color: var(--text-light); margin: 8px 0 16px; display: flex; align-items: center; gap: 8px;">
        <el-avatar :size="24" :src="data.avatarurl" /> {{ data.username }} · {{ data.addtime }}
      </div>
      <p style="color: var(--text-secondary); line-height: 1.8; padding-bottom: 20px; border-bottom: 1px solid var(--border-light);">{{ data.content }}</p>
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 16px;">回复 ({{ replies.length }})</h4>
        <div v-for="r in replies" :key="r.id" style="display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid var(--border-light);">
          <el-avatar :size="32" :src="r.avatarurl" />
          <div><div style="font-size: 13px; color: var(--text-light); margin-bottom: 4px;">{{ r.username }} · {{ r.addtime?.substring(0, 16) }}</div><p style="font-size: 14px;">{{ r.content }}</p></div>
        </div>
        <div style="margin-top: 20px; display: flex; gap: 12px;">
          <el-input v-model="replyContent" placeholder="输入回复内容..." />
          <el-button type="primary" @click="handleReply">回复</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
const route = useRoute(); const userStore = useUserStore(); const data = ref(null); const replies = ref([]); const replyContent = ref('')
const loadData = async () => { const res = await request.get(`/forum/detail/${route.params.id}`); data.value = res.data; replies.value = res.replies || [] }
onMounted(() => loadData())
const handleReply = async () => {
  if (!replyContent.value) return
  const u = userStore.userInfo
  await request.post('/forum/save', { content: replyContent.value, parentid: String(route.params.id), username: u.xingming || u.jiaoshixingming || u.nickname, avatarurl: u.touxiang || u.image })
  replyContent.value = ''; ElMessage.success('回复成功'); loadData()
}
</script>
