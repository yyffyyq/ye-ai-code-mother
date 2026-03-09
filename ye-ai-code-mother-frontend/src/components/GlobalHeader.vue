<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/log.png" alt="Logo" />
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <!--显示登录用户状态-->
          <div v-if="loginUserStore.loginUser.id">
            <a-space>
              <a-avatar class="img-avart" :src="loginUserStore.loginUser.userAvatar" />
            </a-space>
            <a-dropdown>
              <a class="ant-dropdown-link" @click.prevent>
                {{ loginUserStore.loginUser.userName ?? '用户' }}
                <DownOutlined />
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <a href="javascript:;">个人信息</a>
                  </a-menu-item>
                  <a-menu-item>
                    <a href="javascript:;">完善个人资料</a>
                  </a-menu-item>
                  <a-menu-item>
                    <a href="javascript:;" @click="userLogout">退出</a>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/userLogin">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { useRouter } from 'vue-router'
import { type MenuProps, message } from 'ant-design-vue'
import { userLoginUserStore } from '@/stores/loginUser.ts'
import { DownOutlined } from '@ant-design/icons-vue'
import { logout } from '@/api/userController.ts'
//获取登录用户状态
const loginUserStore = userLoginUserStore()

const router = useRouter()

/**
 * 用户退出功能
 */

const userLogout = async (values: any) => {
  //用户退出
  const res = await logout(values)
  if (res.data.code == 0 && res.data.data != null) {
    //退出登录之后将这个全局包里的登录状态值修改会未登录状态
    loginUserStore.loginUser = { userName: '未登录' }
    message.success('退出成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('退出失败' + res.data.message)
  }
}

// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})
// 菜单配置项
const originItems = [
  {
    key: '/',
    label: '首页',
    title: '首页',
  },
  {
    key: '/admin/UserManange',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/caroulmanges',
    label: '轮播图管理',
    title: '轮播图管理',
  }
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    
    // 权限校验1：管理员权限页面
    if (menuKey?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    
    // 权限校验2：登录用户才可见的页面
    if (menuKey?.startsWith('/caroulmanges')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || !loginUser.id) {
        return false
      }
    }
    
    return true
  })
}

// 展示在菜单的路由数组
//动态更新
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}
</script>

<style scoped>
.header {
  /* 使用一个清爽的淡蓝色作为背景 */
  background: #f0f8ff; 
  padding: 4px 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 48px;
  /* 移除固定宽度，或者使用 object-fit 保持内容比例 */
  max-width: 100%;
  object-fit: contain;
}

/* 修改部分：标题渐变色 */
.site-title {
  margin: 0;
  font-size: 18px;
  /* 这里的渐变是从 AntD 的主色 #1890ff 到一个更浅的青蓝色 #69c0ff */
  background: linear-gradient(to right, #1890ff, #69c0ff);
  -webkit-background-clip: text; /* 兼容 Webkit 内核浏览器 */
  background-clip: text;
  -webkit-text-fill-color: transparent; /* 文字填充透明，显示背景渐变 */
  color: #1890ff; /* 降级兼容：不支持渐变的浏览器显示纯色 */
  font-weight: bold;
}

.ant-menu-horizontal {
  border-bottom: none !important;
  /* 确保菜单背景透明，跟随父级 header 的底色 */
  background: transparent !important;
  /* 调整行高让文字居中对齐 */
  line-height: 48px;
}

/* 修改部分：用户区域布局 */
.user-profile {
  display: flex;
  align-items: center;
  gap: 12px; /* 这里控制头像和名字的间距 */
  cursor: pointer;
}

.user-name {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.img-avart {
  /* 如果需要微调头像大小可以在这里写 */
}
</style>
