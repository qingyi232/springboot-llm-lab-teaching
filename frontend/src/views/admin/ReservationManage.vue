<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @page-change="loadData" :showAdd="false" :showEdit="false" :actionWidth="200">
    <template #search><el-input v-model="search.labName" placeholder="实验室" clearable style="width: 160px;" /><el-input v-model="search.xingming" placeholder="申请人" clearable style="width: 140px;" /><el-select v-model="search.sfsh" placeholder="状态" clearable style="width: 140px;"><el-option label="待审核" value="待审核" /><el-option label="已通过" value="已通过" /><el-option label="未通过" value="未通过" /></el-select></template>
    <el-table-column prop="labName" label="实验室" width="180" /><el-table-column prop="xingming" label="申请人" width="100" /><el-table-column prop="xuehao" label="学号/工号" width="120" /><el-table-column prop="zhuanye" label="专业" width="120" /><el-table-column prop="yuyuerenshu" label="人数" width="80" /><el-table-column prop="yuyueshijian" label="预约时间" width="170" /><el-table-column prop="yuyueshuoming" label="说明" min-width="150" show-overflow-tooltip />
    <el-table-column prop="sfsh" label="状态" width="100"><template #default="{ row }"><el-tag :type="row.sfsh === '已通过' ? 'success' : row.sfsh === '待审核' ? 'warning' : 'danger'" size="small">{{ row.sfsh }}</el-tag></template></el-table-column>
    <template #actions="{ row }">
      <template v-if="row.sfsh === '待审核'">
        <el-button type="success" size="small" @click="handleSh(row, '已通过')">通过</el-button>
        <el-button type="danger" size="small" @click="handleSh(row, '未通过')">拒绝</el-button>
      </template>
      <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
    </template>
  </CrudTable>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const search = reactive({ labName: '', xingming: '', sfsh: '' })
const loadData = async () => { const res = await request.get('/reservation/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleSh = async (row, status) => {
  const { value } = await ElMessageBox.prompt(`请输入审核回复`, '审核', { inputValue: status === '已通过' ? '申请已通过，请按时到达' : '抱歉，实验室暂不可用', confirmButtonText: '确定' })
  await request.post('/reservation/sh', { id: row.id, sfsh: status, shhf: value }); ElMessage.success('审核完成'); loadData()
}
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/reservation/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
