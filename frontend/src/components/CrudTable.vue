<template>
  <div class="page-card">
    <div class="search-bar">
      <slot name="search" />
      <el-button type="primary" @click="$emit('search')"><el-icon><Search /></el-icon>搜索</el-button>
      <el-button type="primary" @click="$emit('add')" v-if="showAdd"><el-icon><Plus /></el-icon>新增</el-button>
    </div>
    <el-table :data="data" stripe v-loading="loading">
      <slot />
      <el-table-column label="操作" :width="actionWidth" fixed="right" v-if="showActions">
        <template #default="scope">
          <slot name="actions" :row="scope.row">
            <el-button type="primary" size="small" @click="$emit('edit', scope.row)" v-if="showEdit">编辑</el-button>
            <el-button type="danger" size="small" @click="$emit('del', scope.row)" v-if="showDel">删除</el-button>
          </slot>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 16px;" v-if="total > 0">
      <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="$emit('page-change', $event)" />
    </div>
  </div>
</template>
<script setup>
const props = defineProps({
  data: { type: Array, default: () => [] },
  total: { type: Number, default: 0 },
  pageSize: { type: Number, default: 10 },
  loading: { type: Boolean, default: false },
  showAdd: { type: Boolean, default: true },
  showEdit: { type: Boolean, default: true },
  showDel: { type: Boolean, default: true },
  showActions: { type: Boolean, default: true },
  actionWidth: { type: [String, Number], default: 160 },
})
const currentPage = defineModel('page', { type: Number, default: 1 })
defineEmits(['search', 'add', 'edit', 'del', 'page-change'])
</script>
