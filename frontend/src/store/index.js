import { createPinia, defineStore } from 'pinia'

export const pinia = createPinia()

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    role: localStorage.getItem('role') || '',
  }),
  actions: {
    setLogin(token, userInfo, role) {
      this.token = token
      this.userInfo = userInfo
      this.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      localStorage.setItem('role', role)
    },
    logout() {
      this.token = ''
      this.userInfo = {}
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('role')
    }
  }
})
