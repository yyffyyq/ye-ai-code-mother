<template>
  <div class="carousel-manage-container">
    <!-- 顶部操作与导航区域 -->
    <div class="top-section">
      <div class="left-nav">
        <div class="page-title">
          <picture-outlined class="title-icon" />
          <span>轮播图管理</span>
        </div>
        <!-- 预留导航栏占位 -->
        <a-menu mode="horizontal" class="nav-menu">
          <a-menu-item key="list">内容列表</a-menu-item>
          <a-menu-item key="config" disabled>高级配置</a-menu-item>
        </a-menu>
      </div>

      <div class="right-ops">
        <a-button type="primary" size="large" @click="showModal">
          <template #icon><plus-outlined /></template>
          新增轮播图
        </a-button>
      </div>
    </div>

    <!-- 底部列表展示区域 -->
    <div class="bottom-section">
      <div class="list-card">
        <div class="card-header">
          <span class="header-text">轮播图列表预览</span>
        </div>
        <div class="card-content">
          <div class="list-placeholder">
            <a-empty description="此处用于浏览数据库内的轮播图列表" />
            <p class="hint-text">数据加载功能开发中...</p>
          </div>
        </div>
      </div>
    </div>

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
            <p class="ant-upload-drag-icon">
              <inbox-outlined />
            </p>
            <p class="ant-upload-text">将文件拖到此处，或<em>点击上传</em></p>
            <p class="ant-upload-hint">建议尺寸 1920x400，附件不超过 2MB</p>
          </a-upload-dragger>
        </a-form-item>
        <a-form-item label="显示位置">
          <a-input-number v-model:value="formState.locationType" :min="0" placeholder="请输入位置类型" style="width: 100%" />
        </a-form-item>
        <a-form-item label="排序权重">
          <a-input-number v-model:value="formState.sortOrder" :min="0" placeholder="数值越大越靠前" style="width: 100%" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { PlusOutlined, InboxOutlined, PictureOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { upload, saveCarousel,list } from '@/api/carouselManagerController.ts'

// 控制弹窗显示
const visible = ref<boolean>(false)
const confirmLoading = ref<boolean>(false)
const uploading = ref<boolean>(false)
const fileList = ref<any[]>([])

// 表单数据
const formState = reactive<API.CarouselManagerDto>({
  imageUrl: '',
  locationType: 0,
  sortOrder: 0,
})

// 显示弹窗
const showModal = () => {
  resetForm()          // 打开前先清空旧的业务数据
  fileList.value = []  // 打开前清空上传组件的 UI 列表
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

// 提交表单
const handleOk = async () => {
  if (!formState.imageUrl) {
    message.warning('请先上传图片')
    return
  }

  confirmLoading.value = true
  try {
    const res = await saveCarousel(formState)
    if (res.data.code === 0) {
      message.success('保存成功')
      visible.value = false
      resetForm()
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

// 取消弹窗
const handleCancel = () => {
  resetForm()
}

// 重置表单
const resetForm = () => {
  formState.imageUrl = ''
  formState.locationType = 0
  formState.sortOrder = 0
}
</script>

<style scoped>
.carousel-manage-container {
  padding: 24px;
  background-color: #f0f2f5;
  width: 1850px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 顶部区域 */
.top-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 0 24px;
  height: 72px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.left-nav {
  display: flex;
  align-items: center;
  gap: 40px;
}

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
  line-height: 72px;
}

/* 底部区域 */
.bottom-section {
  flex: 1;
}

.list-card {
  background: #fff;
  border-radius: 8px;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.card-header {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.header-text {
  font-size: 16px;
  font-weight: 500;
}

.card-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-placeholder {
  text-align: center;
}

.hint-text {
  color: #8c8c8c;
  margin-top: 12px;
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
