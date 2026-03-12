<template>
  <div id="userRegister">
    <h2 class="title">Jintu网站管理用户注册</h2>
  </div>
  <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
    <a-form-item
      label="账号"
      name="userAccount"
      :rules="[{ required: true, message: '请输入账号!' }]"
    >
      <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
    </a-form-item>

    <a-form-item
      label="密码"
      name="userPassword"
      :rules="[
        { required: true, message: '请输入密码！' },
        { min: 8, message: '密码长度不可少于八位' },
      ]"
    >
      <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
    </a-form-item>
    <!--    再次确认密码-->
    <a-form-item
      label="确认密码"
      name="checkPassword"
      :rules="[
        { required: true, message: '请输入密码！' },
        { min: 8, message: '密码长度不可少于八位' },
      ]"
    >
      <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
    </a-form-item>
    <!--    <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">-->
    <!--      <a-checkbox v-model:checked="formState.remember">Remember me</a-checkbox>-->
    <!--    </a-form-item>-->

    <div class="tips">
      有账号，返回
      <RouterLink to="/user/userLogin">登录</RouterLink>
    </div>
    <a-form-item>
      <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { register } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

//初始化传入的值的原本结构
const formState = reactive<API.UserRegiserRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})
//创建表单结束后，也就是点击创建之后的操作
const handleSubmit = async (values: any) => {
  const res = await register(values)
  if (res.data.code == 0 && res.data.data) {
    //这里先不拿新的账号的信息，这里需要做的是让这个用户重新去登录界面登录
    // await loginUserStore.fetchLoginUser()
    message.success('注册成功')
    router.push({
      path: '/user/userLogin',
      replace: true,
    })
  } else {
    message.error('用户登录失败' + res.data.message)
  }
}
</script>
