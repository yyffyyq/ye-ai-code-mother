import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  // base: '/webmanage/',
  base: '/api',
  server: {
    host: '0.0.0.0', // 允许外部 IP 访问
    port: 5173,      // 默认端口
    proxy: {
      target:'http://localhost:8989'
      // '/api/manage': {
      //   target: 'https://www.zjintu.cn', // 你的服务器地址
      //   changeOrigin: true
      // }
    }
  },
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
