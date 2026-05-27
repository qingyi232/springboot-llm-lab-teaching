import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Token'] = token
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 0) {
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        localStorage.removeItem('role')
        router.push('/login')
        ElMessage.error('登录已过期，请重新登录')
      } else {
        ElMessage.error(res.msg || '请求失败')
      }
      return Promise.reject(res)
    }
    return res
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('role')
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
