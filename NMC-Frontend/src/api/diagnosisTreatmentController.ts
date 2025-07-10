// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDiagnosisTreatment POST /api/treatment/add */
export async function addDiagnosisTreatmentUsingPost(
  body: API.DiagnosisTreatment,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/treatment/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteDiagnosisTreatment DELETE /api/treatment/delete/${param0} */
export async function deleteDiagnosisTreatmentUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDiagnosisTreatmentUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/treatment/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** deleteDiagnosisTreatmentByIds DELETE /api/treatment/delete/batch */
export async function deleteDiagnosisTreatmentByIdsUsingDelete(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/treatment/delete/batch', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getDiagnosisTreatmentByName GET /api/treatment/getByName */
export async function getDiagnosisTreatmentByNameUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDiagnosisTreatmentByNameUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDiagnosisTreatment_>('/api/treatment/getByName', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listDiagnosisTreatmentByPage GET /api/treatment/list/page */
export async function listDiagnosisTreatmentByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiagnosisTreatmentByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDiagnosisTreatment_>('/api/treatment/list/page', {
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

/** updateDiagnosisTreatment PUT /api/treatment/update */
export async function updateDiagnosisTreatmentUsingPut(
  body: API.DiagnosisTreatment,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/treatment/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
