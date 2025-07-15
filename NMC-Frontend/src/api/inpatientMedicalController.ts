// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addMedical POST /api/inpatient/medical/add */
export async function addMedicalUsingPost(
  body: API.AddInpatientMedicalRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/medical/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** checkMedicalExists GET /api/inpatient/medical/check */
export async function checkMedicalExistsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkMedicalExistsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/medical/check', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** deleteMedical POST /api/inpatient/medical/delete */
export async function deleteMedicalUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteMedicalUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/medical/delete', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listMedicalInfoByPage GET /api/inpatient/medical/list/page/vo */
export async function listMedicalInfoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listMedicalInfoByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageInpatientMedicalVO_>('/api/inpatient/medical/list/page/vo', {
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
