<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  CircleCheck,
  Delete,
  Edit,
  Key,
  Plus,
  Refresh,
  UserFilled,
  Warning
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import userManagementApi from '@/api/userManagement.js'
import PageQueryTable from '@/components/common/PageQueryTable.vue'

const loading = ref(false)
const saving = ref(false)
const passwordSaving = ref(false)
const users = ref([])
const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
})
const stats = reactive({
  total: 0,
  enabled: 0,
  disabled: 0,
  admin: 0,
  builtin: 0
})
const queryForm = reactive({
  keyword: '',
  role: 'all',
  status: 'all'
})
const appliedQuery = reactive({
  keyword: '',
  role: 'all',
  status: 'all'
})

const userDialogVisible = ref(false)
const passwordDialogVisible = ref(false)
const dialogMode = ref('create')
const activeUser = ref(null)
const userFormRef = ref()
const passwordFormRef = ref()

const userForm = reactive({
  username: '',
  password: '',
  nickname: '',
  email: '',
  role: 'user',
  status: 'enabled'
})

const passwordForm = reactive({
  password: ''
})

const roleOptions = [
  { label: '普通用户', value: 'user', type: 'info' },
  { label: '管理员', value: 'admin', type: 'primary' }
]

const statusOptions = [
  { label: '启用', value: 'enabled', type: 'success' },
  { label: '禁用', value: 'disabled', type: 'warning' }
]

const userRules = computed(() => ({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: dialogMode.value === 'create'
    ? [{ required: true, message: '请输入初始密码', trigger: 'blur' }]
    : [],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}))

const passwordRules = {
  password: [{ required: true, message: '请输入新密码', trigger: 'blur' }]
}

const isBuiltinAdmin = (user) => user?.isBuiltin && user?.role === 'admin'

const syncQuery = (target, source) => {
  target.keyword = source.keyword
  target.role = source.role
  target.status = source.status
}

const resetQuery = (target) => {
  target.keyword = ''
  target.role = 'all'
  target.status = 'all'
}

const buildConditions = () => {
  const conditions = []
  if (appliedQuery.keyword.trim()) {
    conditions.push({
      field: 'keyword',
      operator: 'likeAny',
      value: appliedQuery.keyword.trim(),
      fields: ['username', 'nickname', 'email']
    })
  }
  if (appliedQuery.role !== 'all') {
    conditions.push({ field: 'role', operator: 'eq', value: appliedQuery.role })
  }
  if (appliedQuery.status !== 'all') {
    conditions.push({ field: 'status', operator: 'eq', value: appliedQuery.status })
  }
  return conditions
}

const loadStats = async () => {
  const res = await userManagementApi.getStats()
  if (res.code === 200) {
    stats.total = Number(res.data?.total || 0)
    stats.enabled = Number(res.data?.enabled || 0)
    stats.disabled = Number(res.data?.disabled || 0)
    stats.admin = Number(res.data?.admin || 0)
    stats.builtin = Number(res.data?.builtin || 0)
  } else {
    ElMessage.error(res.message || '获取用户统计失败')
  }
}

const loadUsers = async (pageNo = pagination.pageNo, pageSize = pagination.pageSize) => {
  loading.value = true
  try {
    const res = await userManagementApi.pageUsers({
      pageNo,
      pageSize,
      sortBy: 'createTime',
      sortDirection: 'desc',
      conditions: buildConditions()
    })
    if (res.code === 200) {
      users.value = res.data?.records || []
      pagination.total = Number(res.data?.total || 0)
      pagination.pageNo = Number(res.data?.pageNo || pageNo)
      pagination.pageSize = Number(res.data?.pageSize || pageSize)
    } else {
      ElMessage.error(res.message || '获取用户列表失败')
    }
  } finally {
    loading.value = false
  }
}

const searchUsers = () => {
  syncQuery(appliedQuery, queryForm)
  pagination.pageNo = 1
  loadUsers(1, pagination.pageSize)
}

