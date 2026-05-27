<template>
  <CrudTable :data="list" :total="total" v-model:page="page" @search="loadData" @add="openDialog()" @edit="openDialog($event)" @del="handleDelete($event)" @page-change="loadData">
    <template #search><el-input v-model="search.title" placeholder="标题" clearable style="width: 200px;" /></template>
    <el-table-column prop="title" label="标题" min-width="200" />
    <el-table-column prop="coursetype" label="课程类型" width="140" />
    <el-table-column prop="banji" label="指定班级" width="140">
      <template #default="{ row }">{{ row.banji || '全部班级' }}</template>
    </el-table-column>
    <el-table-column prop="faburenxingming" label="发布人" width="100" />
    <el-table-column prop="jiezhishijian" label="截止时间" width="180" />
    <el-table-column prop="addtime" label="发布时间" width="180" />
  </CrudTable>
  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑' : '添加'" width="600px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="课程类型"><el-select v-model="form.coursetype" style="width: 100%;"><el-option v-for="t in types" :key="t.typename" :label="t.typename" :value="t.typename" /></el-select></el-form-item>
      <el-form-item label="指定班级">
        <el-select v-model="form.banji" clearable placeholder="全部班级（不选则所有学生可见）" style="width: 100%;">
          <el-option v-for="b in banjiList" :key="b" :label="b" :value="b" />
        </el-select>
      </el-form-item>
      <el-form-item label="发布人"><el-input v-model="form.faburenxingming" /></el-form-item>
      <el-form-item label="截止时间"><el-date-picker v-model="form.jiezhishijian" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%;" /></el-form-item>
      <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import CrudTable from '../../components/CrudTable.vue'
const list = ref([]); const types = ref([]); const banjiList = ref([]); const total = ref(0); const page = ref(1); const dialogVisible = ref(false); const isEdit = ref(false); const search = reactive({ title: '' }); const form = reactive({})
const loadData = async () => { const res = await request.get('/homework/page', { params: { page: page.value, limit: 10, ...search } }); list.value = res.data?.list || []; total.value = res.data?.totalCount || 0 }
onMounted(async () => {
  const [t, b] = await Promise.all([request.get('/courseType/list'), request.get('/student/banjiList')])
  types.value = t.data || []
  banjiList.value = b.data || []
  loadData()
})
const openDialog = (row) => { isEdit.value = !!row; Object.assign(form, row || { id: null, title: '', coursetype: '', banji: '', content: '', jiezhishijian: '', faburenxingming: '' }); dialogVisible.value = true }
const handleSave = async () => { if (!form.banji) form.banji = ''; if (isEdit.value) await request.put('/homework/update', form); else await request.post('/homework/save', form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handleDelete = async (row) => { await ElMessageBox.confirm('确定删除？'); await request.delete('/homework/delete', { data: [row.id] }); ElMessage.success('已删除'); loadData() }
</script>
