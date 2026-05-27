<template>
  <div class="page-card" v-if="data">
    <el-page-header @back="$router.back()" :title="'返回'" />
    <div style="margin-top: 20px; text-align: center; padding-bottom: 20px; border-bottom: 1px solid var(--border-light);">
      <h2>{{ data.title }}</h2>
      <div style="font-size: 13px; color: var(--text-light); margin-top: 8px;">{{ data.name }} · {{ data.addtime }} · {{ data.clicknum }}次浏览</div>
    </div>
    <div style="margin-top: 20px; line-height: 1.8; color: var(--text-secondary);" v-html="data.introduction?.replace(/\n/g, '<br/>')"></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../api/request'
const route = useRoute(); const data = ref(null)
onMounted(async () => { const res = await request.get(`/notice/detail/${route.params.id}`); data.value = res.data })
</script>