const resetSearch = () => {
  resetQuery(queryForm)
  resetQuery(appliedQuery)
  pagination.pageNo = 1
  loadUsers(1, pagination.pageSize)
}

const handlePageChange = ({ pageNo, pageSize }) => {
  pagination.pageNo = pageNo
  pagination.pageSize = pageSize
  loadUsers(pageNo, pageSize)
}

const refreshPage = async () => {
  await Promise.all([loadStats(), loadUsers()])
}

const resetUserForm = () => {
  userForm.username = ''
  userForm.password = ''
  userForm.nickname = ''
  userForm.email = ''
  userForm.role = 'user'
  userForm.status = 'enabled'
}

const openCreateDialog = () => {
  dialogMode.value = 'create'
  activeUser.value = null
  resetUserForm()
  userDialogVisible.value = true
}

const openEditDialog = (user) => {
  dialogMode.value = 'edit'
  activeUser.value = user
  userForm.username = user.username
  userForm.password = ''
  userForm.nickname = user.nickname || ''
  userForm.email = user.email || ''
  userForm.role = user.role || 'user'
  userForm.status = user.status || 'enabled'
  userDialogVisible.value = true
}

const submitUser = async () => {
  const valid = await userFormRef.value?.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    const payload = {
      username: userForm.username,
      nickname: userForm.nickname,
      email: userForm.email,
      role: userForm.role,
      status: userForm.status
    }
    let res
    if (dialogMode.value === 'create') {
      res = await userManagementApi.createUser({
        ...payload,
        password: userForm.password
      })
    } else {
      res = await userManagementApi.updateUser(activeUser.value.id, payload)
    }
    if (res.code === 200) {
      ElMessage.success(dialogMode.value === 'create' ? '用户已创建' : '用户已更新')
      userDialogVisible.value = false
      await refreshPage()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } finally {
    saving.value = false
  }
}

const updateRole = async (user, role) => {
  const res = await userManagementApi.updateUser(user.id, { role })
  if (res.code === 200) {
    user.role = res.data.role
    ElMessage.success('角色已更新')
    await loadStats()
  } else {
    ElMessage.error(res.message || '角色更新失败')
    await loadUsers()
  }
}

const updateStatus = async (user, status) => {
  const res = await userManagementApi.updateUser(user.id, { status })
  if (res.code === 200) {
    user.status = res.data.status
    ElMessage.success('状态已更新')
    await loadStats()
  } else {
    ElMessage.error(res.message || '状态更新失败')
    await loadUsers()
  }
}

const openPasswordDialog = (user) => {
  activeUser.value = user
  passwordForm.password = ''
  passwordDialogVisible.value = true
}

const submitPassword = async () => {
  const valid = await passwordFormRef.value?.validate().catch(() => false)
  if (!valid) return
  passwordSaving.value = true
  try {
    const res = await userManagementApi.resetPassword(activeUser.value.id, passwordForm.password)
    if (res.code === 200) {
      ElMessage.success('密码已重置')
      passwordDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '密码重置失败')
    }
  } finally {
    passwordSaving.value = false
  }
}

const deleteUser = async (user) => {
  if (user.isBuiltin) {
    ElMessage.warning('内建用户不能删除')
    return
  }
  const confirmed = await ElMessageBox.confirm(`确定删除用户「${user.username}」吗？`, '删除用户', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    confirmButtonClass: 'el-button--danger',
    type: 'warning'
  }).then(() => true).catch(() => false)
  if (!confirmed) return
  const res = await userManagementApi.deleteUser(user.id)
  if (res.code === 200) {
    ElMessage.success('用户已删除')
    if (users.value.length === 1 && pagination.pageNo > 1) {
      pagination.pageNo -= 1
    }
    await refreshPage()
  } else {
    ElMessage.error(res.message || '删除失败')
  }
}

const formatDate = (value) => {
  if (!value) return '-'
  return String(value).replace('T', ' ').slice(0, 16)
}

onMounted(refreshPage)
</script>

