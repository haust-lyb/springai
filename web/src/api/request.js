import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const service = axios.create({
  baseURL: '/api',
  timeout: 30000
})

service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
      config.headers.satoken = userStore.token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    const userStore = useUserStore()
    if (error.response) {
      const { status } = error.response
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        userStore.logout()
        router.push('/login')
      } else if (status === 403) {
        ElMessage.error('没有权限访问')
      } else if (status === 500) {
        ElMessage.error('服务器错误')
      } else {
        ElMessage.error(error.response.data?.message || '请求失败')
      }
    }
    return Promise.reject(error)
  }
)

export default service
