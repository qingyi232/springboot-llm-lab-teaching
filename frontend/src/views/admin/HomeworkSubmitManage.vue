<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @page-change="loadData" :showAdd="false" :showEdit="false">
    <template #search><el-input v-model="search.xingming" placeholder="学生姓名" clearable style="width: 200px;" /></template>
    <el-table-column prop="homeworkTitle" label="作业标题" min-width="200" /><el-table-column prop="xingming" label="学生" width="100" /><el-table-column prop="xuehao" label="学号" width="120" /><el-table-column prop="content" label="提交内容" min-width="200" show-overflow-tooltip /><el-table-column prop="zhuangtai" label="状态" width="100"><template #default="{ row }"><el-tag :type="row.zhuangtai === '已批改' ? 'success' : ''" size="small">{{ row.zhuangtai }}</el-tag></template></el-table-column><el-table-column prop="addtime" label="提交时间" width="180" />
    <template #actions="{ row }"><el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button></template>
  </CrudTable>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const search = reactive({ xingming: '' })
const loadData = async () => { const res = await request.get('/homework/submit/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/homework/submit/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
