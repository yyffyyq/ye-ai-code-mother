<template>
  <div class="carousel-manage-container">
    <a-card title="轮播图浏览与管理" :bordered="false" class="main-card">
      <template #extra>
        <a-button type="primary" @click="openAddModal">
          <plus-outlined /> 新增轮播图
        </a-button>
      </template>
      
      <!-- 分区浏览 (locationType: 1-首页, 2-副页, 3-其他) -->
      <a-tabs v-model:activeKey="activeTab" @change="onTabChange" size="large">
        <a-tab-pane key="1" tab="首页轮播图" />
        <a-tab-pane key="2" tab="副页轮播图" />
        <a-tab-pane key="3" tab="其他" />
      </a-tabs>

      <!-- 搜索过滤栏 -->
      <div class="filter-bar">
        <span class="filter-label">审核状态筛选：</span>
        <a-radio-group v-model:value="auditStatusFilter" @change="fetchData" button-style="solid">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="0">待审核</a-radio-button>
          <a-radio-button :value="1">已通过</a-radio-button>
          <a-radio-button :value="2">已驳回</a-radio-button>
        </a-radio-group>
        
        <a-button @click="fetchData" style="margin-left: 16px;">
          <reload-outlined /> 刷新数据
        </a-button>
      </div>

      <!-- 图片瀑布流/卡片列表 -->
      <a-list
        :grid="{ gutter: 24, xs: 1, sm: 2, md: 3, lg: 4, xl: 4, xxl: 4 }"
        :data-source="data"
        :loading="loading"
        :pagination="pagination"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <a-card hoverable class="carousel-card">
              <template #cover>
                <div class="image-wrapper">
                  <a-image 
                    :src="item.imageUrl" 
                    :fallback="fallbackImage"
                    :alt="'轮播图-' + item.id" 
                  />
                  <!-- 悬浮层状态标签 -->
                  <div class="status-badge" :class="getStatusClass(item.auditStatus)">
                    {{ getStatusText(item.auditStatus) }}
                  </div>
                </div>
              </template>
              <template #actions>
                <a-tooltip title="播放排序 (越小越靠前)">
                  <span class="action-icon"><swap-outlined style="transform: rotate(90deg);" /> {{ item.sortOrder }}</span>
                </a-tooltip>
                <a-tooltip title="编辑轮播图属性">
                  <edit-outlined class="action-icon edit-icon" @click="openEditModal(item)" />
                </a-tooltip>
                <a-tooltip title="逻辑删除这张图片">
                   <a-popconfirm
                      title="确定要删除这张轮播图吗？"
                      ok-text="确定"
                      cancel-text="取消"
                      @confirm="doDelete(item.id)"
                    >
                      <delete-outlined class="action-icon delete-icon" />
                   </a-popconfirm>
                </a-tooltip>
              </template>
              <a-card-meta>
                <template #title>
                  <div class="meta-title">
                    <span>轮播编号: {{ item.id }}</span>
                  </div>
                </template>
                <template #description>
                  <div class="desc-row">创建时间: {{ item.createTime || '-' }}</div>
                  <div class="desc-row">更新时间: {{ item.updateTime || '-' }}</div>
                </template>
              </a-card-meta>
            </a-card>
          </a-list-item>
        </template>
        
        <!-- 空状态 -->
        <template #empty>
          <a-empty description="暂无该分区的轮播图数据" />
        </template>
      </a-list>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { message } from 'ant-design-vue'
import { EditOutlined, DeleteOutlined, PlusOutlined, ReloadOutlined, SwapOutlined } from '@ant-design/icons-vue'

// 对应后端的 CarouselManager 实体类
interface CarouselManager {
  id: number
  imageUrl: string
  locationType: number
  sortOrder: number
  auditStatus: number
  createTime: string
  updateTime: string
  isDeleted: number
}

// 图片加载失败的默认图占位
const fallbackImage = 'https://via.placeholder.com/400x200?text=No+Image'

const activeTab = ref('1') // 默认: 1-首页
const auditStatusFilter = ref<number | null>(null)
const loading = ref(false)
const data = ref<CarouselManager[]>([])
const total = ref(0)

const searchParams = reactive({
  pageNum: 1,
  pageSize: 8,
})

// 数据分页配置
const pagination = computed(() => ({
  current: searchParams.pageNum,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  pageSizeOptions: ['8', '16', '32'],
  onChange: (page: number, pageSize: number) => {
    searchParams.pageNum = page
    searchParams.pageSize = pageSize
    fetchData()
  },
}))

