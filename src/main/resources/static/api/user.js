// 查询列表接口
const getUserList = (params) => {
    return $axios({
        url: '/user/page',
        method: 'get',
        params
    })
}

// 查询项目
const getProjList = (params) => {
    return $axios({
        url: '/proj',
        method: 'get',
        params
    })
}

// const getUserProjList = (id) => {
//     return $axios({
//         url: '/user/getProjs',
//         method: 'get',
//         params
//     })
// }

const addUser = (params) => {
    return $axios({
        url: '/user',
        method: 'post',
        data: {...params}
    })
}

// 删除接口
const deleteUser = (ids) => {
    return $axios({
        url: '/user',
        method: 'delete',
        params: {ids}
    })
}

const updateUser = (params) => {
    return $axios({
        url: '/user',
        method: 'put',
        data: {...params}
    })
}

const updateUserDto = (params) => {
    return $axios({
        url: '/user/userDto',
        method: 'put',
        data: {...params}
    })
}

const queryUserById = (id) => {
    return $axios({
        url: `/user/${id}`,
        method: 'get'
    })
}

// 修改接口
const editDish = (params) => {
    return $axios({
        url: '/dish',
        method: 'put',
        data: {...params}
    })
}

// 新增接口
const addDish = (params) => {
    return $axios({
        url: '/dish',
        method: 'post',
        data: {...params}
    })
}

// 查询详情
const queryDishById = (id) => {
    return $axios({
        url: `/dish/${id}`,
        method: 'get'
    })
}

// 获取菜品分类列表
const getCategoryList = (params) => {
    return $axios({
        url: '/proj/list',
        method: 'get',
        params
    })
}

// 查菜品列表的接口
const queryDishList = (params) => {
    return $axios({
        url: '/dish/list',
        method: 'get',
        params
    })
}

// 文件down预览
const commonDownload = (params) => {
    return $axios({
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        url: '/common/download',
        method: 'get',
        params
    })
}

// 起售停售---批量起售停售接口
const dishStatusByStatus = (params) => {
    return $axios({
        url: `/dish/status/${params.status}`,
        method: 'post',
        params: {ids: params.id}
    })
}
