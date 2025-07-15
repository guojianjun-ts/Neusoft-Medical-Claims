// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDisease POST /api/inpatient/disease/add */
export async function addDiseaseUsingPost(
  body: API.AddInpatientDiseaseRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/disease/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** checkDiseaseExists GET /api/inpatient/disease/check */
export async function checkDiseaseExistsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkDiseaseExistsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/disease/check', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** deleteDisease POST /api/inpatient/disease/delete/${param0} */
export async function deleteDiseaseUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDiseaseUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/inpatient/disease/delete/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** listDiseaseByPage GET /api/inpatient/disease/list/page */
export async function listDiseaseByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiseaseByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageInpatientDisease_>('/api/inpatient/disease/list/page', {
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

/** listDiseaseInfoByPage GET /api/inpatient/disease/list/page/vo */
export async function listDiseaseInfoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiseaseInfoByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDiseaseInfo_>('/api/inpatient/disease/list/page/vo', {
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
