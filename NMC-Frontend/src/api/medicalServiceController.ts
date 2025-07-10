// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addMedicalService POST /api/service/add */
export async function addMedicalServiceUsingPost(
  body: API.MedicalService,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/service/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteMedicalService DELETE /api/service/delete/${param0} */
export async function deleteMedicalServiceUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteMedicalServiceUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/service/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** deleteMedicalServiceByIds DELETE /api/service/delete/batch */
export async function deleteMedicalServiceByIdsUsingDelete(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/service/delete/batch', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getMedicalServiceByName GET /api/service/getByName */
export async function getMedicalServiceByNameUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMedicalServiceByNameUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListMedicalService_>('/api/service/getByName', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listMedicalServiceByPage GET /api/service/list/page */
export async function listMedicalServiceByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listMedicalServiceByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageMedicalService_>('/api/service/list/page', {
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

/** updateMedicalService PUT /api/service/update */
export async function updateMedicalServiceUsingPut(
  body: API.MedicalService,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/service/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
