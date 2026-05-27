<template>
  <div>
    <div class="page-card">
      <div class="search-bar">
        <el-select v-model="search.shiyanshileixing" placeholder="实验室类型" clearable style="width: 180px;" @change="loadData">
          <el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" />
        </el-select>
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
      </div>
    </div>
    <div class="card-grid">
      <div class="content-card" v-for="lab in list" :key="lab.id" @click="$router.push('/lab/' + lab.id)">
        <img :src="lab.tupian || 'https://images.unsplash.com/photo-1580894894513-541e068a3e2b?w=400'" class="card-img" />
        <div class="card-body">
          <div class="card-title">{{ lab.labName }}</div>
          <div style="display: flex; gap: 8px; margin-bottom: 8px;">
            <el-tag size="small" type="info">{{ lab.shiyanshileixing }}</el-tag>
            <el-tag size="small" :type="lab.zhuangtai === '空闲' ? 'success' : 'warning'">{{ lab.zhuangtai }}</el-tag>
          </div>
          <div class="card-desc"><el-icon><Location /></el-icon> {{ lab.weizhi }} · 容纳{{ lab.rongna }}人</div>
          <div class="card-desc" style="margin-top: 4px;"><el-icon><Clock /></el-icon> {{ lab.kaifangshijian }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]); const types = ref([]); const search = reactive({ shiyanshileixing: '' })
const loadData = async () => { const res = await request.get('/labInfo/list', { params: search }); list.value = res.data || [] }
onMounted(async () => { const t = await request.get('/labType/list'); types.value = t.data || []; loadData() })
</script>
