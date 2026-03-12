// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 批量更新 批量更新传入数据sortorder POST /carouselManager/batchUpdate/ByIdList */
export async function batchUpdateByIdList(body: number[], options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/carouselManager/batchUpdate/ByIdList', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 编辑图片描述和活动时间 编辑图片描述和活动时间 POST /carouselManager/editDescription */
export async function editDescription(
  body: API.CarouselDescriptionDTO,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/carouselManager/editDescription', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取图片描述和时间 用于数据回显 GET /carouselManager/getByIdForDescription/${param0} */
export async function getByIdForDescription(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdForDescriptionParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseCarouselDescriptionVO>(
    `/carouselManager/getByIdForDescription/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 不分页查询 根据位置类型查询轮播图 GET /carouselManager/getByLocationType/${param0} */
export async function getByLocationType(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByLocationTypeParams,
  options?: { [key: string]: any }
) {
  const { carouselLocationType: param0, ...queryParams } = params
  return request<API.BaseResponseListCarouselImageUrlVO>(
    `/carouselManager/getByLocationType/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 根据位置获取轮播图列表 传入位置类型的ID（例如：1-首页，2-副页），返回该位置下的所有轮播图数据 GET /carouselManager/getCarouselBylocationType/${param0} */
export async function getCarouselBylocationType(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCarouselBylocationTypeParams,
  options?: { [key: string]: any }
) {
  const { location_type: param0, ...queryParams } = params
  return request<API.BaseResponseListCarouselManagerVO>(
    `/carouselManager/getCarouselBylocationType/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 根据主键查询 用于根据图片id查询对应轮播图图片 GET /carouselManager/getInfo/${param0} */
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

/** 分页查询 查几个几页怎么查根据发送的请求决定 POST /carouselManager/list/page/vo */
export async function listCarouselManagerPageVo(
  body: API.CarouselManagerDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageCarouselManagerVO>('/carouselManager/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据主键删除 用于根据图片id删除轮播图图片 DELETE /carouselManager/remove/${param0} */
export async function remove(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/carouselManager/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 保存信息如数据库 将提交的数据表单保存到数据库中 POST /carouselManager/saveCarousel */
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

/** 根据主键更新 用于根据图片id修改轮播图图片 PUT /carouselManager/update */
export async function update(body: API.CarouselManagerDto, options?: { [key: string]: any }) {
  return request<API.BaseResponseCarouselManagerVO>('/carouselManager/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 上传轮播图 用于上传图片的接口，返回值为图片存储位置 POST /carouselManager/upload */
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
