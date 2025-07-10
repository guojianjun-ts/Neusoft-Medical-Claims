// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDrugInfo POST /api/drugInfo/add */
export async function addDrugInfoUsingPost(body: API.DrugInfo, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong_>('/api/drugInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteDrugInfo DELETE /api/drugInfo/delete/${param0} */
export async function deleteDrugInfoUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDrugInfoUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/drugInfo/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** deleteDrugInfoByIds DELETE /api/drugInfo/delete/batch */
export async function deleteDrugInfoByIdsUsingDelete(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/drugInfo/delete/batch', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getDrugInfoByName GET /api/drugInfo/getByName */
export async function getDrugInfoByNameUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDrugInfoByNameUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDrugInfo_>('/api/drugInfo/getByName', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listDrugInfoByPage GET /api/drugInfo/list/page */
export async function listDrugInfoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDrugInfoByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDrugInfo_>('/api/drugInfo/list/page', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',
      // size has a default value: 10
      size: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** updateDrugInfo PUT /api/drugInfo/update */
export async function updateDrugInfoUsingPut(body: API.DrugInfo, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/drugInfo/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
