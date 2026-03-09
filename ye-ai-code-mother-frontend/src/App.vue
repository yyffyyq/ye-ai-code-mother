<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { userLoginUserStore } from '@/stores/loginUser.ts'

// 加载获取到登录用户信息
const loginUserStore = userLoginUserStore();
loginUserStore.fetchLoginUser();

</script>

<template>
  <BasicLayout />
</template>

<style>
</style>
