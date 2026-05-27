<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @page-change="loadData" :showAdd="false" :showEdit="false">
    <template #search><el-input v-model="search.title" placeholder="标题" clearable style="width: 200px;" /></template>
    <el-table-column prop="title" label="标题" min-width="200" /><el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip /><el-table-column prop="username" label="发布人" width="120" /><el-table-column prop="addtime" label="发布时间" width="180" />
    <template #actions="{ row }"><el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button></template>
  </CrudTable>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const total = ref(0); const page = ref(1); const search = reactive({ title: '' })
const loadData = async () => { const res = await request.get('/forum/list', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(() => loadData())
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/forum/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
