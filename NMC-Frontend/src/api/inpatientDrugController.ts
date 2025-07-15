// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDrug POST /api/inpatient/drug/add */
export async function addDrugUsingPost(
  body: API.AddInpatientDrugRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/drug/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** checkDrugExists GET /api/inpatient/drug/check */
export async function checkDrugExistsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkDrugExistsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/drug/check', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** deleteDrug POST /api/inpatient/drug/delete */
export async function deleteDrugUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDrugUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/drug/delete', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listDrugInfoByPage GET /api/inpatient/drug/list/page/vo */
export async function listDrugInfoByPageUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDrugInfoByPageUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageInpatientDrugVO_>('/api/inpatient/drug/list/page/vo', {
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
