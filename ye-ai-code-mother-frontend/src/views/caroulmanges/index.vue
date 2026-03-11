<template>
  <carousel-management-layout>
    <!-- 头部左侧：标题与导航 -->
    <template #header-left>
      <div class="page-title">
        <picture-outlined class="title-icon" />
        <span>轮播图管理</span>
      </div>
      <!--这里修改一下，采用获取轮播图list然后根据这个list提取名称然后显示出来，id不显示出来只显示名称-->
      <!--已经添加了接口（getCarouselBylocationType）-->
      <a-menu
        v-if="locationList.length > 0"
        v-model:selectedKeys="activeMenuKey"
        mode="horizontal"
        class="nav-menu"
        @click="handleMenuClick"
      >
        <a-menu-item v-for="item in locationList" :key="String(item.id)">
          {{ item.name }}
        </a-menu-item>
        <a-menu-item key="config" disabled>高级配置</a-menu-item>
      </a-menu>
    </template>

    <!-- 头部右侧：操作按钮 -->
    <template #header-right>
      <a-button type="primary" size="large" @click="showModal">
        <template #icon><plus-outlined /></template>
        新增轮播图
      </a-button>
    </template>

    <!-- 主体内容：列表展示 -->
    <template #content>
      <div class="list-card">
        <div class="card-content">
          <div v-if="carouselList.length === 0 && !loading" class="list-placeholder">
            <a-empty description="该位置暂无轮播图资料" />
          </div>

          <a-list
            v-else
            :grid="{ gutter: 24, xs: 1, sm: 2, md: 3, lg: 4, xl: 4, xxl: 4 }"
            :data-source="carouselList"
            :loading="loading"
            :pagination="pagination"
          >
            <template #renderItem="{ item, index }">
              <a-list-item>
                <a-card hoverable class="carousel-card">
                  <template #cover>
                    <div class="card-cover-wrapper">
                      <img :src="getImageUrl(item.imageUrl)" alt="轮播图" class="card-image" />
                      <div
                        class="status-badge"
                        :class="item.isDeleted === 0 ? 'status-active' : 'status-deleted'"
                      >
                        {{ item.isDeleted === 0 ? '正常展示' : '已下线' }}
                      </div>
                    </div>
                  </template>

                  <a-card-meta>
                    <template #description>
                      <div class="card-info">
                        <p><span class="info-label">创建时间:</span> {{ formatTime(item.createTime) }}</p>
                      </div>
                      <div class="card-info">
                        <p><span class="info-label">最近更新:</span> {{ formatTime(item.updateTime) || '-' }}</p>
                      </div>
                    </template>
                  </a-card-meta>

                  <template #actions>
                    <div class="action-item">
                      <swap-outlined /> {{ item.sortOrder || item.displayOrder }}
                    </div>
                    <edit-outlined key="edit" @click="showModalForEdit(item)" />
                    <delete-outlined key="delete" @click="deleteCarousel(item)" />
                  </template>
                </a-card>
              </a-list-item>
            </template>
          </a-list>
        </div>
      </div>
    </template>
  </carousel-management-layout>

  <!-- 轮播图上传弹窗部分 -->
  <a-modal
    v-model:visible="visible"
    title="新增轮播图"
    @ok="handleOk"
    @cancel="handleCancel"
    :confirm-loading="confirmLoading"
    ok-text="提交"
    cancel-text="取消"
    width="600px"
  >
    <!--图片上传部分-->
    <a-form :model="formState" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="轮播图图片" required>
        <a-upload-dragger
          name="file"
          v-model:file-list="fileList"
          :multiple="false"
          :show-upload-list="true"
          :max-count="1"
          :custom-request="handleUpload"
          accept="image/*"
        >
          <template v-if="formState.imageUrl">
            <img
              :src="getUrl(formState.imageUrl)"
              alt="轮播图"
              style="width: 100%; max-height: 200px; object-fit: contain"
            />
          </template>
          <p class="ant-upload-drag-icon">
            <inbox-outlined />
          </p>
          <p class="ant-upload-text">将文件拖到此处，或<em>点击上传</em></p>
          <p class="ant-upload-hint">建议尺寸 1920x400，附件不超过 2MB</p>
        </a-upload-dragger>
      </a-form-item>
      <a-form-item label="显示位置">
        <a-tag color="blue" style="font-size: 14px; padding: 4px 10px">
          {{ currentLocationName }}
        </a-tag>
      </a-form-item>
      <a-form-item label="排序权重">
        <a-input-number
          v-model:value="formState.displayOrder"
          :min="0"
          placeholder="数值越大越靠前"
          style="width: 100%"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import { computed, createVNode, onMounted, reactive, ref } from 'vue'
