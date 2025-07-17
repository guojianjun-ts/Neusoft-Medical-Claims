// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** listDiagnosisCostByPatientId GET /api/insurance/cost/diagnosis/list */
export async function listDiagnosisCostByPatientIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiagnosisCostByPatientIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDiagnosisCostVO_>('/api/insurance/cost/diagnosis/list', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',

      // size has a default value: 5
      size: '5',
      ...params,
    },
    ...(options || {}),
  })
}

/** listDrugCostByPatientId GET /api/insurance/cost/drug/list */
export async function listDrugCostByPatientIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDrugCostByPatientIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDrugCostVO_>('/api/insurance/cost/drug/list', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',

      // size has a default value: 5
      size: '5',
      ...params,
    },
    ...(options || {}),
  })
}

/** listServiceCostByPatientId GET /api/insurance/cost/medical/list */
export async function listServiceCostByPatientIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listServiceCostByPatientIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageMedicalServiceCostVO_>('/api/insurance/cost/medical/list', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',

      // size has a default value: 5
      size: '5',
      ...params,
    },
    ...(options || {}),
  })
}

/** listDiseaseByPatientId GET /api/insurance/disease/list */
export async function listDiseaseByPatientIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listDiseaseByPatientIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePatientDiseaseVO_>('/api/insurance/disease/list', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',

      // size has a default value: 5
      size: '5',
      ...params,
    },
    ...(options || {}),
  })
}

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

/** listInsuredPatients GET /api/insurance/patient/list */
export async function listInsuredPatientsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInsuredPatientsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePatientRegistration_>('/api/insurance/patient/list', {
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

/** getHospitalReimbursementList GET /api/insurance/reimbursement/hospital/list */
export async function getHospitalReimbursementListUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getHospitalReimbursementListUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListHospitalReimbursementVO_>(
    '/api/insurance/reimbursement/hospital/list',
    {
      method: 'GET',
      params: {
        // current has a default value: 1
        current: '1',
        // hospitalLevel has a default value: 一级医院
        hospitalLevel: '一级医院',

        // size has a default value: 10
        size: '10',
        ...params,
      },
      ...(options || {}),
    }
  )
}

/** calculateTotalReimbursement GET /api/insurance/reimbursement/total */
export async function calculateTotalReimbursementUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.calculateTotalReimbursementUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBigdecimal_>('/api/insurance/reimbursement/total', {
    method: 'GET',
    params: {
      // hospitalLevel has a default value: 一级医院
      hospitalLevel: '一级医院',

      ...params,
    },
    ...(options || {}),
  })
}