<template>
  <main class="user-page">
    <header class="page-header">
      <div>
        <p class="eyebrow">账号与权限</p>
        <h1>用户管理</h1>
      </div>
      <div class="header-actions">
        <el-button :icon="Refresh" :loading="loading" @click="refreshPage">刷新</el-button>
        <el-button type="primary" :icon="Plus" @click="openCreateDialog">新增用户</el-button>
      </div>
    </header>

    <section class="metric-grid" aria-label="用户概览">
      <div class="metric-item">
        <span class="metric-icon user"><el-icon><UserFilled /></el-icon></span>
        <div>
          <strong>{{ stats.total }}</strong>
          <span>全部用户</span>
        </div>
      </div>
      <div class="metric-item">
        <span class="metric-icon enabled"><el-icon><CircleCheck /></el-icon></span>
        <div>
          <strong>{{ stats.enabled }}</strong>
          <span>启用账号</span>
        </div>
      </div>
      <div class="metric-item">
        <span class="metric-icon admin"><el-icon><Key /></el-icon></span>
        <div>
          <strong>{{ stats.admin }}</strong>
          <span>管理员</span>
        </div>
      </div>
      <div class="metric-item">
        <span class="metric-icon builtin"><el-icon><Warning /></el-icon></span>
        <div>
          <strong>{{ stats.builtin }}</strong>
          <span>内建用户</span>
        </div>
      </div>
    </section>

    <PageQueryTable
      v-model:page-no="pagination.pageNo"
      v-model:page-size="pagination.pageSize"
      :loading="loading"
      :total="pagination.total"
      table-height="calc(100vh - 430px)"
      table-min-height="260px"
      @search="searchUsers"
      @reset="resetSearch"
      @page-change="handlePageChange"
    >
      <template #query>
        <el-input
          v-model="queryForm.keyword"
          clearable
          placeholder="搜索用户名、昵称或邮箱"
          class="search-input"
          @keyup.enter="searchUsers"
        />
        <el-select v-model="queryForm.role" class="filter-select">
          <el-option label="全部角色" value="all" />
          <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="queryForm.status" class="filter-select">
          <el-option label="全部状态" value="all" />
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </template>

      <el-table v-loading="loading" :data="users" row-key="id" height="100%" empty-text="暂无用户数据">
        <el-table-column label="用户" min-width="220">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :size="36" class="table-avatar">{{ (row.nickname || row.username || 'U').slice(0, 1) }}</el-avatar>
              <div class="user-copy">
                <div class="user-title">
                  <span>{{ row.username }}</span>
                  <el-tag v-if="row.isBuiltin" size="small" type="info" effect="plain">内建</el-tag>
                </div>
                <small>{{ row.nickname || '-' }}</small>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180">
          <template #default="{ row }">{{ row.email || '-' }}</template>
        </el-table-column>
        <el-table-column label="角色" width="150">
          <template #default="{ row }">
            <el-select
              v-model="row.role"
              size="small"
              :disabled="isBuiltinAdmin(row)"
              @change="updateRole(row, $event)"
            >
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value">
                <el-tag :type="item.type" effect="light">{{ item.label }}</el-tag>
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="140">
          <template #default="{ row }">
            <el-select v-model="row.status" size="small" @change="updateStatus(row, $event)">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
                <el-tag :type="item.type" effect="light">{{ item.label }}</el-tag>
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="170">
          <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="210">
          <template #default="{ row }">
            <div class="row-actions">
              <el-tooltip content="编辑" placement="top">
                <el-button :icon="Edit" circle @click="openEditDialog(row)" />
              </el-tooltip>
              <el-tooltip content="重置密码" placement="top">
                <el-button :icon="Key" circle @click="openPasswordDialog(row)" />
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button
                  :icon="Delete"
                  circle
                  type="danger"
                  :disabled="row.isBuiltin"
                  @click="deleteUser(row)"
                />
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </PageQueryTable>

    <el-dialog
      v-model="userDialogVisible"
      :title="dialogMode === 'create' ? '新增用户' : '编辑用户'"
      width="520px"
      destroy-on-close
    >
      <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item v-if="dialogMode === 'create'" label="初始密码" prop="password">
          <el-input v-model="userForm.password" type="password" show-password placeholder="请输入初始密码" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="默认使用用户名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="name@example.com" />
        </el-form-item>
        <div class="form-grid">
          <el-form-item label="角色" prop="role">
            <el-select v-model="userForm.role" :disabled="dialogMode === 'edit' && isBuiltinAdmin(activeUser)">
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="userForm.status">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitUser">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="passwordDialogVisible" title="重置密码" width="420px" destroy-on-close>
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-position="top">
        <el-form-item label="新密码" prop="password">
          <el-input v-model="passwordForm.password" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="passwordSaving" @click="submitPassword">确认重置</el-button>
      </template>
    </el-dialog>
  </main>
