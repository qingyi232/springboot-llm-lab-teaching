import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  {
    path: '/home',
    component: () => import('../views/front/FrontLayout.vue'),
    children: [
      { path: '', component: () => import('../views/front/Home.vue') },
      { path: '/course-material', component: () => import('../views/front/CourseMaterial.vue') },
      { path: '/course-material/:id', component: () => import('../views/front/CourseMaterialDetail.vue') },
      { path: '/video', component: () => import('../views/front/VideoList.vue') },
      { path: '/video/:id', component: () => import('../views/front/VideoDetail.vue') },
      { path: '/lab', component: () => import('../views/front/LabList.vue') },
      { path: '/lab/:id', component: () => import('../views/front/LabDetail.vue') },
      { path: '/homework', component: () => import('../views/front/HomeworkList.vue') },
      { path: '/forum', component: () => import('../views/front/Forum.vue') },
      { path: '/forum/:id', component: () => import('../views/front/ForumDetail.vue') },
      { path: '/notice', component: () => import('../views/front/NoticeList.vue') },
      { path: '/notice/:id', component: () => import('../views/front/NoticeDetail.vue') },
      { path: '/ai-chat', component: () => import('../views/front/AiChat.vue') },
      { path: '/profile', component: () => import('../views/front/Profile.vue') },
      { path: '/my-reservation', component: () => import('../views/front/MyReservation.vue') },
      { path: '/my-usage', component: () => import('../views/front/MyUsage.vue') },
      { path: '/my-homework', component: () => import('../views/front/MyHomework.vue') },
      { path: '/my-score', component: () => import('../views/front/MyScore.vue') },
      { path: '/my-collection', component: () => import('../views/front/MyCollection.vue') },
      { path: '/my-forum', component: () => import('../views/front/MyForum.vue') },
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { role: '管理员' },
    children: [
      { path: '', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'users', component: () => import('../views/admin/UserManage.vue') },
      { path: 'student', component: () => import('../views/admin/StudentManage.vue') },
      { path: 'teacher', component: () => import('../views/admin/TeacherManage.vue') },
      { path: 'lab-admin', component: () => import('../views/admin/LabAdminManage.vue') },
      { path: 'lab-type', component: () => import('../views/admin/LabTypeManage.vue') },
      { path: 'lab-info', component: () => import('../views/admin/LabInfoManage.vue') },
      { path: 'reservation', component: () => import('../views/admin/ReservationManage.vue') },
      { path: 'usage-record', component: () => import('../views/admin/UsageRecordManage.vue') },
      { path: 'course-type', component: () => import('../views/admin/CourseTypeManage.vue') },
      { path: 'course-material', component: () => import('../views/admin/CourseMaterialManage.vue') },
      { path: 'video', component: () => import('../views/admin/VideoManage.vue') },
      { path: 'homework', component: () => import('../views/admin/HomeworkManage.vue') },
      { path: 'homework-submit', component: () => import('../views/admin/HomeworkSubmitManage.vue') },
      { path: 'homework-score', component: () => import('../views/admin/HomeworkScoreManage.vue') },
      { path: 'forum', component: () => import('../views/admin/ForumManage.vue') },
      { path: 'notice', component: () => import('../views/admin/NoticeManage.vue') },
    ]
  },
  {
    path: '/teacher-manage',
    component: () => import('../views/teacher/TeacherLayout.vue'),
    meta: { role: '教师' },
    children: [
      { path: '', component: () => import('../views/teacher/TeacherDashboard.vue') },
      { path: 'course-material', component: () => import('../views/teacher/TeacherCourseMaterial.vue') },
      { path: 'video', component: () => import('../views/teacher/TeacherVideo.vue') },
      { path: 'homework', component: () => import('../views/teacher/TeacherHomework.vue') },
      { path: 'homework-submit', component: () => import('../views/teacher/TeacherHomeworkSubmit.vue') },
      { path: 'homework-score', component: () => import('../views/teacher/TeacherHomeworkScore.vue') },
      { path: 'notice', component: () => import('../views/teacher/TeacherNotice.vue') },
    ]
  },
  {
    path: '/labadmin-manage',
    component: () => import('../views/labadmin/LabAdminLayout.vue'),
    meta: { role: '实验室管理员' },
    children: [
      { path: '', component: () => import('../views/labadmin/LabAdminDashboard.vue') },
      { path: 'lab-type', component: () => import('../views/admin/LabTypeManage.vue') },
      { path: 'lab-info', component: () => import('../views/labadmin/LabAdminLabInfo.vue') },
      { path: 'reservation', component: () => import('../views/labadmin/LabAdminReservation.vue') },
      { path: 'usage-record', component: () => import('../views/labadmin/LabAdminUsageRecord.vue') },
      { path: 'notice', component: () => import('../views/labadmin/LabAdminNotice.vue') },
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const publicPaths = ['/login', '/register', '/home', '/course-material', '/video', '/lab', '/forum', '/notice']
  const isPublic = publicPaths.some(p => to.path === p || to.path.startsWith(p + '/'))

  if (!token && !isPublic && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
