// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /carouselLocation/getInfo/${param0} */
export async function getInfo1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInfo1Params,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.CarouselLocation>(`/carouselLocation/getInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /carouselLocation/list */
export async function list(options?: { [key: string]: any }) {
  return request<API.BaseResponseListCarouselLocation>('/carouselLocation/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /carouselLocation/page */
export async function page1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.page1Params,
  options?: { [key: string]: any }
) {
  return request<API.PageCarouselLocation>('/carouselLocation/page', {
    method: 'GET',
    params: {
      ...params,
      page: undefined,
      ...params['page'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /carouselLocation/remove/${param0} */
export async function remove1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.remove1Params,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<boolean>(`/carouselLocation/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /carouselLocation/save */
export async function save(body: API.CarouselLocation, options?: { [key: string]: any }) {
  return request<boolean>('/carouselLocation/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /carouselLocation/update */
export async function update1(body: API.CarouselLocation, options?: { [key: string]: any }) {
  return request<boolean>('/carouselLocation/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
