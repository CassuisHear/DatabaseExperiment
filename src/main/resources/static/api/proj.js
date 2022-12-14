// 查询项目
const getProjList = (params) => {
    return $axios({
        url: '/proj/page',
        method: 'get',
        params
    })
}

const deleteProj = (ids) => {
    return $axios({
        url: '/proj',
        method: 'delete',
        params: {ids}
    })
}

const addProj = (params) => {
    return $axios({
        url: '/proj',
        method: 'post',
        data: {...params}
    })
}

const updateProj = (params) => {
    return $axios({
        url: '/proj',
        method: 'put',
        data: {...params}
    })
}


// 修改接口
const editCategory = (params) => {
    return $axios({
        url: '/proj',
        method: 'put',
        data: {...params}
    })
}

// 新增接口
const addCategory = (params) => {
    return $axios({
        url: '/proj',
        method: 'post',
        data: {...params}
    })
}

// 编辑页面反查详情接口
const queryCategoryById = (id) => {
    return $axios({
        url: `/proj/${id}`,
        method: 'get'
    })
}


// 删除当前列的接口
const deleCategory = (ids) => {
    return $axios({
        url: '/proj',
        method: 'delete',
        params: {ids}
    })
}