</template>

<style scoped>
.user-page {
  height: 100vh;
  overflow: auto;
  padding: 28px 36px 34px;
  color: #111827;
  background:
    radial-gradient(circle at 12% 6%, rgba(22, 119, 255, 0.08), transparent 24%),
    linear-gradient(135deg, #f8fbff 0%, #f5f7fb 48%, #ffffff 100%);
}

.page-header,
.header-actions,
.toolbar,
.user-cell,
.user-title,
.row-actions {
  display: flex;
  align-items: center;
}

.page-header {
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 18px;
}

.eyebrow {
  margin-bottom: 5px;
  color: #60708f;
  font-size: 13px;
  font-weight: 800;
}

h1 {
  color: #0f172a;
  font-size: 28px;
  line-height: 1.2;
}

.header-actions {
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(150px, 1fr));
  gap: 14px;
  margin-bottom: 16px;
}

.metric-item {
  min-height: 82px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 13px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 12px 32px rgba(35, 74, 122, 0.06);
}

.metric-icon {
  width: 42px;
  height: 42px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 8px;
  font-size: 20px;
}

.metric-icon.user {
  color: #1677ff;
  background: #eaf3ff;
}

.metric-icon.enabled {
  color: #16a34a;
  background: #eaf8ef;
}

.metric-icon.admin {
  color: #7c3aed;
  background: #f1ebff;
}

.metric-icon.builtin {
  color: #d97706;
  background: #fff4dc;
}

.metric-item strong {
  display: block;
  color: #0f172a;
  font-size: 24px;
  line-height: 1;
}

.metric-item span {
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.toolbar {
  gap: 12px;
  margin-bottom: 14px;
  padding: 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.92);
}

.search-input {
  max-width: 360px;
}

.filter-select {
  width: 150px;
}

.table-panel {
  height: calc(100vh - 275px);
  min-height: 380px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 16px 42px rgba(35, 74, 122, 0.07);
}

.table-avatar {
  flex: 0 0 auto;
  color: #1677ff;
  font-weight: 800;
  background: #eef5ff;
}

.user-cell {
  gap: 11px;
  min-width: 0;
}

.user-copy {
  min-width: 0;
}

.user-title {
  gap: 8px;
  min-width: 0;
  color: #0f172a;
  font-weight: 800;
}

.user-title span:first-child,
.user-copy small {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-copy small {
  display: block;
  max-width: 180px;
  color: #64748b;
}

.row-actions {
  gap: 8px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

:deep(.el-table__header th) {
  color: #475569;
  font-weight: 800;
  background: #f8fafc;
}

:deep(.el-select) {
  width: 100%;
}

@media (max-width: 1180px) {
  .user-page {
    padding: 22px;
  }

  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .table-panel {
    height: calc(100vh - 352px);
  }
}

@media (max-width: 720px) {
  .user-page {
    padding: 16px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .header-actions,
  .toolbar {
    width: 100%;
  }

  .toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .search-input,
  .filter-select {
    width: 100%;
    max-width: none;
  }

  .metric-grid,
  .form-grid {
    grid-template-columns: 1fr;
  }

  .table-panel {
    height: 520px;
  }
}
</style>
