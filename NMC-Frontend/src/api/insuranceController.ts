// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** updatePatientInfo PUT /api/insurance/patient/${param0} */
export async function updatePatientInfoUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updatePatientInfoUsingPUTParams,
  body: API.PatientRegistration,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/insurance/patient/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  })
}

/** listInsuredPatients GET /api/insurance/patient/list/page */
export async function listInsuredPatientsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInsuredPatientsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePatientRegistration_>('/api/insurance/patient/list/page', {
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
