declare namespace API {
  type AddInpatientDiagnosisRequest = {
    diagnosisId?: number
    doctorOrder?: string
    orderTime?: string
    patientId?: number
    useMethod?: string
  }

  type AddInpatientDiseaseRequest = {
    diseaseId?: number
    diseaseType?: number
    patientId?: number
  }

  type AddInpatientDrugRequest = {
    doctorOrder?: string
    drugId?: number
    endTime?: string
    orderNumber?: number
    patientId?: number
    startTime?: string
    useMethod?: string
  }

  type AddInpatientMedicalRequest = {
    doctorOrder?: string
    medicalId?: number
    orderNumber?: number
    orderTime?: string
    patientId?: number
    useMethod?: string
  }

  type BaseResponseBigdecimal_ = {
    code?: number
    data?: number
    message?: string
  }

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

  type BaseResponseMapStringBigdecimal_ = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponsePageDiagnosisCostVO_ = {
    code?: number
    data?: PageDiagnosisCostVO_
    message?: string
  }

  type BaseResponsePageDiagnosisTreatment_ = {
    code?: number
    data?: PageDiagnosisTreatment_
    message?: string
  }

  type BaseResponsePageDiseaseInfo_ = {
    code?: number
    data?: PageDiseaseInfo_
    message?: string
  }

  type BaseResponsePageDrugCostVO_ = {
    code?: number
    data?: PageDrugCostVO_
    message?: string
  }

  type BaseResponsePageDrugInfo_ = {
    code?: number
    data?: PageDrugInfo_
    message?: string
  }

  type BaseResponsePageInpatientDiagnosisVO_ = {
    code?: number
    data?: PageInpatientDiagnosisVO_
    message?: string
  }

  type BaseResponsePageInpatientDisease_ = {
    code?: number
    data?: PageInpatientDisease_
    message?: string
  }

  type BaseResponsePageInpatientDrugVO_ = {
    code?: number
    data?: PageInpatientDrugVO_
    message?: string
  }

  type BaseResponsePageInpatientMedicalVO_ = {
    code?: number
    data?: PageInpatientMedicalVO_
    message?: string
  }

  type BaseResponsePageMedicalService_ = {
    code?: number
    data?: PageMedicalService_
    message?: string
  }

  type BaseResponsePageMedicalServiceCostVO_ = {
    code?: number
    data?: PageMedicalServiceCostVO_
    message?: string
  }

  type BaseResponsePagePatientDiseaseVO_ = {
    code?: number
    data?: PagePatientDiseaseVO_
    message?: string
  }

