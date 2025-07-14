declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseInt_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseListDiagnosisTreatment_ = {
    code?: number
    data?: DiagnosisTreatment[]
    message?: string
  }

  type BaseResponseListDrugInfo_ = {
    code?: number
    data?: DrugInfo[]
    message?: string
  }

  type BaseResponseListDrugReimbursement_ = {
    code?: number
    data?: DrugReimbursement[]
    message?: string
  }

  type BaseResponseListHospitalReimbursementVO_ = {
    code?: number
    data?: HospitalReimbursementVO[]
    message?: string
  }

  type BaseResponseListMedicalService_ = {
    code?: number
    data?: MedicalService[]
    message?: string
  }

  type BaseResponseLoginUserVO_ = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponsePageDiagnosisTreatment_ = {
    code?: number
    data?: PageDiagnosisTreatment_
    message?: string
  }

  type BaseResponsePageDrugInfo_ = {
    code?: number
    data?: PageDrugInfo_
    message?: string
  }

  type BaseResponsePageMedicalService_ = {
    code?: number
    data?: PageMedicalService_
    message?: string
  }

  type BaseResponsePagePatientVO_ = {
    code?: number
    data?: PagePatientVO_
    message?: string
  }

  type BaseResponsePatientRegistration_ = {
    code?: number
    data?: PatientRegistration
    message?: string
  }

  type deleteDiagnosisTreatmentUsingDELETEParams = {
    /** id */
    id: number
  }

  type deleteDrugInfoUsingDELETEParams = {
    /** id */
    id: number
  }

  type deleteDrugReimbursementUsingPOSTParams = {
    /** id */
    id: number
  }

  type deleteMedicalServiceUsingDELETEParams = {
    /** id */
    id: number
  }

  type deleteUsingDELETEParams = {
    /** id */
    id: number
  }

  type DiagnosisTreatment = {
    countryNumber?: string
    id?: number
    remark?: string
    treatmentExclude?: string
    treatmentInfo?: string
    treatmentName?: string
    treatmentNumber?: string
    treatmentPrice?: number
    treatmentType?: string
    treatmentUnit?: string
  }

  type DrugInfo = {
    chinaName?: string
    drugManufacturer?: string
    drugPrice?: number
    drugUnit?: string
    goodsName?: string
    id?: number
    insuranceType?: string
    remarks?: string
    specifications?: string
  }

  type DrugReimbursement = {
    drugReimbursementInfo?: string
    drugReimbursementProportion?: number
    drugReimbursementType?: string
    drugStatus?: number
    id?: number
  }

  type getByLevelUsingGETParams = {
    /** hospitalLevel */
    hospitalLevel: string
  }

  type getCurrentPatientUsingGETParams = {
    /** patientId */
    patientId: number
  }

  type getDiagnosisTreatmentByNameUsingGETParams = {
    /** chineseName */
    chineseName: string
  }

  type getDrugInfoByNameUsingGETParams = {
    /** chineseName */
    chineseName: string
  }

  type getMedicalServiceByNameUsingGETParams = {
    /** chineseName */
    chineseName: string
  }

  type HospitalReimbursement = {
    hospitalLevel?: string
    id?: number
    maxPayLevel?: number
    minPayLevel?: number
    payProportion?: number
    peopleType?: number
    status?: number
  }

  type HospitalReimbursementVO = {
    id?: number
    payRange?: string
    peopleTypeDesc?: string
    proportionDesc?: string
    statusDesc?: string
  }

  type listDiagnosisTreatmentByPageUsingGETParams = {
    /** current */
    current?: number
    /** size */
    size?: number
    /** treatmentName */
    treatmentName?: string
  }

  type listDrugInfoByPageUsingGETParams = {
    /** chinaName */
    chinaName?: string
    /** current */
    current?: number
    /** size */
    size?: number
  }

  type listMedicalServiceByPageUsingGETParams = {
    /** current */
    current?: number
    /** serviceName */
    serviceName?: string
    /** size */
    size?: number
  }

  type listPatientVOByPageUsingGETParams = {
    /** current */
    current?: number
    /** patientName */
    patientName?: string
    /** size */
    size?: number
  }

  type LoginUserVO = {
    createTime?: string
    id?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userStatus?: number
  }

  type MedicalService = {
    countryNumber?: string
    id?: number
    remark?: string
    serviceExclude?: string
    serviceInfo?: string
    serviceName?: string
    serviceNumber?: string
    servicePrice?: number
    serviceType?: string
    serviceUnit?: string
  }

  type PageDiagnosisTreatment_ = {
    current?: number
    pages?: number
    records?: DiagnosisTreatment[]
    size?: number
    total?: number
  }

  type PageDrugInfo_ = {
    current?: number
    pages?: number
    records?: DrugInfo[]
    size?: number
    total?: number
  }

  type PageMedicalService_ = {
    current?: number
    pages?: number
    records?: MedicalService[]
    size?: number
    total?: number
  }

  type PagePatientVO_ = {
    current?: number
    pages?: number
    records?: PatientVO[]
    size?: number
    total?: number
  }

  type PatientRegistration = {
    ageType?: string
    birthday?: string
    cardNumber?: string
    caseNumber?: string
    gender?: string
    homeAddress?: string
    id?: number
    page?: number
    patientName?: string
    paymentType?: string
    visitDate?: string
    workStatus?: string
  }

  type PatientVO = {
    cardNumber?: string
    caseNumber?: string
    gender?: string
    id?: number
    patientName?: string
    paymentType?: string
    visitDate?: string
    workStatus?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserRegisterRequest = {
    checkPassword?: string
    userAccount?: string
    userPassword?: string
  }
}
