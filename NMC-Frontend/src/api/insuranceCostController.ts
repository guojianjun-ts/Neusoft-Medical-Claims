// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** getAllCategoryCost GET /api/api/insurance/cost/all/category/${param0} */
export async function getAllCategoryCostUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAllCategoryCostUsingGETParams,
  options?: { [key: string]: any }
) {
  const { patientId: param0, ...queryParams } = params
  return request<API.BaseResponseMapStringBigdecimal_>(
    `/api/api/insurance/cost/all/category/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** getDrugCategoryCost GET /api/api/insurance/cost/drug/category/${param0} */
export async function getDrugCategoryCostUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDrugCategoryCostUsingGETParams,
  options?: { [key: string]: any }
) {
  const { patientId: param0, ...queryParams } = params
  return request<API.BaseResponseMapStringBigdecimal_>(
    `/api/api/insurance/cost/drug/category/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** getTotalCost GET /api/api/insurance/cost/total/${param0} */
export async function getTotalCostUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTotalCostUsingGETParams,
  options?: { [key: string]: any }
) {
  const { patientId: param0, ...queryParams } = params
  return request<API.BaseResponseBigdecimal_>(`/api/api/insurance/cost/total/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