  type BaseResponsePagePatientRegistration_ = {
    code?: number
    data?: PagePatientRegistration_
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

  type calculateTotalReimbursementUsingGETParams = {
    /** hospitalLevel */
    hospitalLevel?: string
    /** patientId */
    patientId: number
    /** workStatus */
    workStatus: string
  }

  type checkDiagnosisExistsUsingGETParams = {
    /** diagnosisId */
    diagnosisId: number
    /** patientId */
    patientId: number
  }

  type checkDiseaseExistsUsingGETParams = {
    /** diseaseId */
    diseaseId: number
    /** diseaseType */
    diseaseType?: number
    /** patientId */
    patientId: number
  }

  type checkDrugExistsUsingGETParams = {
    /** drugId */
    drugId: number
    /** patientId */
    patientId: number
  }

  type checkMedicalExistsUsingGETParams = {
    /** medicalId */
    medicalId: number
    /** patientId */
    patientId: number
  }

  type deleteDiagnosisTreatmentUsingDELETEParams = {
    /** id */
    id: number
  }

  type deleteDiagnosisUsingPOSTParams = {
    /** diagnosisId */
    diagnosisId: number
    /** patientId */
    patientId: number
  }

  type deleteDiseaseUsingPOSTParams = {
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

  type deleteDrugUsingPOSTParams = {
    /** drugId */
    drugId: number
    /** patientId */
    patientId: number
  }

  type deleteMedicalServiceUsingDELETEParams = {
    /** id */
    id: number
  }

  type deleteMedicalUsingPOSTParams = {
    /** medicalId */
    medicalId: number
    /** patientId */
    patientId: number
  }

  type deleteUsingDELETEParams = {
    /** id */
    id: number
  }

  type DiagnosisCostVO = {
    diagnosisCode?: string
    diagnosisId?: number
    diagnosisName?: string
    excludeContent?: string
    price?: number
    unit?: string
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

  type DiseaseInfo = {
    diseaseCategory?: string
    diseaseCode?: string
    diseaseICD?: string
    diseaseName?: string
    id?: number
  }

  type DrugCostVO = {
    drugId?: number
    drugName?: string
    manufacturer?: string
    price?: number
    specification?: string
    unit?: string
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

  type getAllCategoryCostUsingGETParams = {
    /** patientId */
    patientId: number
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

  type getDrugCategoryCostUsingGETParams = {
    /** patientId */
    patientId: number
  }

  type getDrugInfoByNameUsingGETParams = {
    /** chineseName */
    chineseName: string
  }

  type getHospitalReimbursementListUsingGETParams = {
    /** current */
    current?: number
    /** hospitalLevel */
    hospitalLevel?: string
    /** patientId */
    patientId: number
    /** size */
    size?: number
    /** workStatus */
    workStatus: string
  }

  type getMedicalServiceByNameUsingGETParams = {
    /** chineseName */
    chineseName: string
  }

  type getTotalCostUsingGETParams = {
    /** patientId */
    patientId: number
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

  type InpatientDiagnosisVO = {
    countryNumber?: string
    id?: number
    treatmentExclude?: string
    treatmentInfo?: string
    treatmentName?: string
    treatmentNumber?: string
    treatmentPrice?: number
    treatmentUnit?: string
  }

  type InpatientDisease = {
    diseaseId?: number
    diseaseType?: number
    id?: number
    orderTime?: string
    patientId?: number
  }

  type InpatientDrugVO = {
    chinaName?: string
    drugManufacturer?: string
    drugPrice?: number
    id?: number
    specifications?: string
  }

  type InpatientMedicalVO = {
    countryNumber?: string
    id?: number
    serviceExclude?: string
    serviceInfo?: string
    serviceName?: string
    serviceNumber?: string
    servicePrice?: number
    serviceUnit?: string
  }

  type listDiagnosisCostByPatientIdUsingGETParams = {
    /** current */
    current?: number
    /** patientId */
    patientId: number
    /** size */
    size?: number
  }

  type listDiagnosisInfoByPageUsingGETParams = {
    /** current */
    current?: number
    /** size */
    size?: number
    /** treatmentName */
    treatmentName?: string
  }

  type listDiagnosisTreatmentByPageUsingGETParams = {
    /** current */
    current?: number
    /** size */
    size?: number
    /** treatmentName */
    treatmentName?: string
  }

  type listDiseaseByPageUsingGETParams = {
    /** current */
    current?: number
    /** diseaseType */
    diseaseType?: number
    /** patientId */
    patientId: number
    /** size */
    size?: number
  }

  type listDiseaseByPatientIdUsingGETParams = {
    /** current */
    current?: number
    /** patientId */
    patientId: number
    /** size */
    size?: number
  }

  type listDiseaseInfoByPageUsingGETParams = {
    /** current */
    current?: number
    /** diseaseName */
    diseaseName?: string
    /** size */
    size?: number
  }

  type listDrugCostByPatientIdUsingGETParams = {
    /** current */
    current?: number
    /** patientId */
    patientId: number
    /** size */
    size?: number
  }

  type listDrugInfoByPageUsingGET1Params = {
    /** chineseName */
    chineseName?: string
    /** current */
    current?: number
    /** size */
    size?: number
  }

  type listDrugInfoByPageUsingGETParams = {
    /** chinaName */
    chinaName?: string
    /** current */
    current?: number
    /** size */
    size?: number
  }

  type listInsuredPatientsUsingGETParams = {
    /** current */
    current?: number
    /** name */
    name?: string
    /** size */
    size?: number
  }

  type listMedicalInfoByPageUsingGETParams = {
    /** chineseName */
    chineseName?: string
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

  type listPatientByPageUsingGETParams = {
    /** current */
    current?: number
    /** patientName */
    patientName?: string
    /** size */
    size?: number
  }

  type listServiceCostByPatientIdUsingGETParams = {
    /** current */
    current?: number
    /** patientId */
    patientId: number
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

  type MapStringBigdecimal_ = true

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

  type MedicalServiceCostVO = {
    excludeContent?: string
    medicalId?: number
    price?: number
    serviceCode?: string
    serviceName?: string
    unit?: string
  }

  type PageDiagnosisCostVO_ = {
    current?: number
    pages?: number
    records?: DiagnosisCostVO[]
    size?: number
    total?: number
  }

  type PageDiagnosisTreatment_ = {
    current?: number
    pages?: number
    records?: DiagnosisTreatment[]
    size?: number
    total?: number
  }

  type PageDiseaseInfo_ = {
    current?: number
    pages?: number
    records?: DiseaseInfo[]
    size?: number
    total?: number
  }

  type PageDrugCostVO_ = {
    current?: number
    pages?: number
    records?: DrugCostVO[]
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

  type PageInpatientDiagnosisVO_ = {
    current?: number
    pages?: number
    records?: InpatientDiagnosisVO[]
    size?: number
    total?: number
  }

  type PageInpatientDisease_ = {
    current?: number
    pages?: number
    records?: InpatientDisease[]
    size?: number
    total?: number
  }

  type PageInpatientDrugVO_ = {
    current?: number
    pages?: number
    records?: InpatientDrugVO[]
    size?: number
    total?: number
  }

  type PageInpatientMedicalVO_ = {
    current?: number
    pages?: number
    records?: InpatientMedicalVO[]
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

  type PageMedicalServiceCostVO_ = {
    current?: number
    pages?: number
    records?: MedicalServiceCostVO[]
    size?: number
    total?: number
  }

  type PagePatientDiseaseVO_ = {
    current?: number
    pages?: number
    records?: PatientDiseaseVO[]
    size?: number
    total?: number
  }

  type PagePatientRegistration_ = {
    current?: number
    pages?: number
    records?: PatientRegistration[]
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

  type PatientDiseaseVO = {
    diseaseName?: string
    diseaseType?: number
    id?: number
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

  type updatePatientInfoUsingPUTParams = {
    /** id */
    id: number
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
