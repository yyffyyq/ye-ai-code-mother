<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="../assets/logo.png" alt="Logo" />
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
            <a-button type="primary" @click="router.push('/user/userLogin')">登录</a-button>
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
/* ================== 顶部导航栏整体样式 ================== */
.header {
  /* 使用清新淡蓝色背景 (带有极轻微的渐变显得更通透) */
  background: linear-gradient(90deg, #e6f7ff 0%, #f0f5ff 100%);
  padding: 0 24px;
  height: 64px; /* Ant Design 标准高度 */
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.08); /* 阴影也带一点点微弱的蓝色呼应 */
  position: sticky; /* 吸顶效果 */
  top: 0;
  z-index: 100;
}

/* 强制 a-row 内部垂直居中 (因为不改 template，所以用深度选择器) */
.header :deep(.ant-row) {
  height: 64px;
  align-items: center;
}

/* ================== 左侧 Logo 区域 ================== */
.header-left {
  display: flex;
  align-items: center;
  height: 100%;
  cursor: pointer;
}

.logo {
  height: 36px; /* 稍微缩小一点，更显精致 */
  object-fit: contain;
  transition: transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.header-left:hover .logo {
  transform: scale(1.06); /* 鼠标悬浮 Logo 轻微放大 */
}

/* ================== 中间导航菜单样式 ================== */
/* 穿透修改 Antd 默认菜单样式 */
.header :deep(.ant-menu-horizontal) {
  border-bottom: none !important;
  background: transparent !important;
  line-height: 64px; /* 和头部高度保持一致，保证指示条在最底部 */
  font-size: 15px;
  font-weight: 500;
}

/* ================== 右侧用户操作区域 ================== */
.user-login-status {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
}

/* 利用 CSS 让头像和名字强行水平居中对齐 (针对 v-if 那个 div) */
.user-login-status > div {
  display: flex;
  align-items: center;
  gap: 12px; /* 头像和名字的间距 */
}

/* 头像微调 */
.img-avart {
  border: 1px solid #f0f0f0;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.img-avart:hover {
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.12); /* 头像悬浮发光感 */
}

/* 下拉触发文字样式 */
.ant-dropdown-link {
  color: #333333; /* 配合白底改成深灰色 */
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px; /* 文字和箭头的间距 */
  transition: color 0.3s;
  text-decoration: none;
}

.ant-dropdown-link:hover {
  color: #1890ff; /* 悬浮变回 Antd 主题蓝 */
}

/* 箭头悬浮翻转动画 */
.ant-dropdown-link :deep(.anticon-down) {
  transition: transform 0.3s;
  font-size: 12px;
  color: #8c8c8c;
}

.ant-dropdown-link:hover :deep(.anticon-down) {
  transform: rotate(180deg); /* 悬浮时箭头优雅翻转 */
  color: #1890ff;
}
</style>
