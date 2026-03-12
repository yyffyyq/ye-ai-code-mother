<template>
  <div id="userLogin">
    <h2 class="title">Jintu网站管理 - 用户登录</h2>
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
      <a-input-password v-model:value="formState.userPassword" />
    </a-form-item>

    <!--    <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">-->
    <!--      <a-checkbox v-model:checked="formState.remember">Remember me</a-checkbox>-->
    <!--    </a-form-item>-->

    <div class="tips">
      没有账号，去
      <RouterLink to="/user/userRegister">注册</RouterLink>
    </div>
    <a-form-item>
      <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
    </a-form-item>
  </a-form>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { login } from '@/api/userController.ts'
import { useRouter } from 'vue-router'
import { userLoginUserStore } from '@/stores/loginUser.ts'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = userLoginUserStore()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  const res = await login(values)
  //登录成功
  if (res.data.code == 0 && res.data.data) {
    //全局包里拿到最新用户登录信息
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('用户登录失败' + res.data.message)
  }
}
</script>
<style>
#userLogin {
  max-width: 480px;
  margin: 0 auto;
}
.title {
  text-align: center;
  margin-bottom: 16px;
}
.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}
.tips {
  text-align: right;
  font-size: 13px;
  color: #bbb;
  margin-bottom: 16px;
}
</style>
