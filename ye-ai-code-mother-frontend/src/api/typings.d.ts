declare namespace API {
  type BaseResponse = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseCarouselDescriptionVO = {
    code?: number
    data?: CarouselDescriptionVO
    message?: string
  }

  type BaseResponseCarouselManagerVO = {
    code?: number
    data?: CarouselManagerVO
    message?: string
  }

  type BaseResponseListCarouselImageUrlVO = {
    code?: number
    data?: CarouselImageUrlVO[]
    message?: string
  }

  type BaseResponseListCarouselLocation = {
    code?: number
    data?: CarouselLocation[]
    message?: string
  }

  type BaseResponseListCarouselManagerVO = {
    code?: number
    data?: CarouselManagerVO[]
    message?: string
  }

  type BaseResponseLoginUserVO = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponsePageCarouselManagerVO = {
    code?: number
    data?: PageCarouselManagerVO
    message?: string
  }

  type BaseResponsePageUserVO = {
    code?: number
    data?: PageUserVO
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUser = {
    code?: number
    data?: User
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type CarouselDescriptionDTO = {
    id?: number
    hrefUrl?: string
    description?: string
    descriptionTime?: string
  }

  type CarouselDescriptionVO = {
    hrefUrl?: string
    description?: string
    descriptionTime?: string
  }

  type CarouselImageUrlVO = {
    imageUrl?: string
    displayOrder?: number
    isDeleted?: number
  }

  type CarouselLocation = {
    id?: number
    name?: string
    description?: string
    createTime?: string
    updateTime?: string
  }

  type CarouselManager = {
    id?: number
    imageUrl?: string
    locationType?: number
    displayOrder?: number
    auditStatus?: number
    createTime?: string
    updateTime?: string
    isDeleted?: number
    hrefUrl?: string
    description?: string
    descriptionTime?: string
  }

  type CarouselManagerDto = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    imageUrl?: string
    locationType?: number
    displayOrder?: number
  }

  type CarouselManagerVO = {
    id?: number
    imageUrl?: string
    displayOrder?: number
    isDeleted?: number
    createTime?: string
    updateTime?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getByIdForDescriptionParams = {
    id: number
  }

  type getByLocationTypeParams = {
    carouselLocationType: number
  }

  type getCarouselBylocationTypeParams = {
    location_type: number
  }

  type getInfo1Params = {
    id: number
  }

  type getInfoParams = {
    id: number
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserVOByIdParams = {
    id: number
  }

  type LoginUserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
    updateTime?: string
  }

  type PageCarouselLocation = {
    records?: CarouselLocation[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageCarouselManagerVO = {
    records?: CarouselManagerVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type pageParams = {
    page: PageCarouselLocation
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type remove1Params = {
    id: number
  }

  type removeParams = {
    id: number
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type UserAddRequest = {
    userName?: string
    userAccount?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userName?: string
    userAccount?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegiserRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserUpdateRequest = {
    id?: number
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
  }
}