// 获取数据 (模拟调用后端 API / 后续接真实接口)
const fetchData = async () => {
  loading.value = true
  try {
    // 【对接后端代码提示】：
    // 将下面的模拟数据逻辑删掉，取消注释这里的代码，替换为真实的 API 请求
    // const res = await listCarouselManagerByPage({
    //   pageNum: searchParams.pageNum,
    //   pageSize: searchParams.pageSize,
    //   locationType: Number(activeTab.value),
    //   auditStatus: auditStatusFilter.value
    // });
    // if (res.data.code === 0) {
    //   data.value = res.data.data.records ?? []
    //   total.value = res.data.data.totalRow ?? 0
    // } else { ... }
    
    // 【演示用假数据】：模拟网络延时加载 --------------------
    setTimeout(() => {
      const mockData: CarouselManager[] = []
      for (let i = 0; i < searchParams.pageSize; i++) {
        const id = (searchParams.pageNum - 1) * searchParams.pageSize + i + 1
        if (id > 25) break // 仅仅为了模拟总数封顶25个
        
        const locType = Number(activeTab.value)
        const status = auditStatusFilter.value !== null ? auditStatusFilter.value : Math.floor(Math.random() * 3)
        // 伪随机图 ID
        const imgId = id + locType * 13
        
        mockData.push({
          id,
          imageUrl: `https://picsum.photos/id/${imgId % 1000}/400/200`,
          locationType: locType,
          sortOrder: i + 1,
          auditStatus: status,
          createTime: '2026-03-09 10:00:00',
          updateTime: '2026-03-09 12:00:00',
          isDeleted: 0,
        })
      }
      data.value = mockData
      total.value = 25 
      loading.value = false
    }, 500)
    // ----------------------------------------------------
  } catch (err) {
    message.error('加载轮播图数据失败')
    loading.value = false
  }
}

// 切换Tab分区时触发
const onTabChange = () => {
  searchParams.pageNum = 1 // 重置到第一页
  fetchData()
}

// 状态文本转换 (0-待审核, 1-已通过, 2-已驳回)
const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '待审核'
    case 1: return '已通过'
    case 2: return '已驳回'
    default: return '未知状态'
  }
}

// 状态对应的 CSS 圆角标签背景色类
const getStatusClass = (status: number) => {
  switch (status) {
    case 0: return 'status-warning'
    case 1: return 'status-success'
    case 2: return 'status-error'
    default: return 'status-default'
  }
}

// 占位操作函数集，后续接业务逻辑
const openAddModal = () => {
  message.info('这里将呼出 [新增/上传] 弹窗...')
}

const openEditModal = (item: CarouselManager) => {
  message.info(`准备修改编号为 [${item.id}] 的轮播图配置...`)
}

const doDelete = (id: number) => {
  // 模拟逻辑删除操作
  message.success(`已删除编号为 ${id} 的轮播图!`)
  // 真实业务这里要 await delete API 然后再查一遍:
  fetchData()
}

// 初始化钩子
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.carousel-manage-container {
  padding: 24px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 64px);
}

.main-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.filter-bar {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 8px;
  display: flex;
  align-items: center;
}

.filter-label {
  font-weight: 500;
  margin-right: 12px;
  color: #333;
}

/* 瀑布流卡片效果 */
.carousel-card {
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.carousel-card:hover {
  box-shadow: 0 10px 24px rgba(0,0,0,0.12);
  transform: translateY(-4px);
}

.image-wrapper {
  position: relative;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e9ecef;
  overflow: hidden;
}

/* 确保预览图撑满且缩放自如 */
.image-wrapper :deep(.ant-image) {
  width: 100%;
  height: 100%;
}
.image-wrapper :deep(img) {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.carousel-card:hover .image-wrapper :deep(img) {
  transform: scale(1.08); /* hover 时放大 */
}

/* 悬浮层状态标签，置于图片右上角 */
.status-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  z-index: 10;
  box-shadow: 0 2px 6px rgba(0,0,0,0.25);
  backdrop-filter: blur(2px);
}

.status-warning { background-color: rgba(250, 173, 20, 0.9); }
.status-success { background-color: rgba(82, 196, 26, 0.9); }
.status-error { background-color: rgba(255, 77, 79, 0.9); }
.status-default { background-color: rgba(217, 217, 217, 0.9); color: #333; }

.meta-title {
  font-size: 15px;
  font-weight: bold;
}

.desc-row {
  margin-top: 6px;
  font-size: 12px;
  color: #8c8c8c;
}

/* 底部操作区小图标 */
.action-icon {
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: color 0.3s;
}
.edit-icon:hover { color: #1890ff; }
.delete-icon:hover { color: #ff4d4f; }
</style>
