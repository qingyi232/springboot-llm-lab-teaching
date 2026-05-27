<template>
  <div v-if="data">
    <div class="page-card">
      <el-page-header @back="$router.back()" :title="'返回'" :content="data.title" />
      <div style="margin-top: 24px; display: flex; gap: 24px;">
        <img :src="data.tupian || 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=400'" style="width: 320px; height: 200px; object-fit: cover; border-radius: 12px;" />
        <div style="flex: 1;">
          <h2 style="margin-bottom: 12px;">{{ data.title }}</h2>
          <div style="display: flex; gap: 12px; margin-bottom: 16px;">
            <el-tag>{{ data.coursetype }}</el-tag>
            <span style="color: var(--text-light); font-size: 14px;"><el-icon><View /></el-icon> {{ data.clicknum }} 次浏览</span>
            <span style="color: var(--text-light); font-size: 14px;"><el-icon><Star /></el-icon> {{ data.storecount }} 次收藏</span>
          </div>
          <div style="display: flex; gap: 12px;">
            <el-button type="primary" @click="toggleCollect" :loading="collectLoading" :icon="collected ? 'StarFilled' : 'Star'">
              {{ collected ? '已收藏' : '收藏' }}
            </el-button>
            <el-button v-if="data.fujian" @click="download"><el-icon><Download /></el-icon>下载附件</el-button>
            <span v-else style="color: var(--text-light); font-size: 14px; line-height: 32px;">暂无附件</span>
          </div>
          <div style="margin-top: 16px; font-size: 13px; color: var(--text-light);">
            发布人：{{ data.faburenxingming }} · {{ data.addtime }}
          </div>
        </div>
      </div>
    </div>
    <div class="page-card detail-content" style="margin-top: 20px;">
      <div v-html="renderedContent" class="rich-content"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { marked } from 'marked'
import request from '../../api/request'
const route = useRoute()
const data = ref(null)
const collected = ref(false)
const collectLoading = ref(false)

const renderedContent = computed(() => {
  if (!data.value?.content) return ''
  return marked(data.value.content)
})

const loadDetail = async () => {
  const res = await request.get(`/courseMaterial/detail/${route.params.id}`)
  data.value = res.data
}

onMounted(async () => {
  await loadDetail()
  try {
    const c = await request.get('/collection/check', { params: { refid: route.params.id, tablename: 'course_material' } })
    collected.value = c.data
  } catch (e) {}
})

const toggleCollect = async () => {
  collectLoading.value = true
  try {
    if (collected.value) {
      await request.delete('/collection/delete', { params: { refid: data.value.id, tablename: 'course_material' } })
      collected.value = false
      data.value.storecount = Math.max((data.value.storecount || 0) - 1, 0)
      ElMessage.success('取消收藏')
    } else {
      await request.post('/collection/save', { refid: data.value.id, tablename: 'course_material', name: data.value.title, picture: data.value.tupian, type: '课程资料' })
      collected.value = true
      data.value.storecount = (data.value.storecount || 0) + 1
      ElMessage.success('收藏成功')
    }
  } catch (e) {
    ElMessage.error('操作失败，请先登录')
  } finally {
    collectLoading.value = false
  }
}
const download = () => { if (data.value.fujian) window.open(data.value.fujian) }
</script>

<style scoped>
.detail-content {
  min-height: 200px;
}
.rich-content :deep(h1),
.rich-content :deep(h2),
.rich-content :deep(h3),
.rich-content :deep(h4) {
  margin-top: 28px;
  margin-bottom: 12px;
  font-weight: 700;
  color: var(--text-primary);
}
.rich-content :deep(h2) { font-size: 20px; border-bottom: 2px solid var(--primary); padding-bottom: 8px; display: inline-block; }
.rich-content :deep(h3) { font-size: 17px; }
.rich-content :deep(p) { line-height: 1.8; color: var(--text-secondary); margin-bottom: 12px; }
.rich-content :deep(ul),
.rich-content :deep(ol) { padding-left: 20px; margin-bottom: 16px; }
.rich-content :deep(li) { line-height: 2; color: var(--text-secondary); }
.rich-content :deep(a) { color: var(--primary); text-decoration: underline; }
.rich-content :deep(img) { max-width: 100%; border-radius: 8px; margin: 12px 0; }
.rich-content :deep(pre) { background: #f5f7fa; padding: 16px; border-radius: 8px; overflow-x: auto; margin: 12px 0; }
.rich-content :deep(code) { background: #f5f7fa; padding: 2px 6px; border-radius: 4px; font-size: 14px; }
.rich-content :deep(blockquote) { border-left: 4px solid var(--primary); padding-left: 16px; margin: 12px 0; color: var(--text-light); }
.rich-content :deep(table) { width: 100%; border-collapse: collapse; margin: 12px 0; }
.rich-content :deep(th),
.rich-content :deep(td) { border: 1px solid var(--border-light); padding: 8px 12px; text-align: left; }
.rich-content :deep(th) { background: #f5f7fa; font-weight: 600; }
</style>
