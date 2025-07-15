// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDiagnosis POST /api/inpatient/diagnosis/add */
export async function addDiagnosisUsingPost(
  body: API.AddInpatientDiagnosisRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/diagnosis/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** checkDiagnosisExists GET /api/inpatient/diagnosis/check */
export async function checkDiagnosisExistsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkDiagnosisExistsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/diagnosis/check', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** deleteDiagnosis POST /api/inpatient/diagnosis/delete */
export async function deleteDiagnosisUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDiagnosisUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/inpatient/diagnosis/delete', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listDiagnosisInfoByPage GET /api/inpatient/diagnosis/list/page/vo */
export async function listDiagnosisInfoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiagnosisInfoByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageInpatientDiagnosisVO_>(
    '/api/inpatient/diagnosis/list/page/vo',
    {
      method: 'GET',
      params: {
        // current has a default value: 1
        current: '1',
        // size has a default value: 10
        size: '10',
        ...params,
      },
      ...(options || {}),
    }
  )
}
