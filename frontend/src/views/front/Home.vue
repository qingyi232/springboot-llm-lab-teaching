<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1>智慧实验教学<br/>开启高效学习之旅</h1>
        <p>基于SpringBoot和大模型技术，为您提供实验室预约、课程资源管理、AI智能辅导一站式服务</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round @click="$router.push('/lab')">
            <el-icon><Monitor /></el-icon>实验室预约
          </el-button>
          <el-button size="large" round @click="$router.push('/ai-chat')">
            <el-icon><ChatDotSquare /></el-icon>AI助手
          </el-button>
        </div>
      </div>
      <div class="hero-image">
        <img src="https://images.unsplash.com/photo-1562774053-701939374585?w=500" alt="campus" />
      </div>
    </div>

    <div class="section-title">
      <h2>公告通知</h2>
      <router-link to="/notice">查看更多 →</router-link>
    </div>
    <div class="notice-list">
      <div class="notice-item" v-for="n in notices" :key="n.id" @click="$router.push('/notice/' + n.id)">
        <el-tag :type="getNoticeType(n.typename)" size="small">{{ n.typename || '通知' }}</el-tag>
        <span class="notice-title">{{ n.title }}</span>
        <span class="notice-time">{{ n.addtime?.substring(0, 10) }}</span>
      </div>
    </div>

    <div class="section-title">
      <h2>热门课程资料</h2>
      <router-link to="/course-material">查看更多 →</router-link>
    </div>
    <div class="card-grid">
      <div class="content-card" v-for="item in materials" :key="item.id" @click="$router.push('/course-material/' + item.id)">
        <img :src="item.tupian || 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=400'" class="card-img" />
        <div class="card-body">
          <div class="card-title">{{ item.title }}</div>
          <div class="card-desc">{{ item.content }}</div>
        </div>
        <div class="card-footer">
          <span><el-icon><User /></el-icon> {{ item.faburenxingming }}</span>
          <span><el-icon><View /></el-icon> {{ item.clicknum }}</span>
        </div>
      </div>
    </div>

    <div class="section-title">
      <h2>推荐学习视频</h2>
      <router-link to="/video">查看更多 →</router-link>
    </div>
    <div class="card-grid">
      <div class="content-card" v-for="item in videos" :key="item.id" @click="$router.push('/video/' + item.id)">
        <div style="position: relative;">
          <img :src="item.tupian || 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400'" class="card-img" />
          <div style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 48px; height: 48px; background: rgba(0,0,0,0.5); border-radius: 50%; display: flex; align-items: center; justify-content: center;">
            <el-icon :size="24" color="white"><VideoPlay /></el-icon>
          </div>
        </div>
        <div class="card-body">
          <div class="card-title">{{ item.title }}</div>
          <div class="card-desc">{{ item.content }}</div>
        </div>
        <div class="card-footer">
          <span><el-icon><User /></el-icon> {{ item.faburenxingming }}</span>
          <div>
            <span style="margin-right: 12px;"><el-icon><View /></el-icon> {{ item.clicknum }}</span>
            <span><el-icon><Top /></el-icon> {{ item.thumbsup }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section-title">
      <h2>实验室一览</h2>
      <router-link to="/lab">查看更多 →</router-link>
    </div>
    <div class="card-grid">
      <div class="content-card" v-for="lab in labs" :key="lab.id" @click="$router.push('/lab/' + lab.id)">
        <img :src="lab.tupian || 'https://images.unsplash.com/photo-1580894894513-541e068a3e2b?w=400'" class="card-img" />
        <div class="card-body">
          <div class="card-title">{{ lab.labName }}</div>
          <div style="display: flex; gap: 8px; margin-bottom: 8px;">
            <el-tag size="small" type="info">{{ lab.shiyanshileixing }}</el-tag>
            <el-tag size="small" :type="lab.zhuangtai === '空闲' ? 'success' : 'warning'">{{ lab.zhuangtai }}</el-tag>
          </div>
          <div class="card-desc">{{ lab.weizhi }} · 容纳{{ lab.rongna }}人</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'

const notices = ref([])
const materials = ref([])
const videos = ref([])
const labs = ref([])

const getNoticeType = (type) => {
  const map = { '教务通知': '', '设备通知': 'warning', '安全通知': 'danger', '放假通知': 'info' }
  return map[type] || 'info'
}

onMounted(async () => {
  try {
    const [n, m, v, l] = await Promise.all([
      request.get('/notice/list'),
      request.get('/courseMaterial/front/list', { params: { limit: 4, sort: 'clicknum' } }),
      request.get('/video/front/list', { params: { limit: 4 } }),
      request.get('/labInfo/list'),
    ])
    notices.value = (n.data || []).slice(0, 4)
    materials.value = (m.data?.list || [])
    videos.value = (v.data?.list || [])
    labs.value = (l.data || []).slice(0, 4)
  } catch (e) {}
})
</script>

<style scoped>
.hero-section {
  display: flex;
  align-items: center;
  gap: 48px;
  padding: 48px 40px;
  background: linear-gradient(135deg, #ecfdf5 0%, #f0fdfa 100%);
  border-radius: 20px;
  margin-bottom: 40px;
}
.hero-content { flex: 1; }
.hero-content h1 {
  font-size: 36px;
  font-weight: 800;
  line-height: 1.3;
  color: var(--text-primary);
  margin-bottom: 16px;
}
.hero-content p {
  font-size: 15px;
  color: var(--text-secondary);
  line-height: 1.8;
  margin-bottom: 28px;
}
.hero-actions { display: flex; gap: 12px; }
.hero-image {
  width: 360px;
  flex-shrink: 0;
}
.hero-image img {
  width: 100%;
  height: 240px;
  object-fit: cover;
  border-radius: 16px;
  box-shadow: var(--shadow-lg);
}
.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  margin-top: 12px;
}
.section-title h2 { font-size: 20px; font-weight: 700; }
.section-title a { font-size: 14px; color: var(--primary); }
.notice-list {
  background: white;
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 32px;
  overflow: hidden;
}
.notice-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: background 0.2s;
}
.notice-item:last-child { border-bottom: none; }
.notice-item:hover { background: var(--bg-main); }
.notice-title { flex: 1; font-size: 14px; color: var(--text-primary); }
.notice-time { font-size: 12px; color: var(--text-light); }
</style>
