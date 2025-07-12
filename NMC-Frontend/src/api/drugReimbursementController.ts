// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addDrugReimbursement POST /api/drugReimbursement/add */
export async function addDrugReimbursementUsingPost(
  body: API.DrugReimbursement,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/drugReimbursement/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteDrugReimbursement POST /api/drugReimbursement/delete */
export async function deleteDrugReimbursementUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDrugReimbursementUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/drugReimbursement/delete', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listAllReimbursement GET /api/drugReimbursement/listAll */
export async function listAllReimbursementUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListDrugReimbursement_>('/api/drugReimbursement/listAll', {
    method: 'GET',
    ...(options || {}),
  })
}

/** updateDrugReimbursement POST /api/drugReimbursement/update */
export async function updateDrugReimbursementUsingPost(
  body: API.DrugReimbursement,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/drugReimbursement/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