import {
  PlusOutlined,
  InboxOutlined,
  PictureOutlined,
  EditOutlined,
  DeleteOutlined,
  SwapOutlined, ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import {
  upload,
  saveCarousel,
  update,
  listCarouselManagerPageVo, remove
} from '@/api/carouselManagerController.ts'
import { list } from '@/api/carouselLocationController.ts'
import CarouselManagementLayout from './layout/CarouselManagementLayout.vue'
import dayjs from 'dayjs'
// =============================================格式化时间函数=======
const formatTime = (time: string | null) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

// =============================== 导航栏 (位置分类) 相关逻辑 =================
// =============================== 获取轮播图信息通过locationTypeid===========
// 用于存放当前选中位置的轮播图列表数据
const carouselList = ref<any[]>([])
// 用于控制加载中的 loading 圈圈
const loading = ref<boolean>(false)
//==========制作一个变量用于存放点击之后变值，方便之后在点击添加轮播图的时候不需要再选择1、2、3
const locationTypeForcarouselSaveKey = ref<string>('')
// 分页配置对象
const pagination = reactive({
  current: 1, // 当前页数
  pageSize: 8, // 每页条数 (建议设为 4 的倍数，比如 8，因为你的网格是 4 列)
  total: 0, // 数据总数
  showSizeChanger: true, // 是否允许改变每页条数
  // 当页码或 pageSize 改变时触发
  onChange: (page: number, pageSize: number) => {
    pagination.current = page
    pagination.pageSize = pageSize
    // 页码改变后，重新拉取当前位置的数据
    fetchCarouselData(locationTypeForcarouselSaveKey.value)
  },
})
// 监听用户点击导航栏切换的操作
const handleMenuClick = (menuInfo: any) => {
  const selectedLocationId = menuInfo.key
  console.log('用户切换到了位置 ID:', selectedLocationId)

  // 如果点击的是高级配置，直接 return
  if (selectedLocationId === 'config') return
  //拿到当前导航栏的值
  locationTypeForcarouselSaveKey.value = selectedLocationId
  // 每次切换左侧位置菜单时，一定要把页码重置为第 1 页
  pagination.current = 1
  // 在这里调用根据位置 ID 获取轮播图列表的接口
  fetchCarouselData(selectedLocationId)
}
const fetchCarouselData = async (selectLocationId: String | number) => {
  // 开启加载动画
  loading.value = true
  try {
    // 调用API接口获取对应位置所属的轮播图列表
    const res = await listCarouselManagerPageVo({
      locationType: Number(selectLocationId),
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    })
    if (res.data.code == 0) {
      // 获取分页返回的数据 (注意：MyBatis-Plus/Flex 返回的列表数据通常在 records 字段里)
      const pageData = res.data.data
      // 赋值给列表，如果没有数据则给空数组兜底
      carouselList.value = pageData?.records || []
      // 更新分页总条数 (如果后端叫 totalRow，请把这里的 total 换成 totalRow)
      pagination.total = Number(pageData?.total || pageData?.totalRow || 0)
    }
  } catch (error) {
    console.log('查询失败' + error)
  } finally {
    loading.value = false // 关闭加载动画
  }
}
// ===========================兼容images文件夹的图片和tmp文件夹的图片===============
const getUrl = (path: string) => {
  // 统一后端基地址 (包含 /api)
  const baseUrl = 'http://localhost:8989/api'
  if (!path) return ''

  // 1. 如果已经是完整的网络链接，直接用
  if (path.startsWith('http')) {
    return path
  }
  // 判断图片前缀
  if (path.startsWith('/tmp/') || path.startsWith('/images/')) {
    return `${baseUrl}${path}`
  }
}

// ==========================只兼容转换images文件夹下的路径======================
const getImageUrl = (path: string) => {
  if (!path) return ''

  // 1. 如果已经是完整的网络链接，直接用
  if (path.startsWith('http')) {
    return path
  }

  // 统一后端基地址 (包含 /api)
  const baseUrl = 'http://localhost:8989/api'

  // 3. 兼容新数据：如果是后端的相对路径 (比如 /images/xxx.png)
  if (path.startsWith('/images/')) {
    return `${baseUrl}${path}`
  }

  // 兜底返回
  return `${baseUrl}/images/${path}`
}

// ============================存放从后端获取的位置列表数据=====================
const locationList = ref<any[]>([])
// 当前选中的导航栏 key (Ant Design 的 selectedKeys 必须是数组，并且 key 通常建议转成字符串)
const activeMenuKey = ref<string[]>([])

// 1. 获取导航栏(位置)数据
const fetchLocations = async () => {
  const res = await list()
  if (res.data.code == 0) {
    //查询成功执行
    // message.success('查询成功')
    //查询成功之后对列表赋值
    locationList.value = res.data.data || []
    //选中导航栏高亮选中
    if (locationList.value.length > 0) {
      const firstId = String(locationList.value[0].id)
      activeMenuKey.value = [firstId]
      //在这里加一个为了在第一次点进来还未点击导航栏的时候可以有数据预加载
      locationTypeForcarouselSaveKey.value = Number([firstId])
      // 刚进页面不仅要选中第一个导航栏，还要把它的轮播图查出来！
      fetchCarouselData(firstId)
    }
  } else {
    //查询失败执行标准
    message.error('查询失败' + res.data.message)
  }
}
// 页面一进入就自动执行
//钩子函数
onMounted(() => {
  fetchLocations()
})
// ====================新增或者修改时所需数据=======================
// 控制弹窗显示
const visible = ref<boolean>(false)
const confirmLoading = ref<boolean>(false)
const uploading = ref<boolean>(false)
const fileList = ref<any[]>([])
// 新增/修改表单数据
const formState = reactive<API.CarouselManagerDto>({
  imageUrl: '',
  locationType: 0,
  displayOrder: 0,
})
// =======================轮播图删除相关逻辑=============================
const deleteCarousel = async (item: API.CarouselManagerDto | any) => {
  Modal.confirm({
    title: '确认删除',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除这条轮播图吗？此操作不可恢复。',
    okText: '确认删除',
    okType: 'danger', // 按钮会变成红色警告样式
    cancelText: '取消',
    // 点击确认后的异步操作写在这里
    async onOk() {
      try {
        // 进行 api 接口调用
        const res = await remove({ id: item.id });
        if (res.data.code === 0) {
          message.success("删除成功");
          //更新
          fetchLocations()

        } else {
          message.error("删除失败：" + (res.data.message || '未知错误'));
        }
      } catch (error) {
        message.error("网络请求出错，删除失败");
        console.error(error);
      }
    },
    // 点击取消的回调（通常不需要写逻辑，弹窗会自动关掉）
    onCancel() {
      console.log('用户取消了删除操作');
    },
  });
}

// ======================= 轮播图编辑相关逻辑 ============================
// 编辑轮播图显示弹窗方法
const showModalForEdit = (item: API.CarouselManagerDto | any) => {
  //在点击轮播图编辑时赋值该值给表单数据
  Object.assign(formState, item)
  //清除上传组件的UI列表
  fileList.value = []
  //这里的展示弹窗可能需要修改，因为我所需要的弹窗是需要图片的回显，然后可以修改上传图片
  //显示弹窗
  visible.value = true
}
// ================= 轮播图添加相关逻辑 =================

// 新增显示弹窗方法
const showModal = () => {
  //清空
  Object.assign(formState, null)
  resetForm() // 打开前先清空旧的业务数据
  fileList.value = [] // 打开前清空上传组件的 UI 列表
  visible.value = true
}

// 图片上传处理
const handleUpload = async (options: any) => {
  const { file, onSuccess, onError } = options

  const formData = new FormData()
  formData.append('file', file)

  uploading.value = true
  try {
    // 调用 API 上传图片
    const res = await upload(formData as any, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    if (res.data.code === 0 && res.data.data) {
      formState.imageUrl = res.data.data
      message.success('图片上传成功')
      console.log(formState.imageUrl)
      onSuccess(res.data)
    } else {
      message.error('图片上传失败：' + (res.data.message || '未知错误'))
      onError(new Error(res.data.message))
    }
  } catch (error: any) {
    message.error('上传过程中出现错误')
    onError(error)
  } finally {
    uploading.value = false
  }
}

// ==================================提交表单================================
// ===================这里还需要做一个是添加还是修改的判断,判断依据为是否有id========
const handleOk = async () => {
  if (!formState.imageUrl) {
    message.warning('请先上传图片')
    return
  }

  confirmLoading.value = true
  console.log('formState.id' + formState.id)
  if (formState.id != null) {
    console.log('这是修改操作')
    try {
      const res = await update(formState)
      if (res.data.code === 0) {
        message.success('修改成功')
        visible.value = false
        resetForm()
        fetchLocations()
      } else {
        message.error('修改失败：' + (res.data.message || '未知错误'))
      }
    } catch (error) {
      message.error('修改过程中出现了报错')
    } finally {
      confirmLoading.value = false
    }
  } else {
    try {
      const res = await saveCarousel(formState)
      if (res.data.code === 0) {
        message.success('保存成功')
        visible.value = false
        resetForm()
        // 这里需要再刷新list函数
        fetchLocations()
        // 这里可以触发列表刷新 logic
      } else {
        message.error('保存失败：' + (res.data.message || '未知错误'))
      }
    } catch (error) {
      message.error('保存过程中出现错误')
    } finally {
      confirmLoading.value = false
    }
  }
}

// 取消弹窗
const handleCancel = () => {
  resetForm()
}

//根据localtionId来获取localtion名称
const currentLocationName = computed(() => {
  // 从我们早就查好的 locationList 里面，找 id 等于当前选中 id 的那个对象
  const currentId = locationTypeForcarouselSaveKey.value
  //todo ====================================查一下这是什么语法？================================================
  const target = locationList.value.find((item) => String(item.id) === String(currentId))

  // 如果找到了就返回 name，没找到就返回个默认提示
  return target ? target.name : '未找到位置名称'
})

// 重置表单
const resetForm = () => {
  formState.imageUrl = ''
  //这样写写台潦草了，需要拿着这个去调用函数找到名称，这样更加的直观一些
  //不可以这样写，只能在显示部分做手脚
  formState.locationType = Number(locationTypeForcarouselSaveKey.value)
  formState.displayOrder = 0
}
</script>

<style scoped>
.page-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #1f1f1f;
}

.title-icon {
  font-size: 22px;
  color: #1890ff;
}

.nav-menu {
  border-bottom: none;
}

/* 列表卡片容器 */
.card-content {
  padding: 24px 0;
}

.list-placeholder {
  text-align: center;
  padding: 100px 0;
}

/* 轮播图卡片样式 */
.carousel-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.carousel-card:hover {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.card-cover-wrapper {
  position: relative;
  height: 180px;
  overflow: hidden;
  background: #fafafa;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 状态标签样式 */
.status-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.status-active {
  background-color: #fadb14; /* 亮黄色，匹配图中的待审核/正常色 */
  color: #000;
}

.status-deleted {
  background-color: #ff4d4f; /* 红色 */
}

/* 标题样式 */
.card-title-text {
  font-size: 15px;
  font-weight: 600;
  color: #262626;
}

.card-info {
  margin-top: 8px;
}

.card-info p {
  margin-bottom: 4px;
  font-size: 12px;
  color: #8c8c8c;
}

.info-label {
  color: #595959;
  font-weight: 500;
}

/* 底部操作项 */
.action-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.ant-upload-drag-icon {
  margin-bottom: 16px !important;
}

.ant-upload-text em {
  color: #1890ff;
  font-style: normal;
  text-decoration: underline;
}
</style>
