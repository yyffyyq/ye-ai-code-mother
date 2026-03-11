<template>
  <div class="carousel-management-layout">
    <div class="layout-header">
      <div class="header-left">
        <slot name="header-left"></slot>
      </div>
      <div class="header-right">
        <slot name="header-right"></slot>
      </div>
    </div>
    <div class="layout-content">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
// 通用布局组件，支持左右分区的头部和下方主体内容
</script>

<style scoped>
.carousel-management-layout {
  width: 1800px;
  margin: 0 auto;
  min-height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 0 24px;
  height: 72px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 24px;
  height: 100%;
}

.header-right {
  display: flex;
  align-items: center;
}

.layout-content {
  flex: 1;
  padding: 24px;
  width: 100%;
  margin: 0 auto;
}
</style>
