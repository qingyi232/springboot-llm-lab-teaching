<template>
  <div class="page-card" v-if="data">
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
        <p style="color: var(--text-secondary); line-height: 1.8;">{{ data.content }}</p>
        <div style="margin-top: 16px; display: flex; gap: 12px;">
          <el-button type="primary" @click="toggleCollect" :loading="collectLoading" :icon="collected ? 'StarFilled' : 'Star'">
            {{ collected ? '已收藏' : '收藏' }}
          </el-button>
          <el-button v-if="data.fujian" @click="download"><el-icon><Download /></el-icon>下载附件</el-button>
        </div>
        <div style="margin-top: 16px; font-size: 13px; color: var(--text-light);">
          发布人：{{ data.faburenxingming }} · {{ data.addtime }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
const route = useRoute()
const data = ref(null)
const collected = ref(false)
const collectLoading = ref(false)

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
