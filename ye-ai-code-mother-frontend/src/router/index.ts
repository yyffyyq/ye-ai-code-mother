import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import UserLogin from '@/views/user/UserLogin.vue'
import UserRegister from '@/views/user/UserRegister.vue'
import UserManange from '@/views/admin/UserManange.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/user/userLogin',
      name: '用户登录',
      component: UserLogin,
    },
    {
      path: '/user/userRegister',
      name: '用户注册',
      component: UserRegister,
    },
    {
      path: '/admin/UserManange',
      name: '管理员',
      component: UserManange,
    },
    {
      path: '/caroulmanges',
      name: '轮播图管理',
      component: () => import('@/views/caroulmanges/index.vue'),
    },
  ],
})

export default router
