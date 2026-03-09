import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getLogin } from '@/api/userController.ts'


/**
 * 登录用户信息的全局包
 */
export const userLoginUserStore = defineStore('loginUser', () => {
  //全局默认值

  const loginUser = ref<API.LoginUserVO>({
    userName: '未为查询到登录',
  })

  //获取用户登录信息
  async  function fetchLoginUser(){
    const res = await getLogin()
    if(res.data.code === 0 && res.data.data){
      loginUser.value=res.data.data
    }
  }
  function setLoginUser(newLoginUser: any){
    loginUser.value= newLoginUser;
  }

  return { loginUser ,fetchLoginUser,setLoginUser}
})
