<template>
  <div class="page-card" v-if="data">
    <el-page-header @back="$router.back()" :title="'返回'" :content="data.title" />
    <div style="margin-top: 24px;">
      <h2>{{ data.title }}</h2>
      <div style="display: flex; gap: 12px; margin: 12px 0;"><el-tag>{{ data.coursetype }}</el-tag><span style="color: var(--text-light); font-size: 14px;"><el-icon><View /></el-icon> {{ data.clicknum }}</span></div>
      <div style="background: #000; border-radius: 12px; overflow: hidden; margin-bottom: 20px;">
        <iframe v-if="bilibiliSrc" :src="bilibiliSrc" scrolling="no" frameborder="0" allowfullscreen
          style="width: 100%; height: 500px; display: block;" />
        <video v-else-if="data.videourl" :src="data.videourl" controls preload="auto"
          :poster="data.tupian || ''" style="width: 100%; max-height: 500px; display: block;" controlslist="nodownload">
          您的浏览器不支持视频播放
        </video>
        <div v-else style="height: 400px; display: flex; align-items: center; justify-content: center;">
          <div style="color: #999; font-size: 14px; text-align: center;"><el-icon :size="48" color="#666"><VideoPlay /></el-icon><p style="margin-top: 12px;">暂无视频</p></div>
        </div>
      </div>
      <div style="display: flex; gap: 12px; align-items: center; margin-bottom: 20px;">
        <el-button @click="thumbsup('thumbsup')" round><el-icon><Top /></el-icon>点赞 {{ data.thumbsup }}</el-button>
        <el-button @click="thumbsup('crazynum')" round><el-icon><Bottom /></el-icon>踩 {{ data.crazynum }}</el-button>
        <el-button @click="toggleCollect" :loading="collectLoading" round :type="collected ? 'warning' : ''"><el-icon><Star /></el-icon>{{ collected ? '已收藏' : '收藏' }} {{ data.storecount }}</el-button>
        <a v-if="bilibiliSrc" :href="data.videourl" target="_blank" rel="noopener"
          style="margin-left: auto; color: #00a1d6; font-size: 14px; text-decoration: none;">在B站打开观看</a>
      </div>
      <p style="color: var(--text-secondary); line-height: 1.8;">{{ data.content }}</p>
      <div style="margin-top: 12px; font-size: 13px; color: var(--text-light);">发布人：{{ data.faburenxingming }} · {{ data.addtime }}</div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../api/request'

const route = useRoute()
const data = ref(null)
const collected = ref(false)
const collectLoading = ref(false)

const bilibiliSrc = computed(() => {
  const url = data.value?.videourl || ''
  if (!url) return ''
  if (url.includes('player.bilibili.com')) {
    return url.startsWith('//') ? 'https:' + url : url
  }
  const bvMatch = url.match(/bilibili\.com\/video\/(BV[\w]+)/)
  if (bvMatch) return `https://player.bilibili.com/player.html?bvid=${bvMatch[1]}&autoplay=0`
  return ''
})

onMounted(async () => {
  const res = await request.get(`/video/detail/${route.params.id}`)
  data.value = res.data
  try {
    const c = await request.get('/collection/check', { params: { refid: route.params.id, tablename: 'video' } })
    collected.value = c.data
  } catch (e) {}
})

const thumbsup = async (type) => {
  try {
    await request.post(`/video/thumbsup/${data.value.id}`, null, { params: { type } })
    data.value[type]++
    ElMessage.success('操作成功')
  } catch (e) { ElMessage.error('操作失败，请先登录') }
}

const toggleCollect = async () => {
  collectLoading.value = true
  try {
    if (collected.value) {
      await request.delete('/collection/delete', { params: { refid: data.value.id, tablename: 'video' } })
      collected.value = false
      data.value.storecount = Math.max((data.value.storecount || 0) - 1, 0)
      ElMessage.success('取消收藏')
    } else {
      await request.post('/collection/save', { refid: data.value.id, tablename: 'video', name: data.value.title, picture: data.value.tupian, type: '学习视频' })
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
</script>
