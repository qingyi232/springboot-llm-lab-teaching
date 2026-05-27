<template>
  <div class="page-card" v-if="data">
    <el-page-header @back="$router.back()" :title="'返回'" :content="data.labName" />
    <div style="margin-top: 24px; display: flex; gap: 24px;">
      <img :src="data.tupian || 'https://images.unsplash.com/photo-1580894894513-541e068a3e2b?w=400'" style="width: 400px; height: 260px; object-fit: cover; border-radius: 12px;" />
      <div style="flex: 1;">
        <h2 style="margin-bottom: 12px;">{{ data.labName }}</h2>
        <div style="display: flex; gap: 8px; margin-bottom: 16px;">
          <el-tag>{{ data.shiyanshileixing }}</el-tag>
          <el-tag :type="data.zhuangtai === '空闲' ? 'success' : 'warning'">{{ data.zhuangtai }}</el-tag>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="实验室编号">{{ data.labId }}</el-descriptions-item>
          <el-descriptions-item label="位置">{{ data.weizhi }}</el-descriptions-item>
          <el-descriptions-item label="容纳人数">{{ data.rongna }}人</el-descriptions-item>
          <el-descriptions-item label="剩余座位">
            <span :style="{ color: data.shenyuzuowei > 0 ? '#10b981' : '#ef4444', fontWeight: 'bold' }">{{ data.shenyuzuowei ?? 0 }}人</span>
            <span style="color: #999; margin-left: 6px;">(已预约{{ data.yiyuyue ?? 0 }}人)</span>
          </el-descriptions-item>
          <el-descriptions-item label="管理员">{{ data.guanliyuan }}</el-descriptions-item>
          <el-descriptions-item label="开放时间" :span="2">{{ data.kaifangshijian }}</el-descriptions-item>
        </el-descriptions>
        <p style="margin-top: 16px; color: var(--text-secondary); line-height: 1.8;">{{ data.jianjie }}</p>
        <el-button v-if="userStore.role !== '实验室管理员'" type="primary" style="margin-top: 20px;" @click="showReserve = true" :disabled="data.zhuangtai !== '空闲'">
          <el-icon><Calendar /></el-icon>预约此实验室
        </el-button>
      </div>
    </div>
    <el-dialog v-model="showReserve" title="预约申请" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="预约人数"><el-input-number v-model="form.yuyuerenshu" :min="1" :max="200" placeholder="请输入预约人数" style="width: 100%;" /></el-form-item>
        <el-form-item label="预约说明"><el-input v-model="form.yuyueshuoming" type="textarea" :rows="3" placeholder="请输入预约用途" /></el-form-item>
        <el-form-item label="预约时间"><el-date-picker v-model="form.yuyueshijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="开始时间" style="width: 100%;" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.yuyuejieshu" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="结束时间" style="width: 100%;" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="showReserve = false">取消</el-button><el-button type="primary" @click="submitReserve">提交申请</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
const route = useRoute(); const userStore = useUserStore(); const data = ref(null); const showReserve = ref(false)
const form = reactive({ yuyuerenshu: 1, yuyueshuoming: '', yuyueshijian: '', yuyuejieshu: '' })
onMounted(async () => { const res = await request.get(`/labInfo/detail/${route.params.id}`); data.value = res.data })
const submitReserve = async () => {
  if (!form.yuyueshuoming || !form.yuyueshijian) { ElMessage.warning('请填写完整信息'); return }
  const u = userStore.userInfo
  await request.post('/reservation/add', { labId: data.value.labId, labName: data.value.labName, shiyanshileixing: data.value.shiyanshileixing, weizhi: data.value.weizhi, tupian: data.value.tupian, ...form, xingming: u.xingming || u.jiaoshixingming, xuehao: u.xuehao || u.gonghao, zhuanye: u.zhuanye || '', banji: u.banji || '' })
  ElMessage.success('预约申请已提交，请等待审核'); showReserve.value = false
}
</script>
