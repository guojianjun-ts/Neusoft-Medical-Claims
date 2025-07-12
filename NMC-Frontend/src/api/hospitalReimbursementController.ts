// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** add POST /api/api/hospital/reimburse/add */
export async function addUsingPost(
  body: API.HospitalReimbursement,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/api/hospital/reimburse/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** delete DELETE /api/api/hospital/reimburse/delete/${param0} */
export async function deleteUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/api/hospital/reimburse/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** getByLevel GET /api/api/hospital/reimburse/level/${param0} */
export async function getByLevelUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByLevelUsingGETParams,
  options?: { [key: string]: any }
) {
  const { hospitalLevel: param0, ...queryParams } = params
  return request<API.BaseResponseListHospitalReimbursementVO_>(
    `/api/api/hospital/reimburse/level/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** update PUT /api/api/hospital/reimburse/update */
export async function updateUsingPut(
  body: API.HospitalReimbursement,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/api/hospital/reimburse/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
