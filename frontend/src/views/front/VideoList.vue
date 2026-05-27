<template>
  <div>
    <div class="page-card">
      <div class="search-bar">
        <el-input v-model="search.title" placeholder="搜索视频名称" clearable style="width: 240px;" />
        <el-select v-model="search.coursetype" placeholder="课程类型" clearable style="width: 160px;" @change="loadData">
          <el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" />
        </el-select>
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
      </div>
    </div>
    <div class="card-grid">
      <div class="content-card" v-for="item in list" :key="item.id" @click="$router.push('/video/' + item.id)">
        <div style="position: relative;"><img :src="item.tupian || 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400'" class="card-img" /><div style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 48px; height: 48px; background: rgba(0,0,0,0.5); border-radius: 50%; display: flex; align-items: center; justify-content: center;"><el-icon :size="24" color="white"><VideoPlay /></el-icon></div></div>
        <div class="card-body"><div class="card-title">{{ item.title }}</div><el-tag size="small" type="info">{{ item.coursetype }}</el-tag><div class="card-desc" style="margin-top: 8px;">{{ item.content }}</div></div>
        <div class="card-footer"><span><el-icon><User /></el-icon> {{ item.faburenxingming }}</span><div><span style="margin-right: 8px;"><el-icon><View /></el-icon> {{ item.clicknum }}</span><span><el-icon><Top /></el-icon> {{ item.thumbsup }}</span></div></div>
      </div>
    </div>
    <div style="text-align: center; margin-top: 20px;"><el-pagination background layout="prev, pager, next" :total="total" :page-size="12" v-model:current-page="page" @current-change="loadData" /></div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]); const types = ref([]); const total = ref(0); const page = ref(1)
const search = reactive({ title: '', coursetype: '' })
const loadData = async () => { const res = await request.get('/video/front/list', { params: { page: page.value, limit: 12, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => { const t = await request.get('/courseType/list'); types.value = t.data || []; loadData() })
</script>
