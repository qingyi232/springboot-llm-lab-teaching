<template>
  <div>
    <div class="page-card">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h3>论坛交流</h3>
        <el-button type="primary" @click="showPost = true"><el-icon><Plus /></el-icon>发布帖子</el-button>
      </div>
      <div class="forum-list">
        <div class="forum-item" v-for="item in list" :key="item.id" @click="$router.push('/forum/' + item.id)">
          <el-avatar :size="40" :src="item.avatarurl || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'" />
          <div style="flex: 1;">
            <div style="font-weight: 600; margin-bottom: 4px;">{{ item.title }}</div>
            <div style="font-size: 13px; color: var(--text-secondary); display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">{{ item.content }}</div>
            <div style="font-size: 12px; color: var(--text-light); margin-top: 8px;">{{ item.username }} · {{ item.addtime?.substring(0, 10) }}</div>
          </div>
        </div>
      </div>
      <div style="text-align: center; margin-top: 16px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="10" v-model:current-page="page" @current-change="loadData" /></div>
    </div>
    <el-dialog v-model="showPost" title="发布帖子" width="500px">
      <el-form label-width="60px">
        <el-form-item label="标题"><el-input v-model="postForm.title" placeholder="请输入标题" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="postForm.content" type="textarea" :rows="5" placeholder="请输入内容" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="showPost = false">取消</el-button><el-button type="primary" @click="handlePost">发布</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
const userStore = useUserStore(); const list = ref([]); const total = ref(0); const page = ref(1); const showPost = ref(false)
const postForm = reactive({ title: '', content: '' })
const loadData = async () => { const res = await request.get('/forum/list', { params: { page: page.value, limit: 10 } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handlePost = async () => {
  if (!postForm.title || !postForm.content) { ElMessage.warning('请填写完整信息'); return }
  const u = userStore.userInfo
  await request.post('/forum/save', { ...postForm, username: u.xingming || u.jiaoshixingming || u.nickname || u.username, avatarurl: u.touxiang || u.image })
  ElMessage.success('发布成功'); showPost.value = false; postForm.title = ''; postForm.content = ''; loadData()
}
</script>
<style scoped>
.forum-item { display: flex; gap: 14px; padding: 16px; border-bottom: 1px solid var(--border-light); cursor: pointer; transition: background 0.2s; }
.forum-item:hover { background: var(--bg-main); }
.forum-item:last-child { border-bottom: none; }
</style>
