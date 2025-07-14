// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addPatient POST /api/patient/add */
export async function addPatientUsingPost(
  body: API.PatientRegistration,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/patient/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getCurrentPatient GET /api/patient/get */
export async function getCurrentPatientUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCurrentPatientUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePatientRegistration_>('/api/patient/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listPatientVOByPage GET /api/patient/list/page/vo */
export async function listPatientVoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listPatientVOByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePatientVO_>('/api/patient/list/page/vo', {
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

/** updatePatient POST /api/patient/update */
export async function updatePatientUsingPost(
  body: API.PatientRegistration,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/patient/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
