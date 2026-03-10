// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 轮播图按Id查询功能部分 GET /carouselManager/getInfo/${param0} */
export async function getInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInfoParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.CarouselManager>(`/carouselManager/getInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 轮播图查询列表功能部分 GET /carouselManager/list */
export async function list(options?: { [key: string]: any }) {
  return request<API.CarouselManager[]>('/carouselManager/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 轮播图按页查询部分 GET /carouselManager/page */
export async function page(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageParams,
  options?: { [key: string]: any }
) {
  return request<API.PageCarouselManager>('/carouselManager/page', {
    method: 'GET',
    params: {
      ...params,
      page: undefined,
      ...params['page'],
    },
    ...(options || {}),
  })
}

/** 轮播图信息删除按轮播图id功能部分 DELETE /carouselManager/remove/${param0} */
export async function remove(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<boolean>(`/carouselManager/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 轮播图信息保存到数据库功能部分 POST /carouselManager/saveCarousel */
export async function saveCarousel(body: API.CarouselManagerDto, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/carouselManager/saveCarousel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 轮播图信息更新功能部分 PUT /carouselManager/update */
export async function update(body: API.CarouselManager, options?: { [key: string]: any }) {
  return request<boolean>('/carouselManager/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 图片上传功能部分 POST /carouselManager/upload */
export async function upload(body: {}, options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/carouselManager/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
